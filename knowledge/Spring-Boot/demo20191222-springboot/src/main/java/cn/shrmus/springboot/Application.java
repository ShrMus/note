package cn.shrmus.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

/*@Import(
    value = {
        MybatisMysqlDataSource.class,
        MybatisOracleDataSource.class
    }
)*/
/*@ComponentScan(
    value = {
        "cn.shrmus.springboot.config",
        "cn.shrmus.springboot.user",
        "cn.shrmus.springboot.product"
    }
)*/
/*@ComponentScans(
    value = {
        @ComponentScan(value = {"cn.shrmus.springboot.config"}),
        @ComponentScan(value = {"cn.shrmus.springboot.user"}),
        @ComponentScan(value = {"cn.shrmus.springboot.product"})
    }
)*/
//@EntityScan
//@ConfigurationPropertiesScan
//@ImportAutoConfiguration
/*@ImportResource(
    value = {
        "classpath:config/spring/applicationContext-mybatisMysqlDataSource.xml",
        "classpath:config/spring/applicationContext-mybatisOracleDataSource.xml"
    }
)*/
//@SpringBootApplication(excludeName = "org.springframework.boot.autoconfigure.aop.AopAutoConfiguration")
//@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.aop.AopAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);

        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);

        System.out.println("Spring Boot默认提供的Bean：");
        String[] beanDefinitionNames = configurableApplicationContext.getBeanDefinitionNames();
        Arrays.sort(beanDefinitionNames);
        Arrays.stream(beanDefinitionNames).forEach(System.out::println);

        System.exit(SpringApplication.exit(configurableApplicationContext));
    }
}
