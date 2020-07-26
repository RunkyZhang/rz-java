ApplicationContextInitializer:
    1. ApplicationContextInitializer是一个回调接口，它会在ConfigurableApplicationContext的refresh()方法调用之前被调用,做一些容器的初始化工作
    2. 系统内置的ApplicationContextInitializer有6个
        In spring-boot-1.5.2.RELEASE.jar:
        DelegatingApplicationContextInitializer: 读取spring.properties文件context.initializer.classes属性配置的Initializer类，并对其进行实例化，然后调用其initialize()方法
        ServerPortInfoApplicationContextInitializer: 将自己注册到ApplicationContext的listener列表中，并监听WebServerInitializedEvent事件，事件被触发时，设置local.{serverNamespace}.port属性为应用的服务端口
        ConfigurationWarningsApplicationContextInitializer: 实例化一个ConfigurationWarningsPostProcessor实例，并将其添加到ApplicationContext的beanFactoryPostProcessors中
        ContextIdApplicationContextInitializer: 为当前的ApplicationContext设置一个唯一标识
        In spring-boot-autoconfigure-1.5.2.RELEASE.jar
        SharedMetadataReaderFactoryContextInitializer: 实例化一个CachingMetadataReaderFactoryPostProcessor实例，并将其添加到ApplicationContext的beanFactoryPostProcessors中。
        AutoConfigurationReportLoggingInitializer:
    3. 注册
        1. 在spring.factories中注册
        org.springframework.context.ApplicationContextInitializer=\
          com.yidian.data.initializer.HelloApplicationContextInitializer
        2. 在main方法中注册
        public static void main(String[] args) {
            SpringApplication springApplication = new SpringApplication(HelloApplication.class);
            springApplication.addInitializers(new HelloApplicationContextInitializer());
            springApplication.run(args);
        }
        3. 在application.properties中配置
        context.initializer.classes=com.yidian.data.initializer.HelloApplicationContextInitializer

ApplicationListener:
    1. 通过ApplicationEvent类和ApplicationListener接口，实现***ApplicationContext***事件处理
    2. 注册
        1. 在spring.factories方式注册
        2. 代码.listeners(new CowApplicationListener())方式注册
        3. 用Config中的bean方式注册
        4. 用@Component方式注册

actuator:
    Spring Boot Actuator端点通过JMX和HTTP公开暴露给外界访问，大多数时候我们使用基于HTTP的Actuator端点，因为它们很容易通过浏览器、CURL命令、shell脚本等方式访问。
    访问：http://localhost:8080/info, http://localhost:8080/health; info在配置文件中配置
    https://www.jdon.com/springboot/actuator.html
    https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html
