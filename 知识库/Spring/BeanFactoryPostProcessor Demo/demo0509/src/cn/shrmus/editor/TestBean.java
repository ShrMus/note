package cn.shrmus.editor;


import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.portlet.context.XmlPortletApplicationContext;

import javax.xml.bind.annotation.XmlRegistry;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestBean {
        public static void main(String[] args) {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new ClassPathResource("/applicationContext.xml"));  // BeanFactory
        // ustomEditorConfigurer是Spring提供的BeanFactoryPostProcessor的一个实现，专门用来搞类型转换的
        CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();
        // 自定义日期转换规则
        DatePropertyEditor datePropertyEditor = new DatePropertyEditor();
        datePropertyEditor.setDatePattern("yyyy-MM-dd");

        Map customerEditors = new HashMap();
        customerEditors.put(java.util.Date.class,datePropertyEditor.getClass());

        customEditorConfigurer.setCustomEditors(customerEditors);
        customEditorConfigurer.postProcessBeanFactory(xmlBeanFactory);

        Entity bean = (Entity) xmlBeanFactory.getBean("entity");
        System.out.println(datePropertyEditor);
        System.out.println(bean.getDate());
    }
//    public static void main(String[] args) {
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
//        CustomEditorConfigurer customEditorConfigurer = (CustomEditorConfigurer) applicationContext.getBean("configurer");
//        System.out.println(customEditorConfigurer);
//        Entity entity = (Entity) applicationContext.getBean("entity");
//        System.out.println(entity.getDate());
//        ApplicationContext
//    }
}
