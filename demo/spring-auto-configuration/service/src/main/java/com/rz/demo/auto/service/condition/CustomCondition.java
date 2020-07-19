package com.rz.demo.auto.service.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.reflect.Method;

public class CustomCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Method beanMethod = null;
        try {
            Method introspectedMethod = annotatedTypeMetadata.getClass().getMethod("getIntrospectedMethod");
            beanMethod = (Method) introspectedMethod.invoke(annotatedTypeMetadata, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isMatch = 0 == System.currentTimeMillis() % 2;
        System.out.println("java bean method is " + beanMethod.getName() + ", isMatch is " + isMatch);

        return isMatch;
    }
}
