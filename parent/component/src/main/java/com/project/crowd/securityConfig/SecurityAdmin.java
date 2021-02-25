package com.project.crowd.securityConfig;

import com.project.crowd.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * @description:
 * 因为使用userdetails.User，其中只有“账号”和“密码”的属性，没有原始Admin对象中“邮箱”等属性
 * 所以创建这个类继承User，对User类进行扩展
 */
public class SecurityAdmin extends User {

    // 导入原始Admin对象，包含了Admin对象的全部属性
    @Autowired
    private Admin originalAdmin;

    /**
     * 因为父类没有无参构造器，所以要调用父类的有参构造器
     * @param originalAdmin 传入原始Admin对象
     * @param authorities 创建角色、权限信息的集合
     */
    public SecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities) {

        super(originalAdmin.getLoginAcct(),originalAdmin.getUserPswd(), authorities);

        // 给本类的this.originalAdmin赋值（赋值了才能使用GET/SET方法）
        this.originalAdmin = originalAdmin;

        // 将原始Admin对象中的密码擦除
        this.originalAdmin.setUserPswd(null);
    }

    // 对外提供获取原始Admin对象的get()方法
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
