package com.github.schlegel;

import com.mixpanel.mixpanelapi.ClientDelivery;
import com.mixpanel.mixpanelapi.MessageBuilder;
import com.mixpanel.mixpanelapi.MixpanelAPI;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONObject;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class LoggingEventInterceptor {

    private ExpressionEvaluator<String> evaluator = new ExpressionEvaluator<>();
    private MessageBuilder messageBuilder = new MessageBuilder("TOKEN");


    //@After("execution(@com.github.schlegel.LoggingEvent * *(..)) && @annotation(loggingEvent)")
    //@After("execution(* *.*(..)) && @annotation(loggingEvent)")
    @AfterReturning("@annotation(loggingEvent)")
    public void after(JoinPoint joinPoint, LoggingEvent loggingEvent) throws InterruptedException, IOException {

        String event = loggingEvent.value();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String clientid = String.valueOf(userName.hashCode());

        // Gather together a bunch of messages into a single ClientDelivery. This can happen in a separate thread or process from the call to MessageBuilder.event()
        ClientDelivery delivery = new ClientDelivery();

        for (int i = 0; i < 10; i++) {
            // You can send properties along with events
            LoggingEvent.Property[] properties = loggingEvent.properties();
            JSONObject props = new JSONObject();
            for (LoggingEvent.Property property : properties) {
                String key = property.key();
                String value = getValue(joinPoint, property.value());
                props.put(key, value);
            }

            // Create an event
            JSONObject sentEvent = messageBuilder.event(clientid, event, props);
            delivery.addMessage(sentEvent);
        }

        // TODO async delivery

        // Use an instance of MixpanelAPI to send the messages to Mixpanel's servers.
        MixpanelAPI mixpanel = new MixpanelAPI();
        mixpanel.deliver(delivery);
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
