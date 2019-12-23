package cn.shrmus.spring.app;

import cn.shrmus.spring.dao.UserDao;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * BeanFactory直接编码方式管理对象间的依赖注入关系，参考Application
 * @Author Shr
 * @CreateDate 2019/6/12 23:52
 * @Version 1.0
 */
public class Demo01_BeanFactory {
    public static void main(String[] args) throws Exception {
        // 实例化IoC容器 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        GenericBeanDefinition userDaoBeanDefinition = new GenericBeanDefinition();
        userDaoBeanDefinition.setBeanClass(UserDao.class);
        // 将userDao BeanDefinition注册到容器中
        beanFactory.registerBeanDefinition("userDao",userDaoBeanDefinition);

        UserDao userDao = (UserDao) beanFactory.getBean("userDao");
        userDao.execute();
    }
}
