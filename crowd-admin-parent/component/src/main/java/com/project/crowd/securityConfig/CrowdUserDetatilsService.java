package com.project.crowd.securityConfig;

import com.project.crowd.entity.Admin;
import com.project.crowd.entity.Role;
import com.project.crowd.service.api.AdminService;
import com.project.crowd.service.api.AuthService;
import com.project.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
public class CrowdUserDetatilsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    /**
     * 根据账号名称来获取admin对象，以及对应的角色和权限组合的集合，把对象和集合组合的User对象返回，
     * 因为User对象只有账号和密码相关属性，原始Admin对象还有其他属性，所以后续自定义SecurityAdmin对象返回
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1、根据账号名称查询Admin对象
        Admin admin = adminService.getAdminByLoginAcct(username);

        // 2、获取adminId
        Integer adminId = admin.getId();

        // 3、根据adminId查询角色信息
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);

        // 4、根据adminId查询权限信息
        List<String> authNameList = authService.getAssignedAuthNameByAdminId(adminId);

        // 5、创建集合对象用来存储GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 6、遍历assignedRoleList来存入角色信息
        for (Role role : assignedRoleList) {

            String roleName = "ROLE_" + role.getName();
            authorities.add(new SimpleGrantedAuthority(roleName));
        }

        // 7、遍历authNameList来存入权限信息
        for (String authName : authNameList) {

            authorities.add(new SimpleGrantedAuthority(authName));
        }

        // 8、返回封装好的SecurityAdmin对象（继承User的自定义对象）
        return new SecurityAdmin(admin,authorities);
    }
}
