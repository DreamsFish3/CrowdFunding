package com.project.crowd.test;

import com.project.crowd.MyAnnocationConfiguration;
import com.project.crowd.entity.Employee;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description:
 */
public class SpringTest {

    public static void main(String[] args) {

        // 以前使用new ClassPathXmlApplicationContext("");方式加载XML配置文件

        // 现在基于注解配置类创建IOC容器对象
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(MyAnnocationConfiguration.class);

        // 从IOC容器获取bean
        Employee employee = applicationContext.getBean(Employee.class);
        System.out.println(employee);

        applicationContext.close();
    }
}
