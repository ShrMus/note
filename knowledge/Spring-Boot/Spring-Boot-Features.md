
[TOC]

> 摘要

# 1.Spring应用程序
The ```SpringApplication``` 类提供了一种方便的方法来引导从```main()```方法启动的Spring应用程序。你可以委托给```SpringApplication.run```方法，如下所示：
```java
public static void main(String[] args) {
    SpringApplication.run(Application20200106.class, args);
}
```

应用程序启动，你应该看到类似以下输出：
```properties
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::   v2.2.2.RELEASE

2019-04-31 13:09:54.117  INFO 56603 --- [           main] o.s.b.s.app.SampleApplication            : Starting SampleApplication v0.1.0 on mycomputer with PID 56603 (/apps/myapp.jar started by pwebb)
2019-04-31 13:09:54.166  INFO 56603 --- [           main] ationConfigServletWebServerApplicationContext : Refreshing org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@6e5a8246: startup date [Wed Jul 31 00:08:16 PDT 2013]; root of context hierarchy
2019-04-01 13:09:56.912  INFO 41370 --- [           main] .t.TomcatServletWebServerFactory : Server initialized with port: 8080
2019-04-01 13:09:57.501  INFO 41370 --- [           main] o.s.b.s.app.SampleApplication            : Started SampleApplication in 2.992 seconds (JVM running for 3.658)
```

默认情况下，会显示```INFO```日志消息，包括一些相关的启动细节，比如启动应用程序的用户。<br/>
如果你需要除了```INFO```级别的日志信息，你可以设置[日志级别]()。<br/>
应用程序版本是从主程序类包中的实现版本确定的。<br/>
设置```spring.main.log-startup-info```为```false```可以关闭启动日志信息记录。<br/>
这还将关闭应用程序活动配置文件的日志记录。<br/>

要在启动期间添加额外的日志记录，您可以在```SpringApplication```的子类中重写```logStartupInfo(boolean)```。

## 1.1 启动失败
如果您的应用程序启动失败，注册的```FailureAnalyzers ```将有机会提供专用的错误消息和修复问题的具体操作。<br/>
例如，如果您在端口8080上启动一个web应用程序，并且该端口已经在使用，您应该看到类似于以下消息：
```properties
***************************
APPLICATION FAILED TO START
***************************

Description:

Embedded servlet container failed to start. Port 8080 was already in use.

Action:

Identify and stop the process that's listening on port 8080 or configure this application to listen on another port.
```

