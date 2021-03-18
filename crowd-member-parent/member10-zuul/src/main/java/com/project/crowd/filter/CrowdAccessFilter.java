package com.project.crowd.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.project.crowd.constant.CrowdConstant;
import com.project.crowd.constant.ReleaseResources;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @description:该类为网关对访问路径进行过滤的过滤器配置类
 */
@Component
public class CrowdAccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        // 返回pre，表示该filter在目标微服务前执行
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 获取zuul当前的RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();

        // 通过RequestContext对象获取当前请求对象（框架底层是借助ThreadLocal从当前线程上获取事先绑定的Request对象，因为每个请求对应一个线程）
        HttpServletRequest request = requestContext.getRequest();

        // 获取ServletPaht的值（请求路径）
        String servletPath = request.getServletPath();

        // 判断当前ServletPaht是否属于放行请求路径
        boolean containsResult = ReleaseResources.PASS_REQUEST_SET.contains(servletPath);
        // 如果属于，就返回false，进行放行
        if (containsResult) {
            return false;
        }
        
        // 判断当前ServletPaht是否属于放行静态资源
        boolean judgeStaticResult = ReleaseResources.judgeCurrentServletPathWhetherStaticResource(servletPath);
        // 如果属于，就进行放行,所以取反结果返回
        return !judgeStaticResult;
    }

    @Override
    public Object run() throws ZuulException {

        // 获取当前请求对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // 获取当前Session对象
        HttpSession session = request.getSession();

        // 尝试从Session对象中获取已登录的用户的对象
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        // 判断loginMember是否为空，如果为空，表示没有登录
        if (loginMember == null) {
            // 将提示消息存入sessoin域中
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_ACCESS_FORBIDEN);

            // 从requestContext对象中获取Response对象
            HttpServletResponse response = requestContext.getResponse();
            // 重定向到member05-authentication-consumer工程中的登录页面
            try {
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
