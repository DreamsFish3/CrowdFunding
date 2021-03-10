package com.project.crowd.mvc.interceptor;

import com.project.crowd.constant.CrowdConstant;
import com.project.crowd.entity.Admin;
import com.project.crowd.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @description:
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1、通过request对象获取Session对象
        HttpSession session = request.getSession();

        // 2、尝试从Session域中获取Admin对象
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);

        // 3、判断Admin对象是否为空
        if (admin == null) {
            // 4、为空就抛出访问被禁止的异常
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
        }

        // 5、不为空则返回true，放行
        return true;
    }
}
