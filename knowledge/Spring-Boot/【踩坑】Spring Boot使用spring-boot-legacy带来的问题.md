> 本文记录旧项目重构成Spring Boot项目中遇到的一些问题。

[TOC]

# Spring Boot使用web.xml

虽然Spring官方文档提供了用编程的方式替代`web.xml`配置文件，参考[DispatcherServlet](https://docs.spring.io/spring/docs/5.3.0-SNAPSHOT/spring-framework-reference/web.html#mvc-servlet)和[Servlet Config](https://docs.spring.io/spring/docs/5.3.0-SNAPSHOT/spring-framework-reference/web.html#mvc-container-config)，但是接受新东西确实总是后知后觉，还想保留原来的配置文件怎么办。

另外，官网说是基于Servlet 3.0的基础上，如果是Servlet 2.5则需要按照下面的方法做，如果是Servlet 3.0+就不用改了。

在`pom.xml`中加上依赖（注意版本，我写的版本是最新的）：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-legacy</artifactId>
    <version>2.1.0.RELEASE</version>
</dependency>
```

修改`web.xml`：

```xml
<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>cn.shrmus.demo20200528.DemoApplication20200528</param-value> <!-- 启动类 -->
</context-param>

<listener>
	<listener-class>org.springframework.boot.legacy.context.web.SpringBootContextLoaderListener</listener-class>
</listener>
```

# Spring Boot整合jsp

原项目中可能有jsp页面，还需要servlet、jsp、jstl的包。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.tomcat</groupId>
    <artifactId>tomcat-servlet-api</artifactId>
    <version>9.0.35</version>
</dependency>
<dependency>
    <groupId>org.apache.tomcat</groupId>
    <artifactId>tomcat-jsp-api</artifactId>
    <version>9.0.35</version>
</dependency>
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
</dependency>
```

# 打包的文件

展示一下我写的Demo文件目录：

![image](https://img-blog.csdnimg.cn/20200529212106224.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1Nock11cw==,size_16,color_FFFFFF,t_70)

文件路径根据你的项目文件目录来写，这样Mavan在打包的时候才会把这些目录下的文件添加进去。不要照抄！不要照抄！不要照抄！

```xml
<resources>
    <resource>
        <directory>src/main/resources</directory>
        <includes>
            <include>**/**</include>
        </includes>
    </resource>
    <resource>
        <directory>src/main/webapp</directory>
        <includes>
            <include>**/**</include>
        </includes>
    </resource>
</resources>
```

# 使用spring-boot-legacy带来的问题

后面的问题重现了很多遍，确认不是偶然发生才记录下来的。

访问webapp目录下的jsp文件是正常的。

能执行Controller中的方法，但是不能正确返回WEB-INF目录下的jsp文件，出现404。

我猜想可能是ViewResolver没被注册到Spring容器。

首先检查了一下`springmvc.xml`的配置路径，没问题。

在`springmvc.xml`中，注册一个命名为`internalResourceViewResolver`的bean：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<!-- 配置模型视图的管理器 -->
    <bean id="internalResourceViewResolver"
		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/WEB-INF/page/" />
		<property name="suffix" value=".jsp" />
	</bean>
</beans>
```

文件路径，文件内容都没问题，然后查看了一下Spring容器中的bean，`beanDefinitionMap`里面没有命名为`internalResourceViewResolver`的bean。

先解决问题吧，解决方法有两种。

第一种，在`application.properties`添加配置，这种方式使用的是`org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator`：

```properties
spring.mvc.view.prefix=/WEB-INF/page/
spring.mvc.view.suffix=.jsp
```

第二种，代码注册一个VeiwResolver：

```java
@Bean
public InternalResourceViewResolver internalResourceViewResolver() {
    InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
    internalResourceViewResolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
    internalResourceViewResolver.setContentType("text/html");
    String VIEW_PREFIX = "/WEB-INF/page/";
    String VIEW_SUFFIX = ".jsp";
    internalResourceViewResolver.setPrefix(VIEW_PREFIX);
    internalResourceViewResolver.setSuffix(VIEW_SUFFIX);
    return internalResourceViewResolver;
}
```

# 问题出现的原因

虽然上面这两种方式能解决问题，但我还是想找一下问题出在哪里，开始debug，源码中注册的ViewResolver命名为`defaultViewResolver`，虽然是返回`InternalResourceViewResolver`类型，但是`@Bean`是根据方法名注册进Spring容器的。

![image](https://img-blog.csdnimg.cn/20200529212202919.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1Nock11cw==,size_16,color_FFFFFF,t_70)

这也就解释了为什么我在Spring容器里找不到命名为`internalResourceViewResolver`的bean。

我尝试修改`springmvc.xml`中bean id，还是没用。

最后我注释了`web.xml`表示DispatcherServlet的这段代码。

```xml
<!--
	<servlet>
		<servlet-name>springmvc</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:spring/springmvc.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>springmvc</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
-->
```

在启动类中引入`springmvc.xml`：

```xml
@ImportResource(value = "classpath:/spring/springmvc.xml")
```

将`springmvc.xml`中`mvc`开头的标签代码注释，如`<mvc:annotation-driven>`，`<mvc:resources>`，不注释会报一个这样的错：

```
Description:

The bean 'mvcContentNegotiationManager', defined in class path resource [org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$EnableWebMvcConfiguration.class], could not be registered. A bean with that name has already been defined and overriding is disabled.

Action:

Consider renaming one of the beans or enabling overriding by setting spring.main.allow-bean-definition-overriding=true
```

然后重启外置Tomcat，查看Spring容器中的bean，有`InternalResourceViewResolver`，说明可以访问WEB-INF下的文件，果然可以。

我又把目光转向`web.xml`，为什么注释了这段代码就可以了，而且没有`DispatcherServlet`居然没有报错，我在Spring容器中找了一下`DispatcherServlet`。又是自动配置`org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration`，顿时豁然开朗。

随后我取消`web.xml`的注释，启动类`@ImportResource`这行代码注释，随即在启动类加上：

```java
@SpringBootApplication(exclude = DispatcherServletAutoConfiguration.class)
```

然后重启报错了：

```
Parameter 0 of method errorPageCustomizer in org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration required a bean of type 'org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath' that could not be found.
```

解决这个错误，在启动类加上：

```java
@SpringBootApplication(exclude = {
        DispatcherServletAutoConfiguration.class
        ,ErrorMvcAutoConfiguration.class
})
```

重启没有再报错，访问WEB-INF下的文件出现404，错误信息：

```
ERROR 5524 --- [nio-8080-exec-5] o.s.b.w.servlet.support.ErrorPageFilter  : Cannot forward to error page for request [/user/addui] as the response has already been committed. As a result, the response may have the wrong status code. If your application is running on WebSphere Application Server you may be able to resolve this problem by setting com.ibm.ws.webcontainer.invokeFlushAfterService to false
```

最后一句`setting com.ibm.ws.webcontainer.invokeFlushAfterService to false`：

```java
@Bean
public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter errorPageFilter) {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(errorPageFilter);
    filterRegistrationBean.setEnabled(false);
    return filterRegistrationBean;
}
```

DispatcherServlet自动配置报错的问题虽然解决了，但还是访问不了WEB-INF下的文件，能进入Controller，却返回404，有人看到这里可能会问了，前面不是有两种解决方法了吗，加上`view.prefix`和`view.suffix`解决问题不就行了，还把时间浪费在DispatcherServlet上面。

我想说的是，如果硬要使用原来的配置文件，可能就碰不到这种问题，因为有编码的方式代替。也有可能你使用原来的配置文件遇到了另外的问题，仅仅让工程跑起来是不够的。

我还是把目光放在ViewResolver，`springmvc.xml`中的`internalResourceViewResolver`没有注册到容器中。

在`springmvc.xml`文件时，配置的自动扫描是有效的，解析其他的bean也能被注册到容器中，唯独没有`internalResourceViewResolver`，猜想还是自动配置导致的，前面说源码中将InternalResourceViewResolver类型注册为名为`defaultViewResolver`的bean。

修改启动类：

```java
@SpringBootApplication(exclude = {
        DispatcherServletAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class,
        WebMvcAutoConfiguration.class
})
```

最后发现这样写就行了：

```
@SpringBootApplication(exclude = WebMvcAutoConfiguration.class})
```

这样确实解决问题了，自动配置也不是想象中那么好，如果要使用外部配置文件，首先得知道哪些自动配置替换了外部配置文件中的bean。或许以后还是会选择用编码方式代替配置文件。

# Spring Boot 配置静态资源映射

顺便再说一下静态资源的问题，按照我前面的项目文件路径，用Spring Boot的方式启动，是访问不到静态资源的。参考官方文档[Static Content](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-static-content)。

官方文档说的默认静态路径`/static`，`public`，`/resources`，`META-INF/resources`是打包后的路径，参考target目录。

![image](https://img-blog.csdnimg.cn/20200529212246817.png)

不管静态资源放在什么地方，`spring.resources.static-locations`填的路径参考打包后的目录就知道了。
