package com.project.crowd.mvc.handler;

import com.project.crowd.entity.Admin;
import com.project.crowd.entity.Student;
import com.project.crowd.service.api.AdminService;
import com.project.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:
 */
@Controller
public class TestHandler {

    @Autowired
    private AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @RequestMapping("/test/ssm.html")
    public String testSSM(ModelMap modelMap, HttpServletRequest request) {

        List<Admin> adminList = adminService.getAll();

        modelMap.addAttribute("adminList", adminList);

        // 测试异常
        /*System.out.println(10 / 0);*/

        // 测试判断请求类型
        /*boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult=" + judgeResult);*/

        // 测试空指针异常
        /*String a = null;
        System.out.println(a.length());*/

        return "target";
    }

    /*
    * 方案一结论：不建议使用，获取前段数据的数组，"array"后面要加"[]"
    * */
    @ResponseBody//把方法的返回值"success"封装返回（不参与视图解析器）
    @RequestMapping("send/array/one.html")
    public String testReceiveArrayOne(@RequestParam("array[]") List<Integer> array) {
        for (Integer number : array) {
            System.out.println("number="+number);
        }
        return "success";
    }

    /*
    * 方案二结论：不成立，并且"array"加上"[]"也不能运行
    * */
    @ResponseBody
    @RequestMapping("send/array/two.html")
    public String testReceiveArrayTwo(@RequestParam("array") List<Integer> array) {
        for (Integer number : array) {
            System.out.println("number=" + number);
        }
        return "success";
    }

    /*
    * 方案三结论：
    * 成立,要前端发数组过来之前先准备好json数组（或者json对象），
    * 并且转为json字符串
    * 将 JSON字符串直接赋值给 data 属性 "data":arrayStr
    * 必须要设置 contentType:"application/json;charset=UTF-8"
    * */
    @ResponseBody
    @RequestMapping("send/array/three.html")
    public String testReceiveArrayThree(@RequestBody List<Integer> array) {
        for (Integer number : array) {
            System.out.println("number=" + number);
        }
        return "success";
    }


    /**
     * 接收Ajax请求发送过来的JSON对象，并且使用工具类统一数据后发送响应
     * @param student
     * @return
     */
    @ResponseBody
    @RequestMapping("send/compose/object.json")
    public ResultEntity<Student> testReceiveComposeObject(@RequestBody Student student,HttpServletRequest request) {

        // 测试判断请求类型
        /*boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult=" + judgeResult);*/

        // 测试空指针异常
        /*String a = null;
        System.out.println(a.length());*/

        logger.info(student.toString());
        // 把发送回去的student对象封装到ResultEntity中返回
        return ResultEntity.successWithData(student);
    }
}
