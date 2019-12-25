package cn.shrmus.springboot.demo20191222;

import cn.shrmus.springboot.demo20191222.config.MysqlDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/*
@Import(
        value = {
                cn.shrmus.springboot.demo20191222.config.MysqlDataSource.class,
                cn.shrmus.springboot.demo20191222.config.OracleDataSource.class
        }
)
*/
/*
@ComponentScan(
    value = {
        "cn.shrmus.springboot.demo20191222.config",
        "cn.shrmus.springboot.demo20191222.user",
        "cn.shrmus.springboot.demo20191222.product"
    }
)
*/
/*
@ComponentScans(
    value = {
        @ComponentScan(value = {"cn.shrmus.springboot.demo20191222.config"}),
        @ComponentScan(value = {"cn.shrmus.springboot.demo20191222.user"}),
        @ComponentScan(value = {"cn.shrmus.springboot.demo20191222.product"})
    }
)
*/
/*
@ImportResource(
    value = {
        "classpath:config/spring/applicationContext-MysqlDataSource.xml",
        "classpath:config/spring/applicationContext-OracleDataSource.xml"
    }
)
*/
//@SpringBootApplication(excludeName = "cn.shrmus.springboot.demo20191222.autoconfigure.MysqlDataSourceAutoConfiguration")
//@SpringBootApplication(exclude = {cn.shrmus.springboot.demo20191222.autoconfigure.MysqlDataSourceAutoConfiguration.class})
//@Configuration(proxyBeanMethods = false)
//@EnableAutoConfiguration
@SpringBootApplication
public class Application20191222 {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application20191222.class);

        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);

        System.out.println("Spring Boot默认提供的Bean：");
        String[] beanDefinitionNames = configurableApplicationContext.getBeanDefinitionNames();
        Arrays.sort(beanDefinitionNames);
        Arrays.stream(beanDefinitionNames).forEach(System.out::println);

        MysqlDataSource bean = configurableApplicationContext.getBean(MysqlDataSource.class);
        bean.function();
        System.exit(SpringApplication.exit(configurableApplicationContext));
    }
}
