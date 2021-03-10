package com.project.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import com.project.crowd.entity.Role;
import com.project.crowd.service.api.RoleService;
import com.project.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 */
//@Controller
//@ResponseBody
@RestController
public class RoleHandler {

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @return
     */
    //@ResponseBody
    @RequestMapping("/role/get/page/info.json")
    @PreAuthorize("hasRole('部长') or hasAuthority('role:get')")// 设置角色授权
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        // 调用service方法执行分页查询
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);

        // 执行成功就返回数据
        /**
         * 注意：执行失败不再需要try catch，因为springMVC已经帮我们捕获异常，
         *      通过@ControllerAdvice所指示的异常处理类，进行异常处理,
         *      CrowdExceptionResolver就是我们设置异常处理类，
         *      其中设置如果是Ajax请求就把异常信息放入响应体进行返回。
         */
        return ResultEntity.successWithData(pageInfo);
    }

    /**
     * 保存角色
     * @param role
     * @return
     */
    //@ResponseBody
    @RequestMapping("/role/save.json")
    public ResultEntity<String> saveRole(Role role) {

        roleService.saveRole(role);

        return ResultEntity.successWithoutData();
    }

    /**
     * 更新角色
     * @param role
     * @return
     */
    //@ResponseBody
    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role) {

        roleService.updateRole(role);

        return ResultEntity.successWithoutData();
    }

    /**
     * 整合单条删除和批量删除
     * @param roleIdList
     * @return
     */
    //@ResponseBody
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> removeByRoleIdArray(@RequestBody List<Integer> roleIdList) {

        roleService.removeRole(roleIdList);

        return ResultEntity.successWithoutData();
    }
}
