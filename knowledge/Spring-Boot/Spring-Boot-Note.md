**==Spring Boot (0) 初探==**

[TOC]

> 摘要

# 1. Spring Boot的诞生

在学习Spring Boot之前先提出几个问题： <br />

1. Spring Boot是什么 <br />
2. 为什么要使用Spring Boot <br />
3. Spring Boot能做什么 <br />

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
1. 菜单[**Run**]-->[**Edit Configurations**] <br />
2. [**Configuration**]选项卡-->展开[**Environment**] <br />
3. 在[**Program arguments**]填入```--debug``` <br />

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

## 8.1 打包应用程序

**打包方案一：** <br />
1. 打开[**File**]菜单-->[**Project Structure**]，弹出[**Project Structure**]对话框 <br />
2. 在对话框左侧选择[**Project Settings**]中的[**Artifacts**] <br />
3. 点击[**+**]-->[**Jar**]-->[**From modules with dependencies**]，弹出[**Create JAR from Modules**]对话框 <br />
4. 在对话框中选择[**Module**]和[**Main Class**]，然后[**Directory for META-INF/MANIFEST.SF**]从不可选变成可选状态，这个选项中的路径是创建```MANIFEST.SF```文件的路径 <br />
5. 点击OK之后项目会在[**Directory for META-INF/MANIFEST.SF**]项的路径下创建==META-INF==目录，在此目录下创建```MANIFEST.SF```文件，而文件中的内容就是启动类的配置信息呵呵版本信息 <br />
6. 回到[**Project Structure**]对话框，多了一项刚刚创建的信息，[**Output derectory**]是JAR的输出路径，点击OK <br />
7. 点击菜单[**Build**]-->[**Build Artifacts**]，选择[**Build**]，至此，JAR就打好了。 <br />


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

## 8.2 运行打包的应用程序

创建jar后，你可以使用```java -jar```来运行您的应用程序，如下面的例子所示:
```shell
java -jar target/demo20191222-springboot-1.0-SNAPSHOT.jar
```

还可以在启用远程调试支持的情况下运行打包的应用程序。这样做可以将调试器附加到打包的应用程序中，如下面的示例所示：
```shell
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar target/demo20191222-springboot-1.0-SNAPSHOT.jar
```

## 8.3 使用Maven插件

### 8.3.1 用Maven命令启动应用程序
```shell
mvn spring-boot:run
```

IDEA开发工具中用Maven命令运行应用程序的方式：

**方式一：** <br />
1. 在[**Maven**]窗口中点击[**Execute Maven Goal**]按钮，按钮图标是个M字母的形状 <br />
2. 在弹出的[**Execute Maven Goal**]对话框中，[**Working directory**]选择要运行的应用程序目录，[**Command line**]中输入```spring-boot:run``` <br />

**方式二：**
1. 在[**Maven**]窗口中展开要运行的应用程序，选择[**Plugins**]-->[**spring-boot**]-->[**spring-boot:run**] <br />
2. 双击[**spring-boot:run**] <br />

**方式三：**
1. 菜单[**Run**]-->[**Edit Configurations**] <br />
2. 在[**Run/Debug Configurations**]对话框中，选择左侧的[**+**]-->[**Add New Configuration**]-->[**Maven**] <br />
3. 输入[**Name**]，在[**Parameters**]选项卡中，[**Working directory**]选择要运行的应用程序目录，[**Command line**]中输入```spring-boot:run``` <br />
4. 在工具栏[**运行**]按钮左侧选择刚刚配置的Maven Configuratiion，点击[**运行**]按钮 <br />

### 8.3.2 使用MAVEN_OPTS操作系统环境变量

```shell
export MAVEN_OPTS=-Xmx1024m
```

# 9. 热部署

在程序运行过程中交换字节码，可使用JRebel。

使用```spring-boot-devtools```模块支持快速重启应用程序。

# 10. 开发工具

添加```spring-boot-devtools```模块可以使应用程序开发体验更愉快。

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

