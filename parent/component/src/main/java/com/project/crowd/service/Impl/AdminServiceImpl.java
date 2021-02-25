package com.project.crowd.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.crowd.constant.CrowdConstant;
import com.project.crowd.entity.Admin;
import com.project.crowd.entity.AdminExample;
import com.project.crowd.exception.LoginAcctAlreadyInUseForSaveException;
import com.project.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.project.crowd.exception.LoginFailedException;
import com.project.crowd.mapper.AdminMapper;
import com.project.crowd.service.api.AdminService;
import com.project.crowd.util.CrowdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void saveAdmin(Admin admin) {

        // 1、密码加密
        String userPswd = admin.getUserPswd();
        // JDK的md5加密
        // userPswd = CrowdUtil.md5(userPswd);
        // security提供的加密
        userPswd = bCryptPasswordEncoder.encode(userPswd);
        admin.setUserPswd(userPswd);

        // 生成创建时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);

        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果捕获的异常是字段重复异常，抛出用户名重复异常
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseForSaveException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    /**
     * 对请求发送过来的账号和密码，和数据库进行匹配，匹配成功返回Admin对象，否则抛出异常
     * @param loginAcct
     * @param userPswd
     * @return
     */
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

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        // 1、调用PageHelper的开启分页功能方法
        PageHelper.startPage(pageNum, pageSize);

        // 2、执行查询（使用自定义的根据关键字查询的SQL语句）
        List<Admin> list = adminMapper.selectAdminByKeyword(keyword);

        // 3、封装到PageInfo对象中
        return new PageInfo<>(list);
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public void removeList(List<Integer> adminArray) {

        AdminExample example = new AdminExample();
        AdminExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(adminArray);

        adminMapper.deleteByExample(example);
    }

    @Override
    public Admin getAdminById(Integer adminId) {

        return adminMapper.selectByPrimaryKey(adminId);

    }

    @Override
    public void update(Admin admin) {

        // Selective表示有选择的更新，对于null值的字段不更新
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();

            // 如果捕获的异常是字段重复异常，抛出用户名重复异常
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {

        // 先根据adminId删除旧的关联数据
        adminMapper.deleteOldRelationship(adminId);

        // 根据adminId和roleIdList添加新的关联数据
        // 因为roleIdList有可能为空，为空就不需要执行保存了
        if (roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {

        AdminExample example = new AdminExample();

        AdminExample.Criteria criteria = example.createCriteria();

        criteria.andLoginAcctEqualTo(username);

        List<Admin> adminList = adminMapper.selectByExample(example);

        Admin admin = adminList.get(0);

        return admin;
    }




}
