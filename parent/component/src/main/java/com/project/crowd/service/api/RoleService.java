package com.project.crowd.service.api;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.project.crowd.entity.Role;

import java.util.List;

/**
 * @description:
 */
public interface RoleService {

    PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword);

    void saveRole(Role role);

    void updateRole(Role role);

    void removeRole(List<Integer> roleIdList);

    List<Role> getAssignedRole(Integer adminId);

    List<Role> getUnAssignedRole(Integer adminId);
}
