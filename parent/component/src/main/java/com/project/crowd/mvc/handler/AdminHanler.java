package com.project.crowd.mvc.handler;

import com.project.crowd.constant.CrowdConstant;
import com.project.crowd.entity.Admin;
import com.project.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * @description:
 */
@Controller
public class AdminHanler {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct, @RequestParam("userPswd") String userPswd, HttpSession session) {
        // 1、调用Service方法执行登录检查
        // 这个方法如果能够返回admin对象说明登录成功，如果账号、密码不正确则抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);

        //  2、把成功返回的admin对象存入session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);

        // 3、重定向跳转视图到主页面
        return "redirect:/admin/to/main/page.html";
    }

    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session) {
        // 强制Session失效
        session.invalidate();

        return "redirect:/admin/to/login/page.html";
    }
}
