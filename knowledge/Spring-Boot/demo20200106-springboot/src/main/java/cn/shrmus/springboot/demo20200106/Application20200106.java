package cn.shrmus.springboot.demo20200106;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class Application20200106 extends SpringApplication{
    public static void main(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();

        SpringApplication springApplication = springApplicationBuilder.build(args);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(Application20200106.class);

        Application20200106 application20200106 = new Application20200106();
        application20200106.logStartupInfo(true);

//        System.out.println("Spring Boot默认提供的Bean：");
//        String[] beanDefinitionNames = configurableApplicationContext.getBeanDefinitionNames();
//        Arrays.sort(beanDefinitionNames);
//        Arrays.stream(beanDefinitionNames).forEach(System.out::println);

        SpringApplication.exit(configurableApplicationContext);
    }

    @Override
    protected void logStartupInfo(boolean isRoot) {
        System.out.println("===============================> logStartupInfo(boolean isRoot)");
        super.logStartupInfo(isRoot);
    }
}
