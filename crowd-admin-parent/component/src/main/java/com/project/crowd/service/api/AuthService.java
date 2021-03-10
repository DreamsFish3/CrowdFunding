package com.project.crowd.service.api;

import com.project.crowd.entity.Auth;

import java.util.List;
import java.util.Map;

/**
 * @description:
 */
public interface AuthService {

    List<Auth> getAll();

    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    void saveRoleAuthRalationship(Map<String, List<Integer>> map);

    List<String> getAssignedAuthNameByAdminId(Integer adminId);
}
