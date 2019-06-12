package cn.shrmus.spring.app;

import cn.shrmus.spring.service.UserService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 配置文件方式管理对象间的依赖注入关系
 * @Author Shr
 * @CreateDate 2019/6/13 0:02
 * @Version 1.0
 */
public class Demo02 {
    public static void main(String[] args) {
//        Resource resource = new ClassPathResource("applicationContext.xml");
//        BeanFactory beanFactory = new XmlBeanFactory(resource);
//        UserService userService = beanFactory.getBean(UserService.class);
//        userService.getUserInfo();

//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.getUserInfo();
    }
}
