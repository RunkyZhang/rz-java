// Resource&Autowired
Resource和Autowired差不多：区别是Resource是JDK的标签，Autowired是Spring标签；Resource默认用Bean名称查找赋值，Autowired默认用Bean类型查找赋值

// Conditional
@Conditional：他是在Spring4中引入的有条件注册bean的注解，需要配合@Configuration使用。
提供以下方式：
@ConditionalOnBean：当SpringIoc容器内存在指定Bean的条件
@ConditionalOnClass：当SpringIoc容器内存在指定Class的条件
@ConditionalOnExpression：基于SpEL表达式作为判断条件
@ConditionalOnJava：基于JVM版本作为判断条件
@ConditionalOnJndi：在JNDI存在时查找指定的位置
@ConditionalOnMissingBean：当SpringIoc容器内不存在指定Bean的条件
@ConditionalOnMissingClass：当SpringIoc容器内不存在指定Class的条件
@ConditionalOnNotWebApplication：当前项目不是Web项目的条件
@ConditionalOnProperty：指定的属性是否有指定的值
@ConditionalOnResource：类路径是否有指定的值
@ConditionalOnSingleCandidate：当指定Bean在SpringIoc容器内只有一个，或者虽然有多个但是指定首选的Bean
@ConditionalOnWebApplication：当前项目是Web项目的条件

ApplicationContextAware：就是为了获取ApplicationContext对象