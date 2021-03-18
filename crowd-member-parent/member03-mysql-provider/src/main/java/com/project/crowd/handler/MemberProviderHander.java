package com.project.crowd.handler;

import com.project.crowd.constant.CrowdConstant;
import com.project.crowd.entity.po.MemberPO;
import com.project.crowd.service.api.MemberService;
import com.project.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 */
@RestController
public class MemberProviderHander {

    @Autowired
    private MemberService memberService;

    /**
     * 根据loginacct来查询对应的MemberPO对象
     * @param loginacct
     * @return
     */
    @RequestMapping("/get/memberpo/by/loginacct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(
            @RequestParam("loginacct") String loginacct
    ) {

        try {
            // 调用本地Service执行查询
            MemberPO memberPO = memberService.getMemberPOByLoginAcct(loginacct);
            // 成功执行就返回成功信息和数据
            return ResultEntity.successWithData(memberPO);
        } catch (Exception e) {
            e.printStackTrace();
            // 失败就返回异常信息
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 注册会员信息
     * @param memberPO
     * @return
     */
    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO) {

        try {
            memberService.saveMember(memberPO);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {

            // 判断是否为重复字段异常
            if (e instanceof DuplicateKeyException) {
                //t_member表中loginacct是唯一字段，可以通过这里来向前段返回异常，向用户返回信息
                return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}
