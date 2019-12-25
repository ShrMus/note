package cn.shrmus.springboot.demo20191222.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "config/dataSource/oracleDataSource.properties")
@ConfigurationProperties(prefix = OracleDataSourceProperties.PREFIX, ignoreUnknownFields = false)
@Data
public class OracleDataSourceProperties {
    public final static String PREFIX = "spring.datasource";
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
