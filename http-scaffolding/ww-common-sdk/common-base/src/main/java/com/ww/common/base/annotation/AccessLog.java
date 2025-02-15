package com.ww.common.base.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD})
@Retention(RUNTIME)
public @interface AccessLog {
    int sampleRate() default 10;
    String strategyName() default "";
    String prefix() default "rpc";
}
