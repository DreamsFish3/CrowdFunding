package com.project.crowd.mapper;

import com.project.crowd.entity.Admin;
import com.project.crowd.entity.AdminExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper {
    int countByExample(AdminExample example);

    int deleteByExample(AdminExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    List<Admin> selectByExample(AdminExample example);

    Admin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByExample(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    // 自定义根据关键字搜索查询的方法
    List<Admin> selectAdminByKeyword(String keyword);

    // 自定义根据adminId删除关联表中对应的所有角色关联的方法
    void deleteOldRelationship(Integer adminId);

    // 自定义根据adminId在关联表中保存新的角色关联的方法
    void insertNewRelationship(@Param("adminId") Integer adminId, @Param("roleIdList") List<Integer> roleIdList);
}