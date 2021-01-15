package com.project.crowd.service.Impl;

import com.project.crowd.constant.CrowdConstant;
import com.project.crowd.entity.Admin;
import com.project.crowd.entity.AdminExample;
import com.project.crowd.exception.LoginFailedException;
import com.project.crowd.mapper.AdminMapper;
import com.project.crowd.service.api.AdminService;
import com.project.crowd.util.CrowdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public void saveAdmin(Admin admin) {
        adminMapper.insert(admin);
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {

        // 1、根据登录账号查询Admin对象
        // 创建AdminExample对象
        AdminExample adminExample = new AdminExample();
        // 创建Criteria对象
        AdminExample.Criteria criteria = adminExample.createCriteria();
        // 把查询条件封装到Criteria对象中
        criteria.andLoginAcctEqualTo(loginAcct);
        // 执行查询方法
        List<Admin> list = adminMapper.selectByExample(adminExample);

        // 2、判断Admin对象是否为null
        //返回的list为null抛出登录异常
        if (list == null || list.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 如果list有多个值，则抛出系统异常
        if (list.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }

        // 获取admin对象
        Admin admin = list.get(0);
        // 3、如果Admin对象为null则抛出异常
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 4、如果Admin对象不为null则将数据库密码从Admin对象中取出
        String userPswdDB = admin.getUserPswd();

        // 5、将表单提交的明文密码进行加密
        String userPswdFrom = CrowdUtil.md5(userPswd);

        // 6、对密码进行比较
        if (!Objects.equals(userPswdDB, userPswdFrom)) {
            // 7、如果比较结果是不一致则抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 8、如果一致则返回Admin对象
        return admin;
    }
}
