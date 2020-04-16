> 【更新中】本文大部分内容翻译自官方文档[https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/spring-boot-features.html](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/spring-boot-features.html)

[TOC]

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

**表1 Banner变量**

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
public class Application20200106{
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

有些事件是在创建```ApplicationContext```之前触发的，所以不能将监听器注册成```@Bean```，你可以使用```SpringApplication.addListeners(…)```方法或```SpringApplicationBuilder.listeners(…)```方法注册它们。

如果你希望监听器能自动注册，不管应用程序时如何创建的，你可以在```META-INF/spring.factories```文件中使用```org.springframework.context.ApplicationListener```添加监听器的引用。如下：

```properties
org.springframework.context.ApplicationListener=cn.shrmus.springboot.demo20200106.listener.MyListener
```

在程序运行时，应用程序事件按一下顺序发送：
1. ```ApplicationStartingEvent```在运行开始但还没有任何处理之前发送，监听器和初始化的注册除外。
2. ```ApplicationEnvironmentPreparedEvent```在```Environment```要在已知的上下文中被使用，但上下文创建之前发送。
3. ```ApplicationContextInitializedEvent```在```ApplicationContext```准备好，ApplicationContextInitializers被调用，但还没加载任何bean definitions之前发送。
4. ```ApplicationPreparedEvent```在bean definitions加载后，启动刷新之前发送。
5. ```ApplicationStartedEvent```在上下文刷新之后，调用应用程序和命令行运行程序之前发送。
6. ```ApplicationReadyEvent```在应用程序和命令行运行程序被调用后发送。它表明应用程序已经准备好为请求提供服务。
7. ```ApplicationFailedEvent```在启动出现异常时发送。

上面的列表只包含绑定到```SpringApplication```中的```SpringApplicationEvent```。除此之外，以下事件会在```ApplicationPreparedEvnet```之后和```ApplicationStartedEvent```之前发布：

1. ```ContextRefreshedEvent```在刷新```ApplicationContext```时发送。

> A ```WebServerInitializedEvent``` is sent after the ```WebServer``` is ready.  ```ServletWebServerInitializedEvent``` and ```ReactiveWebServerInitializedEvent``` are the servlet and reactive variants respectively.

2. ```WebServerInitializedEvent```在```WebServer```准备好之后发送。```ServletWebServerInitializedEvent```和```ReactiveWebServerInitializedEvent```分别作用于servlet和reactive。（原文中==variants==原意是变种）

通常不需要使用应用程序事件，但是知道它们的存在是很方便的。在内部，Spring Boot使用事件处理各种任务。

> Application events are sent by using Spring Framework’s event publishing mechanism. Part of this mechanism ensures that an event published to the listeners in a child context is also published to the listeners in any ancestor contexts. As a result of this, if your application uses a hierarchy of ```SpringApplication``` instances, a listener may receive multiple instances of the same type of application event.

应用程序事件是使用Spring框架的事件发布机制发送的。此机制的一部分确保将事件发布到子上下文中的监听器，也能将事件发布到任何祖先上下文的监听器。因此，如果你的应用程序使用了```SpringApplication```实例的层次结构，则监听器可能会接收同一类型应用程序事件的多个实例。

> To allow your listener to distinguish between an event for its context and an event for a descendant context, it should request that its application context is injected and then compare the injected context with the context of the event. The context can be injected by implementing ```ApplicationContextAware``` or, if the listener is a bean, by using ```@Autowired```.

允许监听器区分它的上下文事件和子上下文的事件，它应该请求注入它的应用程序上下文，然后将注入的上下文与事件的上下文比较。上下文可以通过```ApplicationContextAware```实现注入，或者，如果监听器是bean，可以通过```@Autowired```注入。

## 1.7 Web Environment

一个```SpringApplication```会替你创建正确类型的```ApplicationContext```。用于确定```WebApplicationType```的算法相当简单：
- 如果存在Spring MVC，使用```AnnotationConfigServletWebServerApplicationContext```
- 如果不在存Spring MVC，但是存在Spring WebFlux，使用```AnnotationConfigReactiveWebServerApplicationContext```
- 否则，使用```AnnotationConfigApplicationContext```

这意味着如果你同一个应用程序中使用Spring MVC和来自于Spring WebFlux的```WebClinet```，那么默认情况下使用Spring MVC。<br/>
你也可以通过调用```setWebApplicationType(WebApplicationType)```来覆盖它。
还可以通过调用```setApplicationContextClass(…)```完全控制```ApplicationContext```类型。

在Junit测试中使用```ApplicationContext```时，通常需要调用```setWebApplicationType(WebApplicationType.NONE)```。

## 1.8 访问应用程序参数

如果你需要访问传递给```SpringApplication.run(…)```的应用程序参数，则需要注入```org.springframework.boot.ApplicationArguments```。

> The ```ApplicationArguments``` interface provides access to both the raw ```String[]``` arguments as well as parsed ```option``` and ```non-option``` arguments.

接口```ApplicationArguments```提供对原始```String[]```参数和解析过的```option```和```non-option```参数的访问。如下：

```java
import org.springframework.boot.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
public class MyBean {
    @Autowired
    public MyBean(ApplicationArguments args) {
        boolean debug = args.containsOption("debug");
        List<String> files = args.getNonOptionArgs();
        // if run with "--debug logfile.txt" debug=true, files=["logfile.txt"]
    }
}
```

Spring Boot还可以向Spring ```Environment```注册一个```CommandLinePropertySource```。还可以使用```@Value```注解注入单个应用程序参数。

## 1.9 使用ApplicationRunner或CommandLineRunner

如果你需要在```SpringApplication```启动后运行一些特定的代码，你可以实现```ApplicationRunner```或者```CommandLineRunner```接口。<br/>
这两个接口以相同的方式工作，并提供一个单一的```run```方法，这个方法在```SpringApplication.run(…)```结束之前被调用。

接口```CommandLineRunner```以简单字符串数组的形式提供对应用程序参数的访问，而```ApplicationRunner```使用谈论过的```ApplicationArguments```。下面的例子展示带```run```方法的```CommandLineRunner```：
```java
import org.springframework.boot.*;
import org.springframework.stereotype.*;

@Component
public class MyBean implements CommandLineRunner {
    public void run(String... args) {
        // Do something...
    }
}
```

如果定义了多个```CommandLineRunner```或```ApplicationRunner```bean，必须按特定的顺序调用它们。可以通过实现```org.springframework.core.Ordered```接口或```org.springframework.core.annotation.Order```注解控制调用顺序。

## 1.10 应用程序退出

每个```SpringApplication```向JVM注册一个shotdown hook确保```ApplicationContext```在退出时能够优雅地关闭。所有标准的Spring生命周期回调（例如```DisposableBean```接口或```@PreDestroy```注解）都会被使用。

另外，如果希望```SpringApplication.exit()```被调用时返回特殊的退出码，可以实现```org.springframework.boot.ExitCodeGenerator```接口。这个退出码会被传递给```System.exit()```作为状态码返回。如下：

```java
@SpringBootApplication
public class ExitCodeApplication {
    @Bean
    public ExitCodeGenerator exitCodeGenerator() {
        return () -> 42;
    }

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ExitCodeApplication.class, args)));
    }
}
```

此外，接口```ExitCodeGenerator```可以由异常实现。当遇到这样的异常，Spring Boot将返回退出码，退出码是实现这个接口时重写```getExitCode()```方法提供的退出码。

```java
import org.springframework.boot.ExitCodeGenerator;

public class CustomizingException extends Exception implements ExitCodeGenerator {
    @Override
    public int getExitCode() {
        return 43;
    }
}
```

## 1.11 管理特性

通过指定```spring.application.admin.enabled```属性来为应用程序开启管理相关的特性。这将在```MBeanServer```平台上公开[```SpringApplicationAdminMXBean```](https://github.com/spring-projects/spring-boot/tree/v2.2.2.RELEASE/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/admin/SpringApplicationAdminMXBean.java)。你可以使用这个特性来管理你的Spring Boot远程应用程序。这个特性对于任何服务包装器实现都是有用的。

如果你想知道应用程序在哪个HTTP端口上运行，获取```local.server.port```属性的值。

<span id="2._外部化配置"></span>
# 2. 外部化配置

Spring Boot允许您将配置外部化，以便可以在不同的环境中使用相同的应用程序代码。你可以使用properties文件，YAML文件，环境变量和命令行参数外部化配置。属性值可以通过使用```@Value```注解直接注入到bean中。通过Spring的```Environment```抽象访问，或通过```@ConfigurationProperties```[绑定到结构化对象](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/spring-boot-features.html#boot-features-external-config-typesafe-configuration-properties)。

Spring Boot使用一种非常特殊的```PropertySource```顺序，其设计目的是允许合理地覆盖值。属性按以下顺序排序：

1. [Devtools全局设置属性](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/using-spring-boot.html#using-boot-devtools-globalsettings)在```$HOME/.config/spring-boot```文件夹中的时候devtools是活动状态。
2. 在你的测试中使用[```@TestPropertySource```](https://docs.spring.io/spring/docs/5.2.2.RELEASE/javadoc-api/org/springframework/test/context/TestPropertySource.html)注解。
3. 在你的测试中使用```properties```属性。可以在[```@SpringBootTest```](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/api//org/springframework/boot/test/context/SpringBootTest.html)和[用于在你的应用程序中测试特定部分的测试注解](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-tests)。
4. 命令行参数。
5. 来自```SPRING_APPLICATION_JSON```的属性（嵌入在环境变量或系统属性中的内联JSON）。
6. ```ServletConfig```初始化参数。
7. ```ServletContext```初始化参数。
8. 来自```java:comp/env```的JNDI属性。
9. Java系统属性（```System.getProperties()```）。
10. 操作系统环境变量。
11. 只在```random.*```有属性的```RandomValuePropertySource```。
12. 打包jar之外的[Profile-specific应用程序属性](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/spring-boot-features.html#boot-features-external-config-profile-specific-properties)（```application-{profile}.properties```和YAML）。
13. 打包jar之内的[Profile-specific应用程序属性](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/spring-boot-features.html#boot-features-external-config-profile-specific-properties)（```application-{profile}.properties```和YAML）。
14. 打包jar之外的应用程序属性（```application.properties```和YAML）。
15. 打包jar之内的应用程序属性（```application.properties```和YAML）。
16. ```@Configuration```类上的[```@PropertySource```](https://docs.spring.io/spring/docs/5.2.2.RELEASE/javadoc-api/org/springframework/context/annotation/PropertySource.html)注解。请注意，在刷新应用程序上下文之前，不会将此类属性源添加到```Environment```中。在刷新开始之前配置某些属性（例如```logging.*```和```spring.main.*```）已经太晚了。
17. 默认属性（通过指定设置```SpringApplication.setDefaultProperties```）。

举一个具体的例子，假设你开发的一个```@Component```类使用```name```属性，如下：

```java
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;

@Component
public class MyBean {

    @Value("${name}")
    private String name;

    // ...

}
```

在你的应用程序==classpath==（jar内）中的```application.properties```文件为```name```提供一个合理的默认属性值。在新environment中运行时，可以在jar之外提供一个```application.properties```文件覆盖```name```。对于一次性测试，可以使用特定的命令行开关启动（```java -jar app.jar --name="Spring"```）。

在带有环境变量的命令行上提供```SPRING_APPLICATION_JSON```属性，使用下面的UNIX shell：

```shell
$ SPRING_APPLICATION_JSON='{"acme":{"name":"test"}}' java -jar myapp.jar
```

在前面的例子中，在Spring ```Environment```以```acme.name=test```结束。你还可以在系统属性中用```spring.application.json```提供JSON。如下：

```shell
$ java -Dspring.application.json='{"name":"test"}' -jar myapp.jar
```

还可以使用命令行参数提供JSON，如下：

```shell
$ java -jar myapp.jar --spring.application.json='{"name":"test"}'
```

还可以使用JNDI变量提供JSON，如下：

```
java:comp/env/spring.application.json
```

## 2.1 配置随机值

用于注入随机值的```RandomValuePropertySource```是非常有用的（作为秘钥或测试用例）。它可以生成integers，longs，uuids，strings。如下：

```properties
my.secret=${random.value}
my.number=${random.int}
my.bignumber=${random.long}
my.uuid=${random.uuid}
my.number.less.than.ten=${random.int(10)}
my.number.in.range=${random.int[1024,65536]}
```

语法：```random.int*```的语法是```OPEN value (,max) CLOSE```，其中```OPEN,CLOSE```是任意字符，```value,max```是整数。如果提供```max```，则```value```为最小值，```max```为最大值（不含）。

## 2.2 访问命令行属性

默认情况下，```SpringApplication```将任何命令行选项参数（参数以```--```开头，如```--server.port=9000```）转换为```property```，并将它们添加到Spring ```Environment```中。

> As mentioned previously, command line properties always take precedence over other property sources.

就如前面所说，命令行属性始终优先于其它属性源。

如果不希望将命令行属性添加到```Environment```中，可以使用```SpringApplication.setAddCommandLineProperties(false)```来禁用。

## 2.3 应用程序属性文件

加载配置文件：```SpringApplication```从以下位置的```application.properties```文件中加载属性并将属性添加到Spring ```Environment```中：
1. 当前目录的```/config```子目录
2. 当前目录
3. ==classpath==下的```/config```包
4. ==classpath==的根目录

列表按优先级排序（在列表中较高位置定义的属性覆盖在较低位置定义的属性）。

你也可以[使用YAML('yml')文件](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/spring-boot-features.html#boot-features-external-config-yaml)替代'.properties'。

如果不想用```application.properties```作为配置文件名，你也可以指定```spring.config.name```环境属性来切换到另一个文件名。还可以使用```spring.config.location```环境属性来引用一个显示的位置（以逗号分隔的目录位置或文件路径）。

通过```spring.config.name```指定一个文件名：

```shell
$ java -jar myproject.jar --spring.config.name=myproject
```

通过```spring.config.location```指定文件位置：

```shell
$ java -jar myproject.jar --spring.config.location=classpath:/default.properties,classpath:/override.properties
```

```spring.config.name```和```spring.config.location``` 很早就用于确定必须加载哪些文件。它们必须定义为环境属性（通常是操作系统环境变量，系统属性，命令行参数）。

如果```spring.config.location```包含目录（与文件相反），它们应该以```/```结尾（在运行时，在加载前附加由```spring.config.name```生成的名称，包含profile-specific文件名）。```spring.config.location```中指定的文件按原样使用，不支持profile-specific变体，并被任何profile-specific属性覆盖。

配置位置以相反的顺序搜索。默认情况下，配置位置是```classpath:/```，```classpath:/config/```，```file:./```，```file:./config/```。搜索结果的顺序如下：
1. ```file:./config/```
2. ```file:./```
3. ```classpath:/config/```
4. ```classpath:/```

当使用```spring.config.location```自定义配置位置时，它们替代默认的位置。例如，如果```spring.config.location```的值被配置成```classpath:/custom-config/```，```file:./custom-config/```，搜索顺序如下：
1. ```file:./custom-config/```
2. ```classpath:custom-config/```

作为选择，当使用```spring.config.additional-location```自定义配置位置时，它们被使用除默认位置外。附加位置在默认位置之前被搜索。例如，如果附加位置```classpath:/custom-config/```，```file:./custom-config/```被配置，搜索顺序如下：
1. ```file:./custom-config/```
2. ```classpath:custom-config/```
3. ```file:./config/```
4. ```file:./```
5. ```classpath:/config/```
6. ```classpath:/```

> This search ordering lets you specify default values in one configuration file and then selectively override those values in another. You can provide default values for your application in ```application.properties``` (or whatever other basename you choose with ```spring.config.name```) in one of the default locations. These default values can then be overridden at runtime with a different file located in one of the custom locations.

这种搜索顺序允许您在一个配置文件中指定默认值，然后有选择地在另一个配置文件中覆盖这些值。你可以在一个默认位置的```application.properties```中为你的应用程序提供默认值（或你选择的任何其他基本的```spring.config.name```）。在运行时使用位于自定义位置中另一个文件覆盖这些默认值。

如果使用环境变量而不是系统属性，大部分操作系统不允许点分隔（period-separated）的键名，但你可以使用下划线（```SPRING_CONFIG_NAME```代替```spring.config.name```）。

如果应用程序在容器中运行，然后可以使用JNDI属性（在```java:comp/env```中）或servlet上下文初始化参数来代替环境变量或系统属性。

## 2.4 Profile-specific属性

除```application.properties```文件之外，profile-specific属性也能通过使用```application-{profile}.properties```命名约定被定义。```Environment```有一组默认配置文件（默认情况下，```[default]```），如果没有设置活动配置文件，就使用这些默认配置文件。换句话说，如果没有配置文件被明确激活，那么属性从```application-default.properties```被加载。

Profile-specific属性从与标准```application.properties```相同的位置加载，使用profile-specific文件总是覆盖non-specific文件，不管profile-specific文件位于打包的jar的内部或外部。

> If several profiles are specified, a last-wins strategy applies. For example, profiles specified by the ```spring.profiles.active``` property are added after those configured through the ```SpringApplication``` API and therefore take precedence.

如果多个配置文件被指定，则应用最后配置的策略。例如，```spring.profiles.active```属性指定的配置文件添加在通过```SpringApplication``` API配置之后，因此优先。

假设```application.properties```中有如下配置：

```properties
spring.profiles.active=pro,dev
```

最终生效的是```application-dev.properties```文件。

如果你在```spring.config.location```指定了一些文件，profile-specific的变体不被考虑。如果你想使用profile-specific属性，在```spring.config.location```中使用目录。

## 2.5 占位符属性

文件```application.properties```中的值在使用时通过现有的```Environment```过滤，所以你可以返回到以前定义的值（例如，系统属性中定义的值）。

```properties
app.name=MyApp
app.description=${app.name} is a Spring Boot application
```

您还可以使用此技术创建现有Spring Boot属性的“短”变体。点击[howto.html](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/howto.html#howto-use-short-command-line-arguments)查看详情。

## 2.6 加密属性

Spring Boot不提供任何对属性值加密的支持，然而，它提供了修改Spring ```Environment```中包含的值所必须的hook点。```EnvironmentPostProcessor```接口允许你在应用启动之前操作（manipulate）```Environment```。点击[howto.html](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/howto.html#howto-customize-the-environment-or-application-context)查看详情。

如果您正在寻找一种安全的方式来存储凭证和密码，[Spring Cloud Vault](https://cloud.spring.io/spring-cloud-vault/)项目提供了在[HashiCorp Vault](https://www.vaultproject.io/)中存储外部化配置的支持。

## 2.7 使用YAML代替Properties

YAML是JSON的超集，因此是指定分层配置数据的一种方便的格式。当你的classpath中有SnakeYAML库时，```SpringApplication```类自动支持YAML作为properties的另一种选择。

SnakeYAML是由```spring-boot-starter```自动提供的。

### 2.7.1 加载YAML

Spring框架提供了两个方便的类用于加载YAML文档。```YamlPropertiesFactoryBean```将YAML加载为```Properties```，```YamlMapFactoryBean```将YAML加载为```Map```。

例如：参考以下YAML文档：

```yaml
environments:
    dev:
        url: https://dev.example.com
        name: Developer Setup
    prod:
        url: https://another.example.com
        name: My Cool App
```

将上述YAML文档转换为下列properties：

```properties
environments.dev.url=https://dev.example.com
environments.dev.name=Developer Setup
environments.prod.url=https://another.example.com
environments.prod.name=My Cool App
```

YAML列表用[```index```]作为引用，代表属性键。如下：

```yaml
my:
   servers:
       - dev.example.com
       - another.example.com
```

将上述YAML文档转换为下列properties：

```properties
my.servers[0]=dev.example.com
my.servers[1]=another.example.com
```

通过使用Spring Boot的```Binder```工具（```@ConfigurationProperties```就是这么做的）来绑定这样的属性。如果在目标bean中有```java.util.List```（或者```Set```）类型的属性，你需要提供setter或使用可变值初始化它。下面的例子绑定到上述属性：

```java
@ConfigurationProperties(prefix="my")
public class Config {

    private List<String> servers = new ArrayList<String>();

    public List<String> getServers() {
        return this.servers;
    }
}
```

### 2.7.2 将YAML作为Properties公开在Spring环境

类```YamlPropertySourceLoader```可将YAML作为```PropertySource```公开在Spring环境中。这样就可以使用带有占位符语法的```@Value```注解来访问YAML属性。

### 2.7.3 Multi-profile YAML文档

通过使用```spring.profiles```键，你可以在一个文件中指定多个profile-specific YAML文档，指定文档何时应用，如下：

```yaml
server:
    address: 192.168.1.100
---
spring:
    profiles: development
server:
    address: 127.0.0.1
---
spring:
    profiles: production & eu-central
server:
    address: 192.168.1.120
```

如果```development```配置文件是激活的，那么```server.address```的属性值为```127.0.0.1```。<br/>
同理，如果```production```和```eu-central```配置文件是激活的，```server.address```的属性值为```192.168.1.120```。<br/>
如果```development```，```production```和```eu-central```配置文件都没有启用，那么```server.address```的属性值为```192.168.1.100```。

```spring.profiles```可以包含简单的配置文件名（如```production```）或配置文件表达式。配置文件表达式允许表达更复杂的配置文件逻辑，如```production & (eu-central | eu-west)```。查看[参考指南](https://docs.spring.io/spring/docs/5.2.2.RELEASE/spring-framework-reference/core.html#beans-definition-profiles-java)了解更多详情。

如果在应用程序上下文启动时没有显式激活配置文件，则默认配置文件将被激活。因此，在下列YAML中，我们为```spring.security.user.password```设置了一个仅在默认配置文件中可用的值：

```yaml
server:
  port: 8000
---
spring:
  profiles: default
  security:
    user:
      password: weak
```

然而，在下面的例子中，密码总是被设置，因为它没有附加到任何配置文件中，而且它必须在所有其他配置文件中被显式重置：

```yaml
server:
  port: 8000
spring:
  security:
    user:
      password: weak
```

> Spring profiles designated by using the ```spring.profiles``` element may optionally be negated by using the ```!``` character. If both negated and non-negated profiles are specified for a single document, at least one non-negated profile must match, and no negated profiles may match.

使用```spring.profiles```元素指定的Spring配置文件通过使用```!```字符选择性地否定。如果为单个文档指定了否定配置文件和非否定配置文件，至少有一个非否定的配置文件必须匹配，并且否定的配置文件不能匹配。

### 2.7.4 YAML的不足

YAML文件不能通过```@PropertySource```注解加载。因此，在需要以这种方式加载值的情况下，需要使用属性文件。

在profile-specific YAML文件中使用multi YAML文档语法可能导致意外发生。如下：

**application-dev.yml**

```yaml
server:
  port: 8000
---
spring:
  profiles: "!test"
  security:
    user:
      password: "secret"
```

如果使用参数```--spring.profiles.active=dev```运行应用程序，你可能希望```security.user.password```设置为```secret```，但是并不会。

嵌套的文档将被过滤，因为主文件名为```application-dev.yml```。它已经被认为是profile-specific，嵌套文档将被忽略。

建议不要将profile-specific YAML文件和多个YAML文档混合使用。只使用其中一个。

## 2.8 类型安全的配置属性

使用```@Value("${property}")```注解注入配置属性有时会很麻烦（cumbersome），特别（especially）是在处理多个属性或数据本质上是分层的情况下。Spring Boot提供了另一种处理属性的方法，允许强类型bean控制和验证应用程序的配置。

请参见[```@Value```和```类型安全配置属性```之间的区别](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/spring-boot-features.html#boot-features-external-config-vs-value)。

<span id="2.8.1_JavaBean属性绑定"></span>
### 2.8.1 JavaBean属性绑定

可以绑定一个声明标准JavaBean属性的bean，如下面的例子所示：

```java
package com.example;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("acme")
public class AcmeProperties {

    private boolean enabled;

    private InetAddress remoteAddress;

    private final Security security = new Security();

    public boolean isEnabled() { ... }

    public void setEnabled(boolean enabled) { ... }

    public InetAddress getRemoteAddress() { ... }

    public void setRemoteAddress(InetAddress remoteAddress) { ... }

    public Security getSecurity() { ... }

    public static class Security {

        private String username;

        private String password;

        private List<String> roles = new ArrayList<>(Collections.singleton("USER"));

        public String getUsername() { ... }

        public void setUsername(String username) { ... }

        public String getPassword() { ... }

        public void setPassword(String password) { ... }

        public List<String> getRoles() { ... }

        public void setRoles(List<String> roles) { ... }

    }
}
```

上述POJO定义了下列属性：
- ```acme.enabled```，默认值为```false```。
- ```acme.remote-address```，可以强制使用来自```String```的类型。
- ```acme.security.username```，嵌套的"security"对象，其名称由属性的名称决定。特别是（In particular），返回类型没有被使用，可能是```SecurityProperties```。
- ```acme.security.password```。
- ```acme.security.roles```，默认是```USER```的```String```集合。

Spring Boot自动配置大量使用```@ConfigurationProperties```来轻松配置自动配置的bean。<br/>
类似于自动配置类，在Spring Boot中可用的```@ConfigurationProperties```类仅供内部使用。<br/>

> The properties that map to the class, which are configured via properties files, YAML files, environment variables etc., are public API but the content of the class itself is not meant to be used directly.

映射到类的属性（通过属性文件、YAML文件、环境变量等配置）是公共API，但是类本身的内容并不意味着可以直接（directly）使用。

这种安排依赖于（relies on）默认的空构造函数，getter和setter通常是强制性的（mandatory），因为绑定是通过标准的Java bean属性描述符进行的，就像在Spring MVC中一样。在下列情况下可省略setter：
- 只要映射被初始化，就需要一个getter，但不一定是setter，因为绑定器可以对它们进行修改。
- 可以通过索引（通常使用YAML）或使用单个逗号分隔（comma-separated）的值（属性）访问集合和数组。在后一个情况下，setter是必需的。我们建议始终为此类型添加setter。如果你初始化一个集合，请确保它是可变的（not immutable）（如前面的实例所示）。
- 如果初始化了嵌套的POJO属性(如前面示例中的```Security```字段)，则不需要setter。如果你想用绑定器使用其默认构造方法动态（on the fly）创建一个实例，则需要setter。

有些人使用Lombok自动添加getter和setter。请确保Lombok不会为这样的类型生成任何特定的（particular）构造方法，因为容器会自动使用它来实例化对象。

最后，只有标准Java Bean属性被支持，不支持绑定静态属性。

### 2.8.2 构造方法绑定

上一节的示例可以在不变的情况下（in an immutable fashion）重写，如下所示：

```java
package com.example;

import java.net.InetAddress;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.DefaultValue;

@ConstructorBinding
@ConfigurationProperties("acme")
public class AcmeProperties {

    private final boolean enabled;

    private final InetAddress remoteAddress;

    private final Security security;

    public AcmeProperties(boolean enabled, InetAddress remoteAddress, Security security) {
        this.enabled = enabled;
        this.remoteAddress = remoteAddress;
        this.security = security;
    }

    public boolean isEnabled() { ... }

    public InetAddress getRemoteAddress() { ... }

    public Security getSecurity() { ... }

    public static class Security {

        private final String username;

        private final String password;

        private final List<String> roles;

        public Security(String username, String password,
                @DefaultValue("USER") List<String> roles) {
            this.username = username;
            this.password = password;
            this.roles = roles;
        }

        public String getUsername() { ... }

        public String getPassword() { ... }

        public List<String> getRoles() { ... }

    }

}
```

在这个设置中，```@ConstructorBinding```注解用于指示（indicate）应该使用构造函数绑定。<br/>

> This means that the binder will expect to find a constructor with the parameters that you wish to have bound.

这意味着绑定器将期望找到带参数的构造方法，而这些参数是已绑定的。

具有```@ConstructorBinding```类的内部类（如上面示例中的```Security```）也将通过其构造函数绑定。

可以使用```@DefaultValue```指定默认值，并用相同的转换服务将```String```值强制（coerce）转换为缺失属性的目标类型。

要使用构造函数绑定，必须使用启动```@EnableConfigurationProperties```或配置属性扫描。由常规Spring机制创建的bean（如```@Component``` bean，通过（via）```@Bean```方法创建的bean，或者通过```@Import```加载的bean）不能使用构造函数绑定。

如果您的类有多个构造方法，您还可以直接在需要绑定的构造方法上使用```@ConstructorBinding```。

### 2.8.3 开启```@ConfigurationProperties```注解类型

Spring Boot提供了绑定```@ConfigurationProperties```类型并将它们注册为bean的基础设施（infrastructure）。你可以逐个类地启用配置属性，或启用与组件扫描工作方式类似的配置属性扫描。

有时候，用```@ConfigurationProperties```注解的类不适合扫描，例如，如果您正在开发自己的自动配置，或者希望有条件地启用它们。在这些情况下，使用```@EnableConfigurationProperties```注解指定要处理的类型列表。这个注解可以用在任何```@Configuration```类上，如下：

```java
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AcmeProperties.class)
public class MyConfiguration {
}
```

要想使用配置属性扫描，要将```@ConfigurationPropertiesScan```注解添加到你的应用程序中。通常（typically），它被添加到使用```@SpringBootApplication```注解的主应用程序类中，但```@ConfigurationPropertiesScan```注解也可以被添加到任何```@Configuration```类中。默认情况下，将对声明注解的类的包进行扫描。如果你想扫描特定的包，你可以按下面的例子来做：

```java
@SpringBootApplication
@ConfigurationPropertiesScan({ "com.example.app", "org.acme.another" })
public class MyApplication {
}
```

当```@ConfigurationProperties``` bean使用配置属性扫描或通过```@EnableConfigurationProperties```注册时，这个bean户有一个按照惯例的（conventional）名称：```<prefix>-<fqn>```，其中```<prefix>```是在```@ConfigurationProperties```注解中指定的环境键前缀，```<fqn>```是bean的全限定名（the fully qualified name of the bean）。如果注解不提供任何前缀，则只使用bean的全限定名。

上述2.8.2的例子中的bean名是```acme-com.example..AcmeProperties```。

建议```@ConfigurationProperties```只处理（deal with）环境，特别（in particular）是不从上下文注入其他bean。对于极端（corner）情况，可以使用setter注入或框架提供的任何```*Aware```接口（例如你要访问```Environment```可以使用```EnvironmentAware```）。如果你仍然要使用构造方法注入其他bean，则必须使用```@Component```注解配置属性bean，并使用[JavaBean属性绑定](#2.8.1_JavaBean属性绑定)。

### 2.8.4 使用```@ConfigurationProperties```注解类型

这种配置方式在```SpringApplication```外部的YAML配置中特别好用，如下面的例子所示：

```yaml
# application.yml

acme:
    remote-address: 192.168.1.1
    security:
        username: admin
        roles:
          - USER
          - ADMIN

# additional configuration as required
```

要使用```@ConfigurationProperties``` bean，你可以与任何其他bean相同的方式注入它们，如下所示：

```java
@Service
public class MyService {

    private final AcmeProperties properties;

    @Autowired
    public MyService(AcmeProperties properties) {
        this.properties = properties;
    }

    //...

    @PostConstruct
    public void openConnection() {
        Server server = new Server(this.properties.getRemoteAddress());
        // ...
    }

}
```

> Using @ConfigurationProperties also lets you generate metadata files that can be used by IDEs to offer auto-completion for your own keys. 

使用```@ConfigurationProperties```还可以生成元数据文件，IDE可以使用这些元数据文件为你的键提供自动完成功能。详见[appendix](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/appendix-configuration-metadata.html#configuration-metadata)。

### 2.8.5 第三方配置

除了（as well as）使用```@ConfigurationProperties```注解类外，您还可以在公共```@Bean```方法上使用它。当你想将属性绑定到超出你控制范围外的第三方组件时，这样做特别有用。

从```Environment```属性配置bean，将```@ConfigurationProperties```添加到它的bean registration中，如下所示：

```java
@ConfigurationProperties(prefix = "another")
@Bean
public AnotherComponent anotherComponent() {
    ...
}
```

> Any JavaBean property defined with the ```another``` prefix is mapped onto that ```AnotherComponent``` bean in manner similar to the preceding ```AcmeProperties``` example.

使用```another```前缀定义的任何JavaBean属性都将以类似前面的（preceding）```AcmeProperties```示例的方式映射到```AnotherComponent``` bean。

### 2.8.6 宽松的绑定

Spring Boot使用一些宽松的（relaxed）规则将```Environment```属性绑定到```@ConfigurationProperties``` beans中，因此，```Environment```属性名和bean属性名不需要完全匹配（exact match）。有用的常见示例包括短横线分隔的（dash-speparated）环境属性（如```context-path```绑定到```contextPath```），以及大写的（capitalized）环境属性（如```PORT```绑定到```port```）。如下所示：

```java
@ConfigurationProperties(prefix="acme.my-project.person")
public class OwnerProperties {

    private String firstName;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
```

使用上述代码，可以使用以下属性名：

**表2 宽松的绑定**

属性 | 描述
---|---
```acme.my-project.person.first-name``` | 短横线分隔的（kebab case）命名方式，建议在```.properties```和```.yml```文件中使用。
```acme.myProject.person.firstName``` | 标准驼峰命名法（Standard camel case syntax）。
```acme.my_project.person.first_name``` | 下划线命名法（Underscore notation），它是```.properties```和```.yml```文件中使用的另一种格式（alternative format）。
```ACME_MYPROJECT_PERSON_FIRSTNAME``` | 大写命名法（upper case format），建议用于系统环境变量。

注解的```prefix```值必须是短横线分隔的命名方式（小写用```-```分隔，如```acme.my-project..person```）。

**表3 每个属性源的宽松的绑定规则**

属性源 | 简单的 | 列表
---|---|---
Properties文件 | 驼峰命名法，短横线分隔命名法，或下划线命名法 | 使用```[]```的标准列表语法（syntax）或逗号分隔的值（comma-separated）
YAML文件 | 驼峰命名法，短横线分隔命名法，或下划线命名法 | 标准的YAML列表语法或逗号分隔的值
环境变量 | 用下划线作为分隔符的大写命名法。```_```不应在属性名中使用 | 被下划线环绕（surrounded）的数值（numeric values）。例如```MY_ACME_1_OTHER = my.acme[1].other```
系统变量 | 驼峰命名法，短横线分隔命名法，或下划线命名法 | 使用```[]```的标准列表语法（syntax）或逗号分隔的值（comma-separated）

建议属性存储为小写的短横线分隔的命名方法，如```my.property-name=acme```。

绑定到```Map```属性时，如果```key```包含除了（other than）小写字母，数字字符（alpha-numeric characters），或```-```之外的任何内容，你需要使用括号符号（bracket notation），以便原来的（original）值被保存（is preserved）。如果```key```没有被```[]```环绕（is not surrounded），任何不是字母数字或-的字符都被删除。例如，以下属性绑定到```Map```：

```yaml
acme:
  map:
    "[/key1]": value1
    "[/key2]": value2
    /key3: value3
```

上面的属性将绑定到以```/key1```、```/key2```和```key3```作为map中的key的```Map```。

> For YAML files, the brackets need to be surrounded by quotes for the keys to be parsed properly.

YAML文件，要正确解析键，方括号（brackets）需要用引号（quotes）包围。

### 2.8.7 合并复杂类型

当有多个列表配置在多个地方，通过覆盖来替换整个列表。

例如，假设```MyPojo```对象的```name```和```description```属性默认为```null```。下面展示一个```AcmeProperties```类中包含```MyPojo```列表：

```java
@ConfigurationProperties("acme")
public class AcmeProperties {

    private final List<MyPojo> list = new ArrayList<>();

    public List<MyPojo> getList() {
        return this.list;
    }
}
```

使用下面的配置：

```properties
acme:
  list:
    name: my name
    description: my description
---
spring:
  profiles: dev
acme:
  list:
    name: my another name
```

如果```dev```配置文件不是活动的，```AcmeProperties.list```包含了一个```MyPojo```，如前面定义的那样。如果```dev```配置文件被启用，```list```仍然只包含一个元素（```name```值为```my another name```并且```description```值为```null```），该配置不会向list中添加第二个```MyPojo```实例，并且不会合并项（items）。

当```List```被指定在多个配置文件中时，具有最高优先级的那个（并且只使用那个）被使用。如下所示：

```properties
acme:
  list:
    name: my name
    description: my description
    name: another name
    description: another description
---
spring:
  profiles: dev
acme:
  list:
    name: my another name
```

在前面的例子中，如果```dev```配置文件是活动的，```AcmeProperties.list```包含了一个```MyPojo```（```name```值为```my another name```并且```description```值为```null```）。对于YAML，可以使用逗号分隔的列表和YAML列表来完全覆盖list的内容。

对于```Map```属性，你可以绑定来自多个源的属性值。但是，对于多个源中的相同属性，将使用具有最高优先级的属性。下面展示一个```AcmeProperties```类中包含```Map<String, MyPojo>```：

```java
@ConfigurationProperties("acme")
public class AcmeProperties {

    private final Map<String, MyPojo> map = new HashMap<>();

    public Map<String, MyPojo> getMap() {
        return this.map;
    }
}
```

使用下面的配置：

```properties
acme:
  map:
    key1:
      name: my name 1
      description: my description 1
---
spring:
  profiles: dev
acme:
  map:
    key1:
      name: dev name 1
    key2:
      name: dev name 2
      description: dev description 2
```

如果```dev```配置文件不是活动的，```AcmeProperties.map```包含一个```key1```（```name```值为```my name 1```并且```description```值为```my description 1```）。如果```dev```文件被启用，```map```包含两个元素```key1```（```name```值为```dev name 1```并且```description```值为```my description 1```）和```key2```（```name```值为```dev name 2```并且```description```值为```dev description 2```）

前面的合并规则适用于来自所有属性源的属性，而不仅仅是YAML文件。

### 2.8.8 属性转换

Spring Boot绑定到```@ConfigurationProperties```bean时，它尝试将外部应用程序属性强制转换为正确的类型。如果需要自定义类型转换，你可以提供一个```ConversionService```类（使用命名为```conversionService```的类）或自定义属性编辑器（通过```CustomEditorConfigurer```类）或自定义转换器```Converters```（使用注释为```@ConfigurationPropertiesBinding```的类）。

因为这个bean是在应用程序生命周期的早期请求的，因此确保限制```ConversionService```使用的依赖项。通常，任何你需要的依赖项可能在创建时没有完全初始化。你可能希望重命名自定义```ConversionService```，如果不需要配置keys则强制执行，并且只依赖于使用```@ConfigurationPropertiesBinding```的自定义转换器。

#### **转换时间**（Converting durations）

Spring Boot提供专门的工具来表达时间。如果你公开一个```java.time.Duration```属性，在应用程序属性中可使用以下格式：

- 常规```long```表达（使用毫秒（milliseconds）作为默认单元，除非已指定```@DurationUnit```）
- [```java.time.Duration```](https://docs.oracle.com/javase/8/docs/api//java/time/Duration.html#parse-java.lang.CharSequence-)使用的标准ISO-8601格式
- 一种更具可读性的格式，值和单位是耦合（coupled）的（例如```10s```表示10秒）

如下所示：

```java
@ConfigurationProperties("app.system")
public class AppSystemProperties {

    @DurationUnit(ChronoUnit.SECONDS)
    private Duration sessionTimeout = Duration.ofSeconds(30);

    private Duration readTimeout = Duration.ofMillis(1000);

    public Duration getSessionTimeout() {
        return this.sessionTimeout;
    }

    public void setSessionTimeout(Duration sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Duration getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }
}
```

指定30秒后会话超时，```30```，```PT30S```，```30s```都可以。读取超时为500ms，可以用```500```，```PT0.5S```，```500ms```。

还可以使用任何支持的单位，如：

- ```ns```表示纳秒（nanosecond）
- ```us```表示微秒（microsecond）
- ```ms```表示毫秒（millisecond）
- ```s```表示秒（second）
- ```m```表示分（minute）
- ```h```表示时（hour）
- ```d```表示天（day）

默认单位是毫秒，可以使用```@DurationUnit```覆盖，如上面的例子所示。

如果你从以前的版本升级到使用```Long```来表示时间，如果切换到```Duration```的单位不是毫秒，请确保使用```@DurationUnit```定义这个单位。这样做提供了一个透明的升级路径，同时提供更丰富的格式。

#### 转换数据大小（Converting Data Sizes）

Spring框架有一个以字节表示大小的```DataSize```值类型。如果你公开一个```DataSize```属性，在应用程序属性中可使用以下格式：

- 常规```long```表达（使用字节作为默认单元，除非已指定```@DataSizeUnit```）
- [```java.time.Duration```](https://docs.oracle.com/javase/8/docs/api//java/time/Duration.html#parse-java.lang.CharSequence-)使用的标准ISO-8601格式
- 一种更具可读性的格式，值和单位是耦合（coupled）的（例如```10MB```表示10兆）

如下所示：

```java
@ConfigurationProperties("app.io")
public class AppIoProperties {

    @DataSizeUnit(DataUnit.MEGABYTES)
    private DataSize bufferSize = DataSize.ofMegabytes(2);

    private DataSize sizeThreshold = DataSize.ofBytes(512);

    public DataSize getBufferSize() {
        return this.bufferSize;
    }

    public void setBufferSize(DataSize bufferSize) {
        this.bufferSize = bufferSize;
    }

    public DataSize getSizeThreshold() {
        return this.sizeThreshold;
    }

    public void setSizeThreshold(DataSize sizeThreshold) {
        this.sizeThreshold = sizeThreshold;
    }
}
```

指定10兆字节的缓冲区大小，```10```，```10MB```都可以。256字节的大小阈值可以指定为```256```，```256B```。

还可以使用任何支持的单位，如：

- ```B```表示字节（byte）
- ```KB```表示千字节（kilobyte）
- ```MB```表示兆字节（megabyte）
- ```GB```表示千兆字节，吉字节（gigabyte）
- ```TB```表示兆兆字节，太字节（terabyte）

默认单位是字节，可以使用```@DataSizeUnit```覆盖，如上面的例子所示。

如果你从以前的版本升级到使用Long来表示字节，如果切换到```DataSize```的单位不是字节，请确保使用```@DataSizeUnit```定义这个单位。这样做提供了一个透明的升级路径，同时提供更丰富的格式。














