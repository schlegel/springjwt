package com.github.schlegel;

import com.segment.analytics.Analytics;
import com.segment.analytics.Callback;
import com.segment.analytics.Log;
import com.segment.analytics.messages.Message;
import com.segment.analytics.messages.TrackMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class LoggingEventInterceptor {

    private ExpressionEvaluator<String> evaluator = new ExpressionEvaluator<>();

    static final Log STDOUT = new Log() {
        @Override public void print(Level level, String format, Object... args) {
            System.out.println(new Date().toString() + "\t" + level + ":\t" + String.format(format, args));
        }

        @Override public void print(Level level, Throwable error, String format, Object... args) {
            System.out.println(new Date().toString() + "\t" +  level + ":\t" + String.format(format, args));
            System.out.println(error);
        }
    };

    static final Callback CALLBACK = new Callback() {
        @Override public void success(Message message) {
            System.out.println("Uploaded " + message);
        }

        @Override public void failure(Message message, Throwable throwable) {
            System.out.println("Could not upload " + message);
            System.out.println(throwable);
        }
    };


    @AfterReturning("@annotation(loggingEvent)")
    public void after(JoinPoint joinPoint, LoggingEvent loggingEvent) throws InterruptedException, IOException {

        // Get information
        String event = loggingEvent.value();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String company = "ACME Company";
        String clientid = String.valueOf(userName.hashCode());


        Analytics analytics = Analytics.builder("xL7gIljlx2oNXU58Y8x5v98fkgZHdjH1")
                .log(STDOUT)
                .callback(CALLBACK)
                .build();

        // set id of user
//        Map<String, String> idProps = new HashMap<>();
//        idProps.put("name", userName);
//        idProps.put("company", company);
//
//        analytics.enqueue(IdentifyMessage.builder()
//                .userId(clientid)
//                .traits(idProps)
//        );


        // sent event
        Map<String, String> eventProps = new HashMap<>();
        for (LoggingEvent.Property property : loggingEvent.properties()) {
            String key = property.key();
            String value = getValue(joinPoint, property.value());
            eventProps.put(key, value);
        }

        for (int i = 0; i < 50; i++) {
            analytics.enqueue(TrackMessage.builder(event)
                    .userId(clientid)
                    .properties(eventProps)
            );
        }


        analytics.flush();
        // wait for flush message to trigger flushing
        Thread.sleep(1000);
        analytics.shutdown();


        // TODO async delivery
    }



    private String getValue(JoinPoint joinPoint, String condition) {
        return getValue(joinPoint.getTarget(), joinPoint.getArgs(),
                joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), condition);
    }

    private String getValue(Object object, Object[] args, Class clazz, Method method, String condition) {
        if (args == null) {
            return null;
        }
        EvaluationContext evaluationContext = evaluator.createEvaluationContext(object, clazz, method, args);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
        return evaluator.condition(condition, methodKey, evaluationContext, String.class);
    }

    private class ExpressionRootObject {
        private final Object object;

        private final Object[] args;

        public ExpressionRootObject(Object object, Object[] args) {
            this.object = object;
            this.args = args;
        }

        public Object getObject() {
            return object;
        }

        public Object[] getArgs() {
            return args;
        }
    }

    private  class ExpressionEvaluator<T> extends CachedExpressionEvaluator {

        // shared param discoverer since it caches data internally
        private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

        private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

        private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

        /**
         * Create the suitable {@link EvaluationContext} for the specified event handling
         * on the specified method.
         */
        public EvaluationContext createEvaluationContext(Object object, Class<?> targetClass, Method method, Object[] args) {

            Method targetMethod = getTargetMethod(targetClass, method);
            ExpressionRootObject root = new ExpressionRootObject(object, args);
            return new MethodBasedEvaluationContext(root, targetMethod, args, this.paramNameDiscoverer);
        }

        /**
         * Specify if the condition defined by the specified expression matches.
         */
        public T condition(String conditionExpression, AnnotatedElementKey elementKey, EvaluationContext evalContext, Class<T> clazz) {
            return getExpression(this.conditionCache, elementKey, conditionExpression).getValue(evalContext, clazz);
        }

        private Method getTargetMethod(Class<?> targetClass, Method method) {
            AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
            Method targetMethod = this.targetMethodCache.get(methodKey);
            if (targetMethod == null) {
                targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
                if (targetMethod == null) {
                    targetMethod = method;
                }
                this.targetMethodCache.put(methodKey, targetMethod);
            }
            return targetMethod;
        }
    }
}
