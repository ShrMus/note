package cn.shrmus.editor;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.beans.support.ResourceEditorRegistrar;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.Map;

public class TestBean {
        public static void main(String[] args) {
                XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new ClassPathResource("/applicationContext.xml"));  // BeanFactory
                // ustomEditorConfigurer是Spring提供的BeanFactoryPostProcessor的一个实现，专门用来搞类型转换的
//                PropertiesEditor propertiesEditor = new PropertiesEditor();
                CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();

                DatePropertyEditor datePropertyEditor = new DatePropertyEditor();
                datePropertyEditor.setDatePattern("yyyy-MM-dd");

//                Map customerEditors = new HashMap();
//                customerEditors.put(java.util.Date.class,datePropertyEditor.getClass());

                // 自定义日期转换规则
                PropertyEditorRegistrar propertyEditorRegistrar = new PropertyEditorRegistrar(){
                        @Override
                        public void registerCustomEditors(PropertyEditorRegistry propertyEditorRegistry){
                                propertyEditorRegistry.registerCustomEditor(java.util.Date.class,datePropertyEditor);
                        }
                };
                customEditorConfigurer.setPropertyEditorRegistrars(new PropertyEditorRegistrar[]{propertyEditorRegistrar});
//                customEditorConfigurer.setCustomEditors(customerEditors);
                customEditorConfigurer.postProcessBeanFactory(xmlBeanFactory);

                Map map2 = xmlBeanFactory.getCustomEditors();
                Map map = xmlBeanFactory.getBeansOfType(DatePropertyEditor.class);

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

//        public static void main(String[] args) throws Exception{
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(null);
//                Date dateValue = simpleDateFormat.parse("2019-05-11");
//        }
}
