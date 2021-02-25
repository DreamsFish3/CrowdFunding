package com.project.security.config;

import com.project.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * @description:
 */
// 讲当前类标记为配置类
@Configuration
// 启用Web环境下权限控制功能
@EnableWebSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private MyPasswordEncoder myPasswordEncoder;

    /*@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;*/

    @Bean
    public BCryptPasswordEncoder getbCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 重写父类的方法，设置页面请求授权配置
     * @param security
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity security) throws Exception {

        /*
        在数据库中专门建一个表用于存储"记录我"的登录用户数据，
        就算重启服务器，浏览器也能读取数据自动登录，
        如果点击退出登录，数据库的数据也会删除
         */
        // 创建令牌存储库对象
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        //装配数据源
        tokenRepository.setDataSource(dataSource);

        // 链式调用编程
        security
                .authorizeRequests()// 对请求进行授权
                .antMatchers("/index.jsp")// 针对/index.jsp路径进行授权
                .permitAll() // 设置为无条件访问
                .antMatchers("/layui/**")// 针对/layui目录下所有资源进行授权
                .permitAll()// 设置为无条件访问
                .antMatchers("/level1/**")// 针对/level1/**路径设置访问要求
                .hasRole("学徒")// 要求用户具备“学徒”角色才可以访问
                .antMatchers("/level2/**")// 针对/level2/**路径设置访问要求
                .hasAuthority("内门弟子")// 要求用户具备“内门弟子”权限才可以访问

                .and()//而且

                .authorizeRequests()// 对请求进行授权
                .anyRequest()// 任意请求（除了上面设置过的请求）
                .authenticated()// 需要验证(登录)后才能访问

                .and()// 而且

                .formLogin()// 使用表单形式登录
                .loginPage("/index.jsp")// 指定登录页面（如果没有指定，会使用security自带的登录页面）
                /*
                因为loginPage("/index.jsp")设置值后，
                登录提交表单、登录失败和退出登录的地址也同时修改为"/index.jsp加后缀"
                /index.jsp GET：前往登录页面
                /index.jsp POST：登录提交表单
                /index.jsp?error GET：登录失败
                /index.jsp?logout GET：退出登录
                所以其他三个请求的地址要重新设置
                 */
                .loginProcessingUrl("/do/login.html")// 指定登录页面提交表单的地址

                .usernameParameter("loginAcct")// 重新设置登录账号的请求参数名
                .passwordParameter("userPswd")// 重新设置登录密码的请求参数名
                .defaultSuccessUrl("/main.html")// 登录成功前往的地址

                .and()

                /*.csrf()
                .disable()// 如果要使用POST请求，就要禁用CSRF功能，因为该功能要求GET请求才能通行*/
                .logout()// 开启退出登录功能
                .logoutUrl("/do/logout.html")// 设置退出登录地址
                .logoutSuccessUrl("/index.jsp")// 设置退出登录成功地址

                .and()

                .exceptionHandling()// 开启异常处理器
                //.accessDeniedPage("/to/no/auth/page.html")// （没有对应角色或权限）访问被拒绝时前往的页面
                // 或者设置访问被拒绝的handler，创建匿名内部类，重写handle方法,进行更详细的处理
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletRequest.setAttribute("message","☆抱歉！您无法访问这个资源！☆");
                        httpServletRequest.getRequestDispatcher("/WEB-INF/views/no_auth.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                })

                .and()

                .rememberMe()// 开启“记住我”功能(用于浏览器保存登录信息，重开浏览器不需要重新登录)
                .tokenRepository(tokenRepository)// 开启令牌存储库功能（每次调用，都会存储用户信息到数据库中的一个专门的表中存储起来）

                ;
    }

    /**
     * 重写父类的方法，设置身份验证配置
     * @param builder
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

        /*builder
                .inMemoryAuthentication()//（身份验证）在内存中完成账号、密码的检查
                .withUser("tom")// 指定账号
                .password("123")// 指定密码
                .roles("ADMIN","学徒")// 指定当前用户的角色

                .and()

                .withUser("jerry")// 指定账号
                .password("456")// 指定密码
                .authorities("UPDATE","内门弟子")// 指定当前用户的权限
                ;*/

        builder
                // 装配自定义的数据库的用户查询认证
                .userDetailsService(myUserDetailsService)
                /*// 装配自定义的密码比较认证（已过时）
                .passwordEncoder(myPasswordEncoder);*/
                // 装配security提供的加密功能
                .passwordEncoder(getbCryptPasswordEncoder())
        ;

    }
}
