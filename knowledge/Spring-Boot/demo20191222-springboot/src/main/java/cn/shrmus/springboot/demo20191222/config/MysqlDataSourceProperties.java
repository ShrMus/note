package cn.shrmus.springboot.demo20191222.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

//@Configuration
@PropertySource(value = "config/dataSource/mysqlDataSource.properties")
@ConfigurationProperties(prefix = MysqlDataSourceProperties.PREFIX)
@Data
public class MysqlDataSourceProperties {
    public final static String PREFIX = "spring.datasource";
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
