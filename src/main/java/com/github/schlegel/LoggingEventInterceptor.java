package com.github.schlegel;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
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

    @Autowired
    private AnalyticsService analyticsService;

    @AfterReturning("@annotation(loggingEvent)")
    public void after(JoinPoint joinPoint, LoggingEvent loggingEvent) throws InterruptedException, IOException {

        // Get client information
        String company = "ACME Company";
        String companyId = "" + company.hashCode();
        String clientid = "2344";
        String email = "newuser@enmacc.de";
        String createdDate = new Date().toString();
        // String.valueOf(SecurityContextHolder.getContext().getAuthentication().getName().hashCode());


        // sent event with properties
        String event = loggingEvent.value();
        Map<String, String> eventProps = new HashMap<>();

        for (LoggingEvent.Property property : loggingEvent.properties()) {
            String key = property.key();
            String value = getValue(joinPoint, property.value());
            eventProps.put(key, value);
        }

        analyticsService.sendEvent(event, clientid, eventProps);

        // identify user

        Map<String, String> idProps = new HashMap<>();
        idProps.put("Company Name", company);
        idProps.put("Company ID", companyId);
        idProps.put("Email", email);
        idProps.put("Created", createdDate);

        analyticsService.sendIdentification(clientid, idProps);
    }


    /**
     * Evaluates SPel Expression value in the current context
     * @param joinPoint
     * @param condition
     * @return
     */
    private String getValue(JoinPoint joinPoint, String condition) {
        Object object = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        Class clazz = joinPoint.getTarget().getClass();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

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

        public EvaluationContext createEvaluationContext(Object object, Class<?> targetClass, Method method, Object[] args) {

            Method targetMethod = getTargetMethod(targetClass, method);
            ExpressionRootObject root = new ExpressionRootObject(object, args);
            return new MethodBasedEvaluationContext(root, targetMethod, args, this.paramNameDiscoverer);
        }

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
