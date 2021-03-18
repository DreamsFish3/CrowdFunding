package com.project.crowd.handler;

import com.project.crowd.api.MySQLRemoteService;
import com.project.crowd.api.RedisRemoteService;
import com.project.crowd.config.ShortMessageProperties;
import com.project.crowd.constant.CrowdConstant;
import com.project.crowd.entity.po.MemberPO;
import com.project.crowd.entity.vo.MemberLoginVO;
import com.project.crowd.entity.vo.MemberVO;
import com.project.crowd.util.CrowdUtil;
import com.project.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 */
@Controller
public class MemberHandler {

    @Autowired
    private ShortMessageProperties properties;

    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    /**
     * 发送验证码
     * （该函数在注册用户时收集所需属性，向手机用户发送短信验证码，并对短信发送结果进行判断）
     * @param phoneNum
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.josn")
    public ResultEntity<String> sendMessage(
            @RequestParam("phoneNum") String phoneNum
    ) {

        // 发送验证码到phoneNum的手机（把配置文件相关属性一样发送）
        ResultEntity<String> sendMessageResultEntity = CrowdUtil.sendCodeByShortMessage(
                properties.getHost(),
                properties.getPath(),
                properties.getMethod(),
                phoneNum,
                properties.getAppCode(),
                properties.getSmsSignId(),
                properties.getTemplateId()
        );

        // 判断短信发送结果
        if (ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())) {
            // 获取收到的验证码
            String code = sendMessageResultEntity.getData();
            // 设置键
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
            // 如果发送成功就把验证码存入Redis
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, 15, TimeUnit.MINUTES);

            // 判断是否保存成功
            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
                // 如皋保存成功就返回结果提示
                return ResultEntity.successWithoutData();
            } else {
                // 如果保存失败就直接返回结果
                return saveCodeResultEntity;
            }
        } else {
            // 如果发送失败就直接返回结果
            return sendMessageResultEntity;
        }
    }

    /**
     * 注册会员账号
     * （该函数先从页面过来的MemberVO对象进行验证码比对，一致后对密码进行加密，
     * 复制属性改为MemberPO对象保存到数据库）
     * @param memberVO
     * @param modelMap
     * @return
     */
    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {

        // 获取用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();

        // 拼Redis中存储验证码的Key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

        // 从Redis读取Key对应的Value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKeyRemote(key);

        // 检查查询操作是否有效
        // 如果操作失败，返回异常信息，刷新页面
        if (ResultEntity.FAILED.equals(resultEntity.getResult())) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }

        // 返回的结果集的数据就是要获取的Value，即发送的验证码
        String redisCode = resultEntity.getData();
        // 如果查询到验证码为null，返回异常信息，刷新页面
        if (redisCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }

        // 用户输入的验证码
        String formCode = memberVO.getCode();
        // 如果从Redis中能够查询到的Value，就和用户输入的验证码进行比较
        // 如果验证码不一致，返回异常信息，刷新页面
        if (!Objects.equals(redisCode, formCode)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);
            return "member-reg";
        }
        // 验证码一致，则从Redis删除该键值对
        redisRemoteService.removeRedisKeyRemote(key);

        // 执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(memberVO.getUserpswd());
        memberVO.setUserpswd(encode);

        // 调用接口执行远程方法，进行注册会员操作
        // 1、创建空的MemberPO对象
        MemberPO memberPO = new MemberPO();
        // 2、复制属性
        BeanUtils.copyProperties(memberVO,memberPO);
        // 3、调用远程方法
        ResultEntity<String> saveResultEntity = mySQLRemoteService.saveMember(memberPO);
        // 4、判断执行保存操作是否成功，失败就返回异常信息，刷新页面
        if (ResultEntity.FAILED.equals(saveResultEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }

        // 注册完毕，重定向到登录页面
        return "redirect:/auth/member/to/login/page";
    }

    /**
     * 登录会员账号
     * （根据loginacct查询MemberPO对象，然后比对密码，一致后，把需要回显的属性封装为MemberLoginVO对象返回）
     * @param loginacct
     * @param userpswd
     * @param modelMap
     * @param session
     * @return
     */
    @RequestMapping("/auth/member/do/login")
    public String login(
            @RequestParam("loginacct") String loginacct,
            @RequestParam("userpswd") String userpswd,
            ModelMap modelMap,
            HttpSession session
    ) {

        // 调用远程接口，根据登录账号查询MemberPO对象
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);

        // 如果查询失败就返回异常信息，刷新页面
        if (ResultEntity.FAILED.equals(resultEntity.getResult())) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-login";
        }

        MemberPO memberPO = resultEntity.getData();

        // 如果查询的MemberPO为空，返回账号密码错误信息，刷新页面
        if (memberPO == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        // 比对密码
        // 1、获取数据库密码
        String memberPOUserpswd = memberPO.getUserpswd();
        // 2、跟表单密码进行比较
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(userpswd, memberPOUserpswd);
        // 3、如果密码不一致，返回账号密码错误信息，刷新页面
        if (!matches) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        // 4、密码一致后，创建MemberLoginVO对象存入Session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,memberLoginVO);
        return "redirect:/auth/member/to/center/page";
    }

    /**
     *  会员退出登录
     * @param session
     * @return
     */
    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session) {
        // 清空session域
        session.invalidate();
        // 返回首页
        return "redirect:/";
    }
}

