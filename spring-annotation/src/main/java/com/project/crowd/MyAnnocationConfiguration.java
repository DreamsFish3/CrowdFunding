package com.project.crowd;

import com.project.crowd.entity.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 */
// 该注解表示当前类是一个配置类，作用相当于XML配置文件
@Configuration
public class MyAnnocationConfiguration {

    // <bean id="emp" class="com.project.crowd.entity.Employee"/>

    // @Bean注解相当于上面XML标签的配置，把方法的返回值放入IOC容器
    @Bean
    public Employee getEmployee() {
        return new Employee();
    }
}
