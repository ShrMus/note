**==Spring Boot (0) 初探==**
[TOC]
> 摘要

# 1. Spring Boot的诞生
在学习Spring Boot之前先提出几个问题： <br />
1. Spring Boot是什么
2. 为什么要使用Spring Boot
3. Spring Boot能做什么

在此，我找了几篇文章（鄙人没有经历过互联网项目架构的演变，只能从别人的文章中学习）： <br />
为什么会出现Spring Boot：[http://www.sohu.com/a/212136259_100090656](http://www.sohu.com/a/212136259_100090656) <br />
为什么越来越多的开发者选择使用Spring Boot？：</font>[https://blog.csdn.net/xlgen157387/article/details/52830071](https://blog.csdn.net/xlgen157387/article/details/52830071) <br />

# 2. 怎么使用Spring Boot
了解了上述几个问题之后，下一步了解怎么去使用。
[Spring Boot官方文档](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/using-spring-boot.html#using-boot)

## 2.1 环境要求
- JDK 1.8或以上
- Maven 3.2或以上

## 2.2 构建系统
官方文档中提供了几种构建方式，这里使用Maven构建。

### 2.2.1 配置spring-boot-starter-parent
继承一个父工程，POM配置：
```
<!-- Inherit defaults from Spring Boot -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.2.RELEASE</version>
</parent>
```

### 2.2.2 不配置spring-boot-starter-parent
在公司中你的项目可能已经配置了一个父工程，而那个父工程没有依赖Spring Boot，那么可以使用这种方式：
```
<dependencyManagement>
    <dependencies>
        <dependency>
            <!-- Import dependency management from Spring Boot -->
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>2.2.2.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```
如果你看```spring-boot-starter-parent```的POM文件，会发现它继承```spring-boot-dependencies```。

### 2.2.3 使用Spring Boot Maven插件
Spring Boot包含一个Maven插件，它可以将项目打包为一个可执行jar。如果要使用插件，请将其添加到&lt;plugins&gt;部分，如下例所示：
```
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

### 2.2.4 Starters
Starters是一个方便包含在应用程序中的依赖描述符。
你可以一次性获得所需的所有Spring和相关技术。例如你要构建一个Web应用程序，在你的工程POM文件中加入```spring-boot-starter-web```依赖。
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
它包含许多依赖项，把所有你需要依赖的包都包含了，看看```spring-boot-starter-web```的POM文件里面就可以找到它依赖了```spring-web```和```spring-webmvc```等。

所有官方的starters都类似```spring-boot-starter-*```这种方式命名。[官方文档](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/using-spring-boot.html#using-boot-starter)中列出了常用的。

# 3. 代码结构
本地代码结构如下：

![](https://thumbnail0.baidupcs.com/thumbnail/96f87b76a9c944dc3c590325360df000?fid=3226273652-250528-617198325292244&time=1577098800&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-6MdORFR6%2F9eXqZeDDiqT2JqhxUY%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=8275778650118325066&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video)

在```Application.java```文件中声明```main```方法，加上```@SpringBootApplication```注解，如下：
```Java
package cn.shrmus.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

# 4. 配置类
Spring支持基于java的配置，虽然```SpringApplication```可以和XML一起使用，但是建议主源类是一个```@Configuration```类，在主源类中加载其他的配置。通常，定义```main```方法的类可以作为主源类。 <br />

实际上面的```Application```类就是一个```@Configuration```类，因为```@SpringBootApplication```注解基于```@Configuration```注解。

<span id="4.1"></span>
## 4.1 导入其他配置类
不建议把所有配置都放在一个类中，就像XML配置不建议把配置信息都存在一个文件中，可以用```@Import```注解导入其他配置类，或者可以使用```@ComponentScan```来自动获取所有Spring组件，包括标记了```@Configuration```的类，也可以说是```@Component```类。

### 4.1.1 @Import
用法如下：
```
@Import(
    value = {
        cn.shrmus.springboot.config.MybatisMysqlDataSource.class, 
        cn.shrmus.springboot.config.MybatisOracleDataSource.class
    }
)
```
前提是```value```中的类都是标记了```@Configuration```的类。

### 4.1.2 @ComponentScan
用法如下：
```
@ComponentScan(
    value = {
        "cn.shrmus.springboot.config",
        "cn.shrmus.springboot.user",
        "cn.shrmus.springboot.product"
    }
)
```
或者
```
@ComponentScan(
    basePackages = {
        "cn.shrmus.springboot.config",
        "cn.shrmus.springboot.user",
        "cn.shrmus.springboot.product"
    }
)
```
上面的```value```和```basePackages```等价。```@ComponentScan```还有其他的用法，就需要读者自己去摸索了。

### 4.1.3 @ComponentScans
可以声明多个```@ComponentScan```，用法如下：

```
@ComponentScans(
    value = {
        @ComponentScan(value = {"cn.shrmus.springboot.config"}),
        @ComponentScan(value = {"cn.shrmus.springboot.user"}),
        @ComponentScan(value = {"cn.shrmus.springboot.product"})
    }
)
```

另外还有```@ConfigurationPropertiesScan```, ```@EntityScan```,读者可以自己尝试怎么使用。

### 4.1.4 @SpringBootApplication
我在测试上面3个注解的时候，发现一个事情，我把这3个注解都去掉之后，这些类（```@Configuration```类或者说是```@Component```类）还是会获取到，那到底是什么原因，我到网上查了一下，说是带[@SpringBootApplication]()


## 4.2 导入XML配置
如果必须使用基于XML的配置，建议从标记了```@Configuration```的类开始，再使用```@ImportResource```注解加载XML配置文件。 <br />

### 4.2.1 删除注解
把```cn.shrmus.springboot.config.MybatisMysqlDataSource```类和```cn.shrmus.springboot.config.MybatisOracleDataSource```的```@Configuration```注解去掉，确保XML配置有效。

### 4.2.2 新建XML配置文件
在==resources==文件夹中新建两个spring配置文件，主要为了表示```@Configuration```注解值是个数组类型。

==applicationContext-mybatisMysqlDataSource.xml==文件：
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="mybatisMysqlDataSource" class="cn.shrmus.springboot.config.MybatisMysqlDataSource"></bean>
</beans>
```

==applicationContext-mybatisOracleDataSource.xml==文件：
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="mybatisOracleDataSource" class="cn.shrmus.springboot.config.MybatisOracleDataSource"></bean>
</beans>
```

### 4.2.1 @ImportResource
用```@ImportResource```注解导入XML配置文件，用法如下：
```
@ImportResource(
    value = {
        "classpath:config/spring/applicationContext-mybatisMysqlDataSource.xml.xml",
        "classpath:config/spring/applicationContext-mybatisOracleDataSource.xml.xml"
    }
)
```

# 5. 自动配置
Spring Boot自动配置是基于你添加的jar依赖尝试去做自动配置。

你可以选择```@EnableAutoConfiguration```或```@SpringBootApplication```注解加入到```@Configuration```类中。

## 5.1 替换自动配置
你可以定义自己的配置来替换自动配置的特定部分。
在```classpath```中下新建一个==META-INF==目录，在这个目录下新建一个```spring.factories```文件。
```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
cn.shrmus.springboot.autoconfigure.MybatisMysqlDataSourceAutoConfiguration
```

如果你需要了解当前应用的是什么自动配置，以及为什么，请使用```--debug```开关启动您的应用程序。这样做可以为选择的核心日志记录器启用调试日志，并将条件报告记录到控制台。

## 5.2 禁用特定的自动配置类
如果你发现你不想要的特定的自动配置类被应用，你可以使用```@EnableAutoConfiguration```的```exclude```属性来禁用它们，如下面的例子所示：
```
package cn.shrmus.springboot.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class MybatisMysqlDataSource {
}
```

如果要禁用的类不在```classpath```中，也可以使用```excludeName```属性指定类的全限定名，用法如下：
```
@SpringBootApplication(excludeName = "org.springframework.boot.autoconfigure.aop.AopAutoConfiguration")
```

还可以使用```spring.autoconfigure.exclude```来控制要排除的自动配置类列表。用法如下：
