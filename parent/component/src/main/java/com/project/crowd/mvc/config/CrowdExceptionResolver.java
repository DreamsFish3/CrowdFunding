package com.project.crowd.mvc.config;

import com.google.gson.Gson;
import com.project.crowd.constant.CrowdConstant;
import com.project.crowd.exception.AccessForbiddenException;
import com.project.crowd.exception.LoginAcctAlreadyInUseForSaveException;
import com.project.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.project.crowd.exception.LoginFailedException;
import com.project.crowd.util.CrowdUtil;
import com.project.crowd.util.ResultEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:处理异常映射关系
 * 除了对捕获到的异常进行映射分类后处理，还判断是否为Ajax请求，
 * 把异常信息写入响应体返回，或者返回跳转视图
 */
//@ControllerAdvice表示当前类是一个基于注解的异常处理类
@ControllerAdvice
public class CrowdExceptionResolver {

    /**
     * 该方法关联的异常类型为空指针异常
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(NullPointerException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 直接设置好要跳转的视图名称，再调用通用方法处理异常
        String viewName = "system/system-error";
        return commonResolve(viewName, exception, request, response);
    }

    /**
     * 该方法关联的异常类型为数学异常
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView resolveMathException(ArithmeticException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 直接设置好要跳转的视图名称，再调用通用方法处理异常
        String viewName = "system/system-error";
        return commonResolve(viewName, exception, request, response);
    }

    /**
     * 该方法关联的异常类型为自定义的登录失败异常
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 直接设置好要跳转的视图名称，再调用通用方法处理异常
        String viewName = "admin/admin-login";
        return commonResolve(viewName, exception, request, response);
    }

    /**
     * 该方法关联的异常类型为自定义的用户没有登录就访问保护资源的异常
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = AccessForbiddenException.class)
    public ModelAndView resolveAccessForbiddenException(AccessForbiddenException exception,HttpServletRequest request,HttpServletResponse response) throws IOException {
        // 直接设置好要跳转的视图名称，再调用通用方法处理异常
        String viewName = "admin/admin-login";
        return commonResolve(viewName, exception, request, response);
    }

    /**
     * 该方法关联的异常类型为自定义的保存用户时的用户名重复异常
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = LoginAcctAlreadyInUseForSaveException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForSaveException(LoginAcctAlreadyInUseForSaveException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 直接设置好要跳转的视图名称，再调用通用方法处理异常
        String viewName = "admin/admin-add";
        return commonResolve(viewName, exception, request, response);
    }

    /**
     * 该方法关联的异常类型为自定义的更新用户时的用户名重复异常
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(LoginAcctAlreadyInUseForUpdateException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String viewName = "system/system-error";
        return commonResolve(viewName, exception, request, response);
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveException(Exception exception,HttpServletRequest request,HttpServletResponse response) throws IOException {
        // 直接设置好要跳转的视图名称，再调用通用方法处理异常
        String viewName = "admin/admin-login";
        return commonResolve(viewName, exception, request, response);
    }


    /**
     * 通用的异常处理方法
     * 先判断是否为Ajax请求，如果是Ajax请求就把异常信息写入响应体，如果不是就返回视图
     * @param viewName 跳转的视图
     * @param exception 捕获的异常
     * @param request 当前请求对象
     * @param response 当前响应对象
     * @return
     * @throws IOException
     */
    private ModelAndView commonResolve(String viewName,Exception exception,HttpServletRequest request,HttpServletResponse response) throws IOException {
        // 判断请求类型
        boolean judgeResult = CrowdUtil.judgeRequestType(request);

        // 判断是否为Ajax请求
        if (judgeResult) {
            // 创建ResultEntity对象，调用ResultEntity中的failed方法，向前端返回异常消息
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());

            // 把ResultEntity转为json字符串
            Gson gson = new Gson();
            String json = gson.toJson(resultEntity);

            // 把json字符串作为响应体返回给前端
            response.getWriter().write(json);

            // 由于上面已经通过原生的response对象返回了响应，所以不提供ModelAndView对象
            return null;
        }

        // 如果不是Ajax请求则创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();

        // 在把Exception对象存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);

        // 设置对应视图
        modelAndView.setViewName(viewName);
        return modelAndView;
    }
}
