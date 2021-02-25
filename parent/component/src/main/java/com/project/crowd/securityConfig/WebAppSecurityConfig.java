package com.project.crowd.securityConfig;

import com.project.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:这个类为SpringSecurity的配置类
 * 1、因为想要在handler中对前端请求使用权限控制，该类就要放在由springMVC扫描包的路径中，
 * 所以该类就会由springMVC的IOC容器管理
 * 2、但是DelegatingFilterProxy只会扫描spring的IOC容器，即ContextLoaderListener加载的容器，
 * 所以会抛出找不到bean的异常
 * 3、要想在handler中对前端请求使用权限控制，只有两种解决方法
 * 解决方案一：
 * 删除ContextLoaderListener这个Listener，把其中的spring配置文件也交个
 * springMVC的IOC容器管理，即DispatcherServlet加载的容器
 * 问题：会破坏同时使用两个IOC容器的结构，不利于后续容器扩展
 * 解决方案二：
 * 修改源码。对org.springframework.web.filter.DelegatingFilterProxy进行修改，
 * 在initFilterBean()中注释掉扫描spring的IOC容器的方法，
 * 和doFilter()中把原来查找spring的IOC容器的函数注释掉
 * 修改为查找springMVC的IOC容器的函数
 */
// 表示当前类是一个配置类
@Configuration
// 启用Web环境下权限控制功能（SpringSecurity）
@EnableWebSecurity
/*下面的注解是启用全局方法权限控制,并设置prePostEnabled = true，
  保证@PreAuthority,@PostAuthority,@PreFilter,@PostFilter生效。
  但是要求该配置类在容器对应扫描包中才能生效，而该配置类是由spring的IOC容器管理的，
  要想在handler包中的方法上使用权限控制注解有两种方法：
  第一种：
        在使用了权限控制注解的方法的类上使用下面的注解开启功能
  第二种：
        在SpringMVC的XML配置文件中开启AOP的注解扫描，
        增加<aop:aspectj-autoproxy proxy-target-class="true"/>
*/
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CrowdUserDetatilsService crowdUserDetatilsService;

    // 把security提供的加密功能装配到SpringMVC的IOC容器中
    // 但是也因为装配在子容器，父容器无法获取，所以直接在Spring的配置文件中装配Bean
    /*@Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 临时使用内存版登录的模式测试代码
        /*auth
                .inMemoryAuthentication()
                .withUser("tom")
                .password("123456")
                .roles("ADMIN")
        ;*/

        auth
                // 装配自定义的数据库的用户查询认证
                .userDetailsService(crowdUserDetatilsService)
                // 装配security提供的加密功能
                .passwordEncoder(bCryptPasswordEncoder)
        ;

    }

    /**
     * 重写父类的方法，设置页面请求授权配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()// 给请求授权
                .antMatchers("/index.jsp")// 针对/index.jsp路径进行授权
                .permitAll() // 设置为无条件访问
                .antMatchers("/admin/to/login/page.html")//针对前往登录页面路径进行授权
                .permitAll()// 设置为无条件访问
                /*给所有静态资源路径都设置为无条件访问*/
                .antMatchers("/bootstrap/**")
                .permitAll()
                .antMatchers("/crowdjs/**")
                .permitAll()
                .antMatchers("/css/**")
                .permitAll()
                .antMatchers("/fonts/**")
                .permitAll()
                .antMatchers("/img/**")
                .permitAll()
                .antMatchers("/jquery/**")
                .permitAll()
                .antMatchers("/layer/**")
                .permitAll()
                .antMatchers("/script/**")
                .permitAll()
                .antMatchers("/ztree/**")
                .permitAll()

                .antMatchers("/admin/get/page.html")
                //.hasRole("经理")// 给访问用户信息路径设置角色授权
                .access("hasRole('经理') or hasAuthority('user:get')")//给访问用户信息路径设置授权

                .anyRequest()// 任意请求（除了上面设置过的请求）
                .authenticated()// 需要验证(登录)后才能访问

                .and()
                .exceptionHandling()// 开启异常处理功能
                // 资源访问失败处理，使用匿名内部类，携带异常信息请求转发到system-error.jsp页面
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletRequest.setAttribute("exception",new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
                        httpServletRequest.getRequestDispatcher("/WEB-INF/system/system-error.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                })

                .and()

                .csrf()
                .disable()// 如果要使用POST请求，就要禁用CSRF功能，因为该功能要求GET请求才能通行，否则就要在每个POST请求中携带csrf参数名称和token值

                .formLogin()// 开启表单登录的功能（给登录表单进行配置）
                .loginPage("/admin/to/login/page.html")// 指定登录页面
                .loginProcessingUrl("/security/do/login.html")// 指定处理登录请求的地址
                .defaultSuccessUrl("/admin/to/main/page.html")// 指定登录成功后前往的地址
                .usernameParameter("loginAcct")// 指定登录账号的请求参数名称
                .passwordParameter("userPswd")// 指定登录密码的请求参数的名称

                .and()

                .logout()// 开启退出登录功能
                .logoutUrl("/security/do/logout.html")// 指定退出登录地址
                .logoutSuccessUrl("/admin/to/login/page.html")// 指定退出成功以后前往的地址
        ;
    }
}
