package com.project.crowd.service.api;

import com.github.pagehelper.PageInfo;
import com.project.crowd.entity.Admin;

import java.util.List;

/**
 * @description:
 */
public interface AdminService {

    void saveAdmin(Admin admin);

    List<Admin> getAll();

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    void remove(Integer adminId);

    Admin getAdminById(Integer adminId);

    void update(Admin admin);
}
