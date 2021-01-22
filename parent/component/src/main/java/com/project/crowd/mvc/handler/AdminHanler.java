package com.project.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import com.project.crowd.constant.CrowdConstant;
import com.project.crowd.entity.Admin;
import com.project.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @description:
 */
@Controller
public class AdminHanler {

    @Autowired
    private AdminService adminService;

    /**
     * 对登录页面发送过来的账号和密码进行检查是否放行
     * @param loginAcct
     * @param userPswd
     * @param session
     * @return
     */
    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct, @RequestParam("userPswd") String userPswd, HttpSession session) {
        // 1、调用Service方法执行登录检查
        // 这个方法如果能够返回admin对象说明登录成功，如果账号、密码不正确则抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);

        //  2、把成功返回的admin对象存入session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);

        // 3、重定向跳转视图到主页面
        return "redirect:/admin/to/main/page.html";
    }

    /**
     * 退出登录状态
     * @param session
     * @return
     */
    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session) {
        // 强制Session失效
        session.invalidate();

        return "redirect:/admin/to/login/page.html";
    }

    /**
     * 管理员分页查询和搜索查询
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param modelMap
     * @return
     */
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            //如果没有关键字，设置默认值为空串
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            ModelMap modelMap) {

        // 调用Service方法获取PageInfo对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);

        // 把PageInfo对象存入模型
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);

        return "admin-page";
    }

    /**
     * 使用普通请求的删除数据
     * @param adminId
     * @param pageNum
     * @param keyword
     * @return
     */
    /*@RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(
            @PathVariable(value = "adminId") Integer adminId,
            @PathVariable(value = "pageNum") Integer pageNum,
            @PathVariable(value = "keyword") String keyword) {

        // 执行删除
        adminService.remove(adminId);

        // 错误一：没有携带数据，直接转发不显示数据
        //return "admin-";

        // 错误二：请求转发刷新页面后，又进行了一次删除操作，浪费性能
        //return "forward:/admin/get/page.html";

        // 正确：带参数进行重定向
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }*/

    /**
     * 使用了ajax请求的删除数据
     * @param adminId
     */
    @ResponseBody
    @RequestMapping("/admin/remove/page.html")
    public void remove2(@RequestParam("id") Integer adminId) {

        adminService.remove(adminId);

    }

    /**
     * 新建用户数据
     * @param admin
     * @return
     */
    @RequestMapping("/admin/save.html")
    public String save(Admin admin) {

        adminService.saveAdmin(admin);

        // 重定向到最后一页
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }

    /**
     * 从用户查询页面跳转到用户更新页面
     * @param adminId
     * @param modelMap
     * @return
     */
    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap
    ) {

        // 根据id查询Admin对象
        Admin admin = adminService.getAdminById(adminId);

        // 将Admin对象存入模型
        modelMap.addAttribute("admin", admin);

        // 跳转到更新页面
        return "admin-edit";
    }

    /**
     * 使用普通请求更新用户信息
     * @param admin
     * @param pageNum
     * @param keyword
     * @return
     */
    /*@RequestMapping("/admin/update.html")
    public String update(
            Admin admin,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword
    ) {

        adminService.update(admin);

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }*/

    /**
     * 使用Ajax请求更新用户信息
     * @param admin
     */
    @ResponseBody
    @RequestMapping("/admin/update.html")
    public void update(Admin admin) {

        adminService.update(admin);


    }
}
