package com.project.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: 重定向后的视图跳转配置类
 * 该类相当于SpringMVC配置XML中的<mvc:view-controller path=""/>，只负责对页面进行重定向
 *
 */
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // 浏览器访问地址（前往会员注册页面）
        String urlPath = "/auth/member/to/reg/page";
        // 跳转的视图名称
        String viewName = "member-reg";
        // 添加view-controller
        registry.addViewController(urlPath).setViewName(viewName);

        // 重定向前往登录页面
        registry.addViewController("/auth/member/to/login/page").setViewName("member-login");
        // 重定向到会员中心页面
        registry.addViewController("/auth/member/to/center/page").setViewName("member-center");
    }
}
