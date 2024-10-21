package com.vf.common.architecture.cache;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExpressionEvaluator extends CachedExpressionEvaluator {
    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final Map<AnnotatedElementKey, Method> targetMethods = new ConcurrentHashMap<>();

    public EvaluationContext createEvaluationContext(Object object, Class<?> targetClass, Method method, Object[] args) {
        return new MethodBasedEvaluationContext(
                new ExpressionRootObject(object, args),
                getTargetMethod(targetClass, method),
                args,
                paramNameDiscoverer);
    }

    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethods.get(annotatedElementKey);
        if (null == targetMethod) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            this.targetMethods.put(annotatedElementKey, targetMethod);
        }

        return targetMethod;
    }
}

class ExpressionRootObject {
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
