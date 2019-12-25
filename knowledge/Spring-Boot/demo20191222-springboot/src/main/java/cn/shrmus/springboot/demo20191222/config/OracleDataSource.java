package cn.shrmus.springboot.demo20191222.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = OracleDataSourceProperties.class)
public class OracleDataSource {
    @Autowired
    private OracleDataSourceProperties oracleDataSourceProperties;
}
