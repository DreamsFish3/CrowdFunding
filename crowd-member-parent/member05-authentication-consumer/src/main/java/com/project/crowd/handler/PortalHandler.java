package com.project.crowd.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 */
@Controller
public class PortalHandler {

    /**
     * 设置浏览器的访问绝对路径转发到首页
     * @return
     */
    @RequestMapping("/")
    public String showPortalPage() {

        return "portal";
    }
}
