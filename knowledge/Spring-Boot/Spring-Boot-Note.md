**==Spring Boot (0) 初探==**
[TOC]
> 摘要

# 1. Spring Boot的诞生
在学习Spring Boot之前先提出几个问题： <br />
1. Spring Boot是什么
2. 为什么要使用Spring Boot

在此，我找了几篇文章（鄙人没有经历过互联网架构的演变，只能从别人的文章中学习）： <br />
为什么会出现Spring Boot：[http://www.sohu.com/a/212136259_100090656](http://www.sohu.com/a/212136259_100090656) <br />
为什么越来越多的开发者选择使用Spring Boot？：</font>[https://blog.csdn.net/xlgen157387/article/details/52830071](https://blog.csdn.net/xlgen157387/article/details/52830071) <br />

# 2. 怎么使用Spring Boot
知道Spring Boot是什么以及为什么要使用之后，下一步了解怎么去使用。
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

![](https://github.com/ShrMus/note/blob/master/knowledge/Spring-Boot/pictures/Spring-Boot-local-code-struct.png)

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
Spring支持基于java的配置，虽然```SpringApplication```可以和XML一起使用，但是建议主源类是一个```@Configuration```类，在主源类中加载其他的配置。通常，定义主方法的类可以作为主源类。

## 4.1 导入其他配置类
- [ ] 不建议把所有配置都放在一个类中，就像XML配置不建议把配置信息都存在一个文件中，可以用```@Import```注解导入其他配置类，或者可以使用```@ComponentScan```来自动获取所有Spring组件，包括标记了```@Configuration```的类。


## 4.2 导入XML配置
如果必须使用基于XML的配置，建议从标记了```@Configuration```的类开始，再使用```@ImportResource```注解加载XML配置文件。

# 5. 自动配置

