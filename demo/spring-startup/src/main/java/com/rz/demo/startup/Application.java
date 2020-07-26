package com.rz.demo.startup;

import com.rz.demo.startup.initializer.CatApplicationContextInitializer;
import com.rz.demo.startup.listener.CowApplicationListener;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.io.ClassPathResource;

/*
SpringApplication启动：https://www.cnblogs.com/duanxz/p/9380569.html
1. 判断运行环境否是web程序，如果(javax.servlet.Servlet和org.springframework.web.context.ConfigurableWebApplicationContext）都在当前的类加载器中，则为true否则为false，判断结果设置到webEnvironment属性中
2. 设置Initializer，setInitializers
3. 设置Listener，setListeners
4. 运行SpringApplication.run
    1.  stopWatch开始执行统计
    2.  获取包括spring.factories中的Listener，然后开始所有的Listener的监听listeners.starting();
    3.  通过args创建并配置当前SpringBoot应用将要使用的Environment
    4.  Banner
    5.  根据webEnvironment的值来决定创建何种类型的ApplicationContext对象
    6.  通过environment，args，listener创建ApplicationContext；在这步遍历执行所有的initialize()
    7.  刷新ApplicationContext；在这步BeanFactory的设置，BeanFactoryPostProcessor接口的执行、BeanPostProcessor接口的执行、自动化配置类的解析、条件注解的解析、国际化的初始化等
    8.  遍历所有注册的ApplicationRunner和CommandLineRunner，并执行其run()方法；ApplicationContext初始化前的最后一步工作
    9.  调用所有的SpringApplicationRunListener的finished()方法
    10. stopWatch结束执行统计
    11. 打印统计log
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ResourceBanner resourceBanner = new ResourceBanner(new ClassPathResource("banner.txt"));
        new SpringApplicationBuilder(Application.class)
                .initializers(new CatApplicationContextInitializer())
                .listeners(new CowApplicationListener())
                .banner(resourceBanner)
                .run(args);
    }
}
