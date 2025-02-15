package com.ww.common.base.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD})
@Retention(RUNTIME)
public @interface AccessLoginStatus {
    // 0: 默认值，使用SFA通用策略
    int mode() default 0;
}
