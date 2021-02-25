package com.project.crowd.mapper;

import com.project.crowd.entity.Role;
import com.project.crowd.entity.RoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    // 自定义根据关键字搜索查询的方法
    List<Role> selectRoleByKeyword(String keyword);

    // 自定义的查询已分配角色的查询方法
    List<Role> selectAssignedRole(Integer adminId);

    // 自定义的查询未分配角色的查询方法
    List<Role> selectUnAssignedRole(Integer adminId);
}