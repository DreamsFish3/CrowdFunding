package com.project.crowd.mapper;

import com.project.crowd.entity.Auth;
import com.project.crowd.entity.AuthExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthMapper {
    int countByExample(AuthExample example);

    int deleteByExample(AuthExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Auth record);

    int insertSelective(Auth record);

    List<Auth> selectByExample(AuthExample example);

    Auth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByExample(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByPrimaryKeySelective(Auth record);

    int updateByPrimaryKey(Auth record);

    // 自定义根据roleId查询关联的AuthId方法
    List<Integer> selectAssignedAuthIdByRoleId(Integer roleId);

    // 自定义根据roleId删除旧的关联数据的方法
    void deleteOldRelationship(Integer roleId);

    // 自定义根据roleId在关联表中保存新的权限关联的方法
    void insertNewRelationship(@Param("roleId") Integer roleId, @Param("authIdList") List<Integer> authIdList);

    // 自定义根据adminId查询关联的AuthName方法
    List<String> selectAssignedAuthNameByAdminId(Integer adminId);
}