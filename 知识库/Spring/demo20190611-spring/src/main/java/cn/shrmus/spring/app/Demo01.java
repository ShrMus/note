package cn.shrmus.spring.app;

import cn.shrmus.spring.dao.UserDao;
import cn.shrmus.spring.service.UserService;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.StaticApplicationContext;

/**
 * 直接编码方式管理对象间的依赖注入关系
 * @Author Shr
 * @CreateDate 2019/6/12 23:52
 * @Version 1.0
 */
public class Demo01 {
    public static void main(String[] args) throws Exception {
        UserDao userDao = new UserDao();
        UserService userService = new UserService(userDao);
//        ConfigurableListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//
//        beanFactory.registerSingleton("userDao",userDao);
//
//        beanFactory.registerSingleton("userService",userService);
//
//        UserService getuserService = (UserService) beanFactory.getBean("userService");
//        getuserService.getUserInfo();

        StaticApplicationContext applicationContext = new StaticApplicationContext();
        applicationContext.registerSingleton("userDao", UserDao.class);
        applicationContext.registerSingleton("userService",UserService.class);

        UserService getuserService = (UserService) applicationContext.getBean("userService");
        getuserService.getUserInfo();
    }
}
