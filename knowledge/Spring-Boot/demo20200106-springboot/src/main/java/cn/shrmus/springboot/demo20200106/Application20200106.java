package cn.shrmus.springboot.demo20200106;

import cn.shrmus.springboot.demo20200106.user.UserController;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Import(value = cn.shrmus.springboot.demo20200106.configuration.UserConfiguration.class)
@SpringBootApplication
public class Application20200106 extends SpringApplication{
    public static void main(String[] args) {
//        ResourceLoader resourceLoader = new ClassPathXmlApplicationContext("config/spring/user/applicationContext-user.xml");

        ResourceLoader resourceLoader = new AnnotationConfigApplicationContext("cn.shrmus.springboot.demo20200106.user");
//        ResourceLoader resourceLoader = new AnnotationConfigApplicationContext(cn.shrmus.springboot.demo20200106.configuration.UserConfiguration.class);

        SpringApplication springApplication = new SpringApplication(resourceLoader, Application20200106.class);
//        SpringApplication springApplication = new SpringApplication(Application20200106.class);

        springApplication.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);

//        UserController userController = configurableApplicationContext.getBean(UserController.class);
//        System.out.println(userController);

//        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
////        springApplicationBuilder.lazyInitialization(true);
//
////        SpringApplication springApplication = springApplicationBuilder.build(args);
//
//        SpringApplication springApplication = new SpringApplication(Application20200106.class);
//        springApplication.setLazyInitialization(true);
//
//        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(Application20200106.class);
//
////        Application20200106 application20200106 = new Application20200106();
////        application20200106.logStartupInfo(true);

//        System.out.println("Spring Boot默认提供的Bean：");
//        String[] beanDefinitionNames = configurableApplicationContext.getBeanDefinitionNames();
//        Arrays.sort(beanDefinitionNames);
//        Arrays.stream(beanDefinitionNames).forEach(System.out::println);

        SpringApplication.exit(configurableApplicationContext);
    }

    public Application20200106() {
        System.out.println("===============================> Application20200106()");
    }

    @Override
    protected void logStartupInfo(boolean isRoot) {
        System.out.println("===============================> logStartupInfo(boolean isRoot)");
        super.logStartupInfo(isRoot);
    }

    @PostConstruct
    public void testPostConstruct() {
        System.out.println("============================> testPostConstruct()");
    }

    @PreDestroy
    public void testPreDestroy() {
        System.out.println("============================> testPreDestroy()");
    }

}