在运行完全打包的应用程序时，将自动禁用开发人员工具。如果您的应用程序是从```java -jar```启动的，或者是从一个特殊的类加载器启动的，那么它就被认为是一个“生产应用程序”，如果您在容器中运行应用程序，可以排除devtools或设置```spring.devtools.restart.enabled=false```。

默认情况下，重新打包的归档文件不包含devtools。如果您想使用某个远程devtools特性，您需要禁用```excludeDevtools```构建属性来包含它。该属性同时受到Maven和Gradle插件的支持。

## 10.1 属性默认值

Spring Boot支持的几个库使用缓存来提高性能。例如，模板引擎缓存已编译的模板，以避免重复解析模板文件。此外，在提供静态资源时，Spring MVC可以向响应添加HTTP缓存标头。

虽然缓存在生产环境中非常有用，但在开发过程中可能会适得其反，使您无法看到刚才在应用程序中所做的更改。出于这个原因，spring-boot-devtools默认禁用缓存选项。

缓存选项通常由```application.properties```文件设置。例如，Thymeleaf提供了```spring.thymeleaf.cache```属性，不需要手动设置这些属性，```spring-boot-devtools```模块自动应用合理的开发时配置。

由于在开发Spring MVC和Spring WebFlux应用程序时需要更多关于web请求的信息，所以开发人员工具将为web日志组启用```DEBUG```日志。这将为您提供有关传入请求、正在处理它的处理程序、响应结果等信息。如果希望记录所有请求细节(包括潜在的敏感信息)，可以打开```spring.http.log-request-details```配置属性。

如果不希望应用属性默认值，可以将```application.properties```中的```spring.devtools.add-properties```设置为```false```。

## 10.2 自动重启

使用```spring-boot-devtools```的应用程序会使在==classpath==中的文件发生更改时自动重新启动。默认情况下，==classpath==中指向文件夹的任何条目都将被监视，以查看是否有更改。某些资源（如静态资产和视图模板）不需要重新启动应用程序。

<span id="10.2_自动重启"></span>
**触发重启** <br />
由于DevTools监视类路径资源，触发重新启动的惟一方法是更新类路径。更新类路径的方式取决于所使用的IDE。 <br />

在Eclipse中，保存修改后的文件将导致更新类路径并触发重启。 <br />
在IntelliJ IDEA中，构建项目（菜单[**Build**]-->[**Build Project**]）具有相同的效果。

