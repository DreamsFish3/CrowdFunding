package com.project.crowd.service.api;

import com.project.crowd.entity.Admin;

import java.util.List;

/**
 * @description:
 */
public interface AdminService {

    void saveAdmin(Admin admin);

    List<Admin> getAll();

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);
}
