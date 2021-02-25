package com.project.security.service;

import com.project.security.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据表单提交的用户名查询User对象，并装配角色、权限等信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(
            // 表单提交的用户名
            String username
    ) throws UsernameNotFoundException {

        // 1、从数据库查询Admin对象（使用spring的jdbcTemplate操作数据库）
        String sql = "SELECT id,login_acct,user_pswd,user_name,email FROM `t_admin` where login_acct=?";
        List<Admin> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Admin.class), username);
        Admin admin = list.get(0);

        // 2、给Admin设置角色权限信息
        // 如果使用list传入SimpleGrantedAuthority对象，传入的角色要有“ROLE_”前缀
        // 而使用AuthorityUtils.createAuthorityList创建的list，传入就角色就不需要前缀
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("UPDATE"));
        authorities.add(new SimpleGrantedAuthority("ROLE_学徒"));

        // 3、把Admin对象和authorties封装到UserDetails中
        String userPswd = admin.getUserPswd();
        return new User(username,userPswd,authorities);
    }
}
