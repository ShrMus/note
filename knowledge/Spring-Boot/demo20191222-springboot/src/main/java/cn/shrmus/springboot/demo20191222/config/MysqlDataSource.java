package cn.shrmus.springboot.demo20191222.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@EnableAutoConfiguration(exclude = cn.shrmus.springboot.demo20191222.autoconfigure.MysqlDataSourceAutoConfiguration.class)
@Configuration
@EnableConfigurationProperties(value = MysqlDataSourceProperties.class)
public class MysqlDataSource {
    private final MysqlDataSourceProperties mysqlDataSourceProperties;

    @Autowired
    public MysqlDataSource(MysqlDataSourceProperties mysqlDataSourceProperties) {
        this.mysqlDataSourceProperties = mysqlDataSourceProperties;
    }

    public void function() {
        System.out.println(mysqlDataSourceProperties);
    }

}
