package com.rz.auto.tool;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE}) // 该标签的作用域是Class等
@Retention(RetentionPolicy.RUNTIME) // 该标签可以被反射找到
@Documented // 可以被例如javadoc此类的工具文档化，只负责标记，没有成员取值
@Inherited // 允许子类继承父类中的注解
@Import({DogToolRegistry.class})
public @interface EnableTool {
}