Spring Boot提供了很多```FailureAnalyzers ```实现，你也可以[创建自定义的故障分析器](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/howto.html#howto-failure-analyzer)

如果没有故障分析器能处理异常，你也可以显示完整的条件报告，以便更好地理解出错的原因。您需要为```org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener```[启用```debug```属性](#2._外部化配置)或[启用```DEBUG```日志记录]()。

例如，如果您使用```java -jar```运行您的应用程序，您可以启用```debug```属性，如下所示：
```shell
java -jar myproject-0.0.1-SNAPSHOT.jar --debug
```

## 1.2 延迟初始化
开启延迟初始化可以减少应用程序所需的世界，在web应用程序中，启用延迟初始化将导致在接收到HTTP请求之前许多与web相关的bean不会被初始化。

延迟初始化的缺点是较晚得发现应用程序中的问题。如果一个错误配置的bean是延迟初始化的，在启动期间不会出现故障，故障会发生bean被初始化的时候。

还必须注意确保JVM有足够的内存来容纳应用程序的所有bean，而不仅仅是只容纳那些在启动期间初始化的bean。

由于这些原因，延迟初始化默认是禁用的，建议在开启延迟初始化之前对JVM的堆大小进行微调（fine-tuning）。

**用编程方式开启延迟初始化：**<br/>
使用```SpringApplicationBuilder```的```lazyInitialization```方法：

```java
SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
springApplicationBuilder.lazyInitialization(true);
```

使用```SpringApplication```的```setLazyInitialization```方法：

```java
SpringApplication springApplication = new SpringApplication(Application20200106.class);
springApplication.setLazyInitialization(true);
```

**配置文件开启延迟从初始化：**<br/>
使用```spring.main.lazy-initialization```属性：

```properties
spring.main.lazy-initialization=true
```

如果你希望一部分bean使用延迟初始化，一部分bean禁用延迟初始化，你可以使用```@Lazy(false)```注解。
- lazy = true，表示延迟，默认为true
- lazy = false，表示不延迟

## 1.3 自定义Banner
在==classpath==中添加一个```banner.txt```文件或者设置```spring.banner.location```属性来改变在启动期间打印的banner。<br/>
如果文件编码不是UTF-8，需要设置```spring.banner.charset```。<br/>
除了文本文件，还可以在==classpath==中添加```banner.gif```，```banner.jpg```，```banner.png```图片文件。或者设置```spring.banner.image.location```属性。<br/>
图片被转换成ASCII码打印在任何文本banner上方。

在```banner.txt```文件中，可以使用下列占位符：

变量 | 说明
---|---
```${application.version}``` | 应用程序的版本号，和```MANIFEST.MF```一样的声明。例如，```Implementation-Version: 1.0```打印成```1.0```。
```${application.formatted-version}``` | 应用程序的版本号，和```MANIFEST.MF```一样，按照格式显示（用圆括号括起来并且加上前缀```v```）。如```(v1.0)```。
```${spring-boot.version}``` | 你在使用的Spring Boot版本，如```2.2.2.RELEASE```。
```${spring-boot.formatted-version}``` | 你在使用的Spring Boot版本，按照格式显示（用圆括号括起来并且加上前缀```v```）。如```v2.2.2.RELEASE```。
```${Ansi.NAME} (or ${AnsiColor.NAME}, ${AnsiBackground.NAME}, ${AnsiStyle.NAME})``` | ```NAME```是ANSI转义码的名称，详情参见[```AnsiPropertySource ```](https://github.com/spring-projects/spring-boot/tree/v2.2.2.RELEASE/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/ansi/AnsiPropertySource.java)。
```${application.title}``` | 应用程序的Title，和```MANIFEST.MF```一样。如```Implementation-Title: MyApp```打印成```MyApp```。

如果你想用编程的方式生成一个banner，可以使用```SpringApplication.setBanner(…)```。<br/>
实现```org.springframework.boot.Banner```接口并重写```printBanner()```方法。

> You can also use the spring.main.banner-mode property to determine if the banner has to be printed on System.out (console), sent to the configured logger (log), or not produced at all (off).

你也可以使用```spring.main.banner-mode```属性控制banner是否在```System.out```(```console```)被打印，或者发送到已配置的日志程序，或者关闭。

打印banner的bean被注册成一个单例bean，名字为：```springBootBanner```。

## 1.4 自定义SpringApplication
如果默认的```SpringApplication```不是你的菜，你可以创建一个本地实例并对其设置。例如，关闭banner：
```java
public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(Application20200106.class);
    springApplication.setBannerMode(Banner.Mode.OFF);
    springApplication.run(args);
}
```

传给```SpringApplication```的构造方法参数是Spring beans的配置源。在大多数情况下，都是引用```@Configuration```类，但是也可以引用XML配置或引用被扫描的包。

**引用XML配置：**
```java
ResourceLoader resourceLoader = new ClassPathXmlApplicationContext("config/spring/user/applicationContext-user.xml");
SpringApplication springApplication = new SpringApplication(resourceLoader,Application20200106.class);
```

**引用被扫描的包：**

配置类```UserConfiguration```：

```java
package cn.shrmus.springboot.demo20200106.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("cn.shrmus.springboot.demo20200106.user")
@Configuration
public class UserConfiguration {
}
```

启动类```Application20200106```：

```java
@Import(value = cn.shrmus.springboot.demo20200106.configuration.UserConfiguration.class)
@SpringBootApplication
public class Application20200106 extends SpringApplication{
    public static void main(String[] args) {
        ResourceLoader resourceLoader = new AnnotationConfigApplicationContext("cn.shrmus.springboot.demo20200106.user");
//        ResourceLoader resourceLoader = new AnnotationConfigApplicationContext(cn.shrmus.springboot.demo20200106.configuration.UserConfiguration.class);

        SpringApplication springApplication = new SpringApplication(resourceLoader,Application20200106.class);

        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);

        UserController userController = configurableApplicationContext.getBean(UserController.class);
        System.out.println(userController);

        SpringApplication.exit(configurableApplicationContext);
    }
```


也可以使用```application.properties```文件配置```SpringApplication```，详情参考[外部化配置](#2._外部化配置)。

要查看```SpringApplication```的完整配置，参考[```SpringApplication``` Javadoc](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/api//org/springframework/boot/SpringApplication.html)。

## 1.5 构建流式API
如果需要构建一个```ApplicationCOntext```层次结构（具有父/子关系的多个上下文），或者你更喜欢使用构建“流式”API，你可以使用```SpringApplicationBuilder```。

> The ```SpringApplicationBuilder``` lets you chain together multiple method calls and includes ```parent``` and ```child``` methods that let you create a hierarchy, as shown in the following example:

使用```SpringApplicationBuilder```将包含```parent```和```child```方法等多个方法的调用都链在一起，以此创建一个层次结构，如下：

```java
new SpringApplicationBuilder()
        .sources(Parent.class)
        .child(Application.class)
        .bannerMode(Banner.Mode.OFF)
        .run(args);
```

创建```ApplicationContext```层次结构会有一些限制，Web组件被包含在子上下文中，父上下文和子上下文都使用同一个```Environment```。阅读[```SpringApplicationBuilder``` Javadoc](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/api//org/springframework/boot/builder/SpringApplicationBuilder.html)查看详情。

## 1.6 Application事件和监听器
除了常见的Spring框架事件（如[```ContextRefreshedEvent```](https://docs.spring.io/spring/docs/5.2.2.RELEASE/javadoc-api/org/springframework/context/event/ContextRefreshedEvent.html)）外，```SpringApplication```还发送一些额外的应用程序事件。










<span id="2._外部化配置"></span>
# 2. 外部化配置
## 2.3 应用程序属性文件
==application.properties==文件的加载顺序