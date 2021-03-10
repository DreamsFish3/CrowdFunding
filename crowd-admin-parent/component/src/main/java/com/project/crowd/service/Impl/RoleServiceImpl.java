package com.project.crowd.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.crowd.entity.Role;
import com.project.crowd.entity.RoleExample;
import com.project.crowd.mapper.RoleMapper;
import com.project.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {

        // 1、开启分页功能
        PageHelper.startPage(pageNum, pageSize);

        // 2、执行查询（使用自定义的根据关键字查询的SQL语句）
        List<Role> roleList = roleMapper.selectRoleByKeyword(keyword);

        // 3、封装为PageInfo对象返回
        return new PageInfo<>(roleList);
    }

    @Override
    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public void removeRole(List<Integer> roleIdList) {

        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        //delete from t_role where id in ${roleIdList}
        criteria.andIdIn(roleIdList);

        roleMapper.deleteByExample(example);
    }

    @Override
    public List<Role> getAssignedRole(Integer adminId) {

        // 调用自定义的查询方法（使用自定义的查询已分配角色的SQL语句）
        return roleMapper.selectAssignedRole(adminId);
    }

    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {

        // 调用自定义的查询方法（使用自定义的查询未分配角色的SQL语句）
        return roleMapper.selectUnAssignedRole(adminId);
    }
}
