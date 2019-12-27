package cn.shrmus.springboot.demo20191222.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

//@EnableAutoConfiguration(exclude = cn.shrmus.springboot.demo20191222.autoconfigure.MysqlDataSourceAutoConfiguration.class)
//@EnableConfigurationProperties(value = MysqlDataSourceProperties.class)
@Configuration
@PropertySource(value = "config/dataSource/mysqlDataSource.properties")
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class MysqlDataSource {

//    private final MysqlDataSourceProperties mysqlDataSourceProperties;

    private String driverClassName;
    private String url;
    private String username;
    private String password;
//    @Autowired
//    public MysqlDataSource(MysqlDataSourceProperties mysqlDataSourceProperties) {
//        this.mysqlDataSourceProperties = mysqlDataSourceProperties;
//    }

    public void function() {
//        System.out.println(mysqlDataSourceProperties);
        System.out.println("driverClassName=" + driverClassName);
        System.out.println("url=" + url);
        System.out.println("username=" + driverClassName);
        System.out.println("password=" + password);
    }
}
