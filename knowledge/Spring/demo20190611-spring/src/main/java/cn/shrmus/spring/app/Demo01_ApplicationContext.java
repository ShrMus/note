package cn.shrmus.spring.app;

import cn.shrmus.spring.dao.UserDao;
import cn.shrmus.spring.service.UserService;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;

/**
 * ApplicationContext直接编码方式管理对象间的依赖注入关系
 * @Author Shr
 * @CreateDate 2019/6/12 23:52
 * @Version 1.0
 */
public class Demo01_ApplicationContext {
    public static void main(String[] args) throws Exception {
        // 实例化IoC容器 ApplicationContext
        StaticApplicationContext applicationContext = new StaticApplicationContext();

        // 实例化userDao BeanDefinition
        GenericBeanDefinition userDaoBeanDefinition = new GenericBeanDefinition();
        userDaoBeanDefinition.setBeanClass(UserDao.class);

        // 将userDao BeanDefinition注册到容器中
        applicationContext.registerBeanDefinition("userDao",userDaoBeanDefinition);

        // 实例化userService BeanDefinition
        GenericBeanDefinition userServiceBeanDefinition = new GenericBeanDefinition();
        userServiceBeanDefinition.setBeanClass(UserService.class);

        // 将userService BeanDefinition注册到容器中
        applicationContext.registerBeanDefinition("userService",userServiceBeanDefinition);

        // 构造方法注入
//        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
//        constructorArgumentValues.addGenericArgumentValue(userDaoBeanDefinition);
//        userServiceBeanDefinition.setConstructorArgumentValues(constructorArgumentValues);

        // setter方式注入
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        PropertyValue propertyValue = new PropertyValue("userDao",userDaoBeanDefinition);
        propertyValues.addPropertyValue(propertyValue);
        userServiceBeanDefinition.setPropertyValues(propertyValues);


        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.execute();
    }
}
