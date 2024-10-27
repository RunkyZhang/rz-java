package com.ww.common.architecture.cache;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalCacheable {
    String keyExpression() default "";

    int expireTime() default 0;
}