与[LiveReload](#10.3_LiveReload)一起使用时，自动重启效果非常好。如果使用JRebel，则禁用自动重新启动，以便动态类重新加载。其他devtools特性(如LiveReload和属性覆盖)仍然可以使用。

DevTools在重启时依赖于应用程序上下文的shutdown hook去关闭。如果禁用了shutdown hook（```SpringApplication.setRegisterShutdownHook(false)```）它将无法正常工作。

当==classpath==上的一个条目更改时，决定是否应该触发重启时，DevTools会自动忽略名为```spring-boot```、```spring-boot- DevTools```、```spring-boot-autoconfigure```、```spring-boot-actuator```和```spring-boot-starter```的项目。

DevTools需要使用```ApplicationContext```自定义```ResourceLoader```。如果您的应用程序已经提供了一个，那么它将被包装。```ApplicationContext```不支持直接覆盖```getResource```方法。

<span id="10.2_重新启动和重新加载"></span>
**重新启动和重新加载** <br />
重启技术来自于Spring Boot提供的两个类加载器。 <br />

基类加载器（base classloader）：不更改的类（例如，来自第三方jar的类）将加载到base类加载器中。 <br />
重启类加载器（restart classloader）：你正在开发的类被加载到restart加载器中。 <br />

当应用程序重新启动时，restart类加载器被丢弃并且创建一个新的restart类加载器。这种方法意味着应用程序重新启动通常比“冷启动”快得多，因为基类加载器已经可用并被填充了。

如果你发现重新启动对于您的应用程序来说不够快，或者你遇到了类加载问题，那么你可以考虑重新加载技术，例如来自ZeroTurnaround的JRebel。这些工作是通过在装入类时重写它们，使它们更易于重新装入。

### 10.2.1 记录条件评估中的变化

默认情况下，每次应用程序重新启动时，都会记录一个显示条件评估增量的报告。该报告显示了在进行更改(如添加或删除bean和设置配置属性)时对应用程序自动配置的更改。

若要禁用报告的日志记录，请设置以下属性：
```properties
spring.devtools.restart.log-condition-evaluation-delta=false
```

### 10.2.2 不包含资源

某些资源在更改时不一定需要重新启动。例如Thymeleaf模板可以直接编辑。 <br />
默认情况下，在```/META-INF/maven```、```/META-INF/resources```、```/resources```、```/static```、```/public```或```/templates```中更改资源不会触发重新启动，但会触发动态重新加载。 <br />
如果您想要自定义这些排除，您可以使用```spring.devtools.restart.exclude```属性。 <br />
例如，要排除```/static```和```/public```，您需要设置以下属性:
```properties
spring.devtools.restart.exclude=static/**,public/**
```
如果您想要保留这些默认值并添加额外的排除，那么可以使用```spring.devtools.restart.additional-exclude```属性。

### 10.2.3 监视其他路径

当你更改不在==classpath==中的文件时，你希望重新启动或重新加载应用程序。 <br/>
使用```spring.devtools.restart.additional-paths```属性来配置额外的路径以观察变化。 <br/>
可以使用```spring.devtools.restart.exclude```属性来控制其他路径下的更改是触发完全重新启动还是实时重新加载。

### 10.2.4 禁用重新启动

如果不想使用重启特性，可以使用```spring.devtools.restart.enabled```属性禁用。 <br/>
在大多数情况下，可以在```application.properties```中设置这个属性。 <br/>
这样做仍然会初始化restart类加载器，但它不会监视文件更改。

如果你需要完全禁用重启，就要在调用```SpringApplication.run(…)```之前将```spring.devtools.restart.enabled```系统属性设置为```false```。如下所示：

```java
public static void main(String[] args) {
    System.setProperty("spring.devtools.restart.enabled", "false");
    SpringApplication.run(MyApp.class, args);
}
```

### 10.2.5 使用触发器文件

如果你使用的IDE不断编译修改的文件，而你可能更希望只在特定的时间触发重新启动。 <br/>
你可以使用“触发器文件”，它是一个特殊的文件，当您想要触发重启检查时，必须修改它(对文件的任何更新都将触发一次检查，但是只有在Devtools检测到需要执行某些操作时，才会实际重新启动)。<br/>

要使用触发器文件，请将```spring.devtools.restart.trigger-file```属性设置为触发器文件的名称（不包括任何路径），触发器文件必须在==classpath==下。<br/>

例如，如果你有一个具有以下结构的项目：

```properties
src
+- main
   +- resources
      +- .reloadtrigger
```
那么```trigger-file```属性设置成：

```properties
spring.devtools.restart.trigger-file=.reloadtrigger
```
现在只有```src/main/resources/.reloadtrigger```被修改才会触发重启。

如果想要把```spring.devtools.restart.trigger-file```设置为全局设置，以便所有项目都以相同的方式运行。
有些IDE可以使你不用手动更新触发器文件，使用Spring工具，您可以从控制台视图中使用“reload”按钮(只要您的触发文件名为.reloadtrigger)。

### 10.2.6 自定义restart类加载器

正如[重新启动和重新加载](#10.2_重新启动和重新加载)一节中所描述的，重新启动功能是通过使用两个类加载器实现的。<br/>
对于大多数应用程序，这种方法工作得很好。然而，它有时会导致类加载问题。<br/>

默认情况下，IDE中任何打开的项目都是用“restart”类加载器加载的，而任何正规的```.jar```文件都是用“base”类加载器加载的。<br/>

如果您处理的是一个多模块项目，然而并不是每个模块都要导入到IDE中，那么您可能需要自定义一些东西。你可以创建一个```META-INF/spring-devtools.properties```文件。<br/>

> The ```spring-devtools.properties``` file can contain properties prefixed with ```restart.exclude``` and ```restart.include```. The ```include``` elements are items that should be pulled up into the “restart” classloader, and the ```exclude``` elements are items that should be pushed down into the “base” classloader. 

这个```spring-devtools.properties```文件可以包含```restart.exclude```和```restart.include```前缀。
<br/>```include```是一个数组，数组中的元素被“restart”类加载器加载。
<br/>```exclude```也是一个数组，数组中的元素被“base”类加载器加载。<br/>
（原文中的==pulled up==和==pushed down==一直没能理解是什么意思，经过讨论后，只有关注```include```和```exclude```中的元素被谁来执行，而没有去特意解释这两个短语的意思）<br/>
数组元素的值是一个被应用到==classpath==中的正则表达式，如下所示：
```properties
restart.exclude.companycommonlibs=/mycorp-common-[\\w\\d-\.]+\.jar
restart.include.projectcommon=/mycorp-myproj-[\\w\\d-\.]+\.jar
```
所有属性的key必须是唯一的，只要以```restart.include```或```restart.exclude```开头，它就被考虑在内。

==classpath==下所有的```META-INF/spring-devtools.properties```都会被加载，你可以将文件打包到项目内部或项目使用的库中。

### 10.2.7 已知局限性

使用标准ObjectInputStream反序列化的对象，重新启动功能不能很好地工作。<br/>
如果需要反序列化数据，可能需要结合使用Spring的```ConfigurableObjectInputStream```和```Thread.currentThread().getcontextclassloader()```。<br/>

不幸的是，一些第三方库在反序列化时没有考虑上下文类加载器。如果您发现这样的问题，您需要向原始作者请求修复。

<span id="10.3_LiveReload"></span>
## 10.3 LiveReload

模块```spring-boot-devtools```包含一个内嵌的LiveReload服务器，可以用来在资源更改时触发浏览器刷新。<br/>
从[livereload.com](http://livereload.com/extensions/)可以免费获得Chrome、Firefox和Safari的LiveReload浏览器扩展。

如果您不想在应用程序运行时启动LiveReload服务器，将```spring.devtools.livereload.enabled```属性设置为```false```。

一次只能运行一个LiveReload服务器。在启动应用程序之前，确保没有其他的LiveReload服务器在运行。如果您从IDE启动多个应用程序，那么只有第一个具有LiveReload支持。

## 10.4 全局设置
你可以通过向```$HOME/.config/spring-boot```文件夹下添加以下任何文件来配置全局devtools设置：
1. ```spring-boot-devtools.properties```
2. ```spring-boot-devtools.yaml```
3. ```spring-boot-devtools.yml```

任何被添加到这些文件中的属性都适用于所有使用devtools的Spring启动应用程序。<br/>
例如，配置一直使用触发器文件去重启，你需要添加下列属性：<br/>
**~/.config/spring-boot/spring-boot-devtools.properties**

```properties
spring.devtools.restart.trigger-file=.reloadtrigger
```

如果```$HOME/.config/spring-boot```中没有找到devtools配置文件，在```$HOME```文件夹的根目录搜索是否存在```.spring-boot-devtools.properties```文件。<br/>
这允许你与不支持```$HOME/.config/spring-boot```位置的旧版Spring Boot应用程序共享devtools全局配置。

在上述文件中激活的配置文件不会影响加载[特定的配置文件](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/spring-boot-features.html#boot-features-external-config-profile-specific-properties)

## 10.5 远程应用程序
远程代码更新的意思是，在本地IDE修改代码，可以自动更新到服务器上，并且自动重启生效。就像在本地开发环境一样。(来自[Spring Boot Remote Application](https://blog.csdn.net/quqtalk/article/details/84877102))

Spring Boot developer tools并不局限于本地开发。在运行远程应用程序时，你还可以使用一些特性。<br/>
远程支持是可选的，因为启用它可能存在安全风险。仅当在受信任的网络上运行或使用SSL进行保护时，才应该启用它。如果这两个选项都不可用，就不应该在生产环境上使用DevTools的远程支持。<br/>
要启用它，您需要确保重新打包的归档文件中包含```devtools```，如下所示：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <excludeDevtools>false</excludeDevtools>
            </configuration>
        </plugin>
    </plugins>
</build>
```
然后设置```spring.devtools.remote.secret```属性，与任何重要的密码或秘密一样，该值应该是惟一的、强的，这样就不会被猜测或强行使用。

远程devtools支持由两部分支持：
等待连接的服务端结点和在你的IDE中运行的客户端应用程序。<br/>
当设置```spring.devtools.remote.secret```属性时，服务器组件将自动启用。客户端组件必须手动启动。

### 10.5.1 运行远程客户端应用程序
远程客户端应用程序需要与你连接的远程应用程序具有相同的==classpath==。在==classpath==中运行```org.springframework.boot.devtools.RemoteSpringApplication```。应用程序唯一需要的参数是它所连接的远程URL。

如果你使用**Eclipse**或者**STS**，你有一个名为```my-app```的项目已经部署到Cloud Foundry云平台上，你还需要做下列事情：
- 菜单[**Run**]-->[**Run Configurations…**] <br/>
- 创建一个新的Java程序启动配置 <br/>
- 浏览```my-app```项目 <br/>
- 使用```org.springframework.boot.devtools.RemoteSpringApplication```作为main class <br/>
- 添加```https://myapp.cfapps.io```（你的远程URL）到```Program arguments```

> Because the remote client is using the same classpath as the real application it can directly read application properties. 

因为远程客户端应用程序与云平台的应用程序使用相同的==classpath==，所以云平台的应用程序可以直接读取远程客户端应用程序的属性。（官方文档中的real project和it让我有点迷糊，网上的大多是机器翻译，正所谓理论来自于实践，我就去试了一下，得到了这段翻译）<br/>
这就是```spring.devtools.remote.secret```属性被读取并传到服务器端进行身份验证的方法。

通常建议使用```https://```作为连接协议，因为通信是加密的，密码不能被截获。

如果需要使用代理访问远程应用程序，配置```spring.devtools.remote.proxy.host```和```spring.devtools.remote.proxy.port```属性。

### 10.5.2 远程更新
远程客户端监视应用程序==classpath==中的更改，与监视[本地重启](#10.2_自动重启)的方式相同。<br/>
任何更新的资源都被推送到远程应用程序，并（如果需要）触发远程应用程序重新启动。<br/>
如果你在本地没有云服务的特性上进行迭代，这可能会很有帮助。通常，远程更新和重新启动比完整的重新构建和部署周期要快得多。

只有在远程客户端运行时才监控文件。如果在启动远程客户端之前更改文件，则不会将其推到远程服务器。

### 10.5.3 配置文件系统监视器
[FileSystemWatcher](https://github.com/spring-projects/spring-boot/tree/v2.2.2.RELEASE/spring-boot-project/spring-boot-devtools/src/main/java/org/springframework/boot/devtools/filewatch/FileSystemWatcher.java)的工作方式是，在一定的时间间隔内轮询类更改，然后等待预定义的静默期，以确保不再发生更改。<br/>
然后将更改上传到远程应用程序。在较慢的开发环境中，可能会发生这样的情况:安静期不够长，类中的更改可能被分成批。<br/>
上传第一批类更改后，服务器将重新启动。下一批数据不能发送到应用程序，因为服务器正在重新启动。<br/>

这通常表现在```RemoteSpringApplication```日志中关于上传一些类失败的警告，以及随后的重试。但它也可能导致应用程序代码不一致，并且在上传第一批更改后无法重新启动。

如果你经常观察这些问题，尝试将```spring.devtools.restart.poll-interval```和```spring.devtools.restart.quiet-period```参数的值增加到适合您的开发环境中：

```properties
spring.devtools.restart.poll-interval=2s
spring.devtools.restart.quiet-period=1s
```
被监视的classpath文件夹现在每2秒轮询一次，以查找更改，并保持1秒的静默期，以确保没有其他类更改。

# 11. 打包您的应用程序用于生产
可执行jar可用于生产部署。由于它们是自包含的，所以也非常适合基于云的部署。

对于其他“生产就绪”特性，如health、auditing和metric REST或JMX结点，可以考虑添加```spring-boot-actuator```。有关详细信息，请参阅[production-ready-features.html](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/production-ready-features.html#production-ready)。
























