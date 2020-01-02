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
```xml
<!-- Inherit defaults from Spring Boot -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.2.RELEASE</version>
</parent>
```

### 2.2.2 不配置spring-boot-starter-parent

在公司中你的项目可能已经配置了一个父工程，而那个父工程没有依赖Spring Boot，那么可以使用这种方式：
```xml
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
```xml
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
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
它包含许多依赖项，把所有你需要依赖的包都包含了，看看```spring-boot-starter-web```的POM文件里面就可以找到它依赖了```spring-web```和```spring-webmvc```等。

所有官方的starters都类似```spring-boot-starter-*```这种方式命名。[官方文档](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/using-spring-boot.html#using-boot-starter)中列出了常用的。

# 3. 代码结构
本地代码结构如下：
```properties
cn
 +- shrmus
     +- springboot
         +- demo20191222
             +- Application20191222.java
             |
             +- product
             |   +- Product.java
             |   +- ProductController.java
             |   +- ProductService.java
             |   +- ProductRepository.java
             |
             +- user
                 +- User.java
                 +- UserController.java
                 +- UserService.java
                 +- UserRepository.java
```

在```Application20191222.java```文件中声明```main```方法，加上```@SpringBootApplication```注解，如下：
```java
package cn.shrmus.springboot.demo20191222;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application20191222 {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

# 4. 配置类

Spring支持基于java的配置，虽然```SpringApplication```可以和XML一起使用，但是建议主源类是一个```@Configuration```类，在主源类中加载其他的配置。通常，定义```main```方法的类可以作为主源类。 <br />

实际上面的```Application20191222```类就是一个```@Configuration```类，因为```@SpringBootApplication```注解基于```@Configuration```注解。

<span id="4.1"></span>

## 4.1 导入其他配置类
不建议把所有配置都放在一个类中，就像XML配置不建议把配置信息都存在一个文件中，可以用```@Import```注解导入其他配置类，或者可以使用```@ComponentScan```来自动获取所有Spring组件，包括标记了```@Configuration```的类，也可以说是```@Component```类。

### 4.1.1 @Import
用法如下：
```java
@Import(
    value = {
        cn.shrmus.springboot.demo20191222.config.MysqlDataSource.class, 
        cn.shrmus.springboot.demo20191222.config.OracleDataSource.class
    }
)
```
前提是```value```中的类都是标记了```@Configuration```的类。

### 4.1.2 @ComponentScan

用法如下：
```java
@ComponentScan(
    value = {
        "cn.shrmus.springboot.demo20191222.config",
        "cn.shrmus.springboot.demo20191222.user",
        "cn.shrmus.springboot.demo20191222.product"
    }
)
```
或者
```java
@ComponentScan(
    basePackages = {
        "cn.shrmus.springboot.demo20191222.config",
        "cn.shrmus.springboot.demo20191222.user",
        "cn.shrmus.springboot.demo20191222.product"
    }
)
```
上面的```value```和```basePackages```等价。```@ComponentScan```还有其他的用法，就需要读者自己去摸索了。

### 4.1.3 @ComponentScans
可以声明多个```@ComponentScan```，用法如下：

```java
@ComponentScans(
    value = {
        @ComponentScan(value = {"cn.shrmus.springboot.demo20191222.config"}),
        @ComponentScan(value = {"cn.shrmus.springboot.demo20191222.user"}),
        @ComponentScan(value = {"cn.shrmus.springboot.demo20191222.product"})
    }
)
```

### 4.1.4 @ConfigurationPropertiesScan


另外还有```@ConfigurationPropertiesScan```，```@EnableConfigurationProperties```，```@ConfigurationPropertiesBinding```，```@EntityScan```，```@ImportAutoConfiguration```读者可以自己尝试怎么使用。



## 4.2 导入XML文件
如果必须使用基于XML的配置，建议从标记了```@Configuration```的类开始，再使用```@ImportResource```注解加载XML配置文件，用法如下：
```java
@ImportResource(
    value = {
        "classpath:config/spring/applicationContext-MysqlDataSource.xml",
        "classpath:config/spring/applicationContext-OracleDataSource.xml"
    }
)
```

## 4.3 导入properties文件

如果必须使用properties文件，而这个文件又是自定义的，可以用```@PropertySource```注解，用法如下：
```java
@PropertySource(value = "config/dataSource/mysqlDataSource.properties")
```
如果配置信息在```application.properties```中，则可以省略此注解。

# 5. 自动配置

Spring Boot自动配置是基于你添加的jar依赖尝试去做自动配置。

你可以选择```@EnableAutoConfiguration```或```@SpringBootApplication```注解加入到```@Configuration```类中。

## 5.1 替换自动配置
你可以定义自己的配置来替换自动配置的特定部分。
在```classpath```中下新建一个==META-INF==目录，在这个目录下新建一个```spring.factories```文件。
```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
cn.shrmus.springboot.demo20191222.autoconfigure.MysqlDataSourceAutoConfiguration
```

如果你需要了解当前应用的是什么自动配置，以及为什么，请使用```--debug```开关启动您的应用程序。这样做可以为选择的核心日志记录器启用调试日志，并将条件报告记录到控制台。

**启动```--debug```的配置方式**： <br />
菜单[**Run**]-->[**Edit Configurations**] <br />
[**Configuration**]选项卡-->展开[**Environment**] <br />
在[**Program arguments**]填入```--debug``` <br />

## 5.2 禁用特定的自动配置类
如果你发现你不想要的特定的自动配置类被应用，你可以使用```@EnableAutoConfiguration```的```exclude```属性来禁用它们，如下面的例子所示：
```java
package cn.shrmus.springboot.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {cn.shrmus.springboot.demo20191222.autoconfigure.MysqlDataSourceAutoConfiguration.class})
public class MysqlDataSource {
}
```

如果要禁用的类不在```classpath```中，也可以使用```excludeName```属性指定类的全限定名，用法如下：
```java
@SpringBootApplication(excludeName = "cn.shrmus.springboot.demo20191222.autoconfigure.MysqlDataSourceAutoConfiguration")
```

还可以使用```spring.autoconfigure.exclude```来控制要排除的自动配置类列表。在```application.properes```文件中，用法如下：
```properties
spring.autoconfigure.exclude=cn.shrmus.springboot.demo20191222.autoconfigure.MysqlDataSourceAutoConfiguration
```

# 6. Spring Bean和依赖注入

所有应用程序组件（```@Component```、```@Service```、```@Repository```、```@Controller```等)都自动注册为Spring bean。

下面用构造方法注入来获得一个UserRepository Bean：
```java
package cn.shrmus.springboot.demo20191222.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{
    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```
如果bean只有一个构造方法，可以省略```@Autowired```。

# 7. 使用@SpringBootApplication注解
使用```@SpringBootApplication```注解可以启用这三个特性：
- ```@EnableAutoConfiguration```：启用Spring Boot的自动配置机制。
- ```@ComponentScan```：开启扫描，在应用程序的包中扫描```@Component```类。
- ```@Configuration```：允许在上下文中注册额外的bean或导入额外的配置类。


如果你不想在应用程序中使用```@Component```扫描或```@ConfigurationProperties```扫描：
```java
package cn.shrmus.springboot.demo20191222;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class Application20191222 {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
这时候会发现，==user==包和==product==包中的```@Component```类都没有注入到容器中。

# 8. 运行应用程序

本节只讨论基于jar的打包。如果选择将应用程序打包为war文件，应该参考服务器和IDE文档。

**打包方案一：** <br />
打开[**File**]菜单-->[**Project Structure**]，弹出[**Project Structure**]对话框 <br />
在对话框左侧选择[**Project Settings**]中的[**Artifacts**] <br />
点击[**+**]-->[**Jar**]-->[**From modules with dependencies**]，弹出[**Create JAR from Modules**]对话框 <br />
在对话框中选择[**Module**]和[**Main Class**]，然后[**Directory for META-INF/MANIFEST.SF**]从不可选变成可选状态，这个选项中的路径是创建```MANIFEST.SF```文件的路径 <br />
点击OK之后项目会在[**Directory for META-INF/MANIFEST.SF**]项的路径下创建==META-INF==目录，在此目录下创建```MANIFEST.SF```文件，而文件中的内容就是启动类的配置信息呵呵版本信息 <br />
回到[**Project Structure**]对话框，多了一项刚刚创建的信息，[**Output derectory**]是JAR的输出路径，点击OK <br />
点击菜单[**Build**]-->[**Build Artifacts**]，选择[**Build**]，至此，JAR就打好了。 <br />

**打包方案二：** <br />
如果是继承```spring-boot-starter-parent```，只需在POM中添加
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

**打包方案三：** <br />
如果不是继承```spring-boot-starter-parent```而是使用依赖管理```spring-boot-dependencies```，则添加：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <!-- 启动类的全限定名 -->
                <mainClass>cn.shrmus.springboot.demo20191222.Application20191222</mainClass>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## 8.1 运行打包的应用程序
创建jar后，你可以使用```java -jar```来运行您的应用程序，如下面的例子所示:
```shell
java -jar target/demo20191222-springboot-1.0-SNAPSHOT.jar
```

还可以在启用远程调试支持的情况下运行打包的应用程序。这样做可以将调试器附加到打包的应用程序中，如下面的示例所示：
```shell
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar target/demo20191222-springboot-1.0-SNAPSHOT.jar
```

## 8.2 使用Maven插件