package com.ww.common.architecture.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KeyBuilder {
    private final Map<AnnotatedElementKey, Expression> expressions = new ConcurrentHashMap<>();

    @Resource
    private ExpressionEvaluator expressionEvaluator;

    public String build(ProceedingJoinPoint proceedingJoinPoint, LocalCacheable localCacheable) {
        Class<?> targetClass = proceedingJoinPoint.getThis().getClass();
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();

        AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
        Expression expression = expressions.get(annotatedElementKey);
        if (null == expression) {
            SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
            expression = spelExpressionParser.parseExpression(localCacheable.keyExpression());
        }

        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(
                proceedingJoinPoint.getThis(),
                targetClass,
                method,
                proceedingJoinPoint.getArgs());
        Object value = expression.getValue(evaluationContext);

        return null == value ? null : value.toString();
    }
}
