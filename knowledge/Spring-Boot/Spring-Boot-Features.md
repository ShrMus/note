
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

如果```spring.config.location```包含目录（与文件相反），






















