package com.project.crowd.api;

import com.project.crowd.entity.po.MemberPO;
import com.project.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 */
// 设置该接口对应的远程调用，指定远程微服务名称
@FeignClient("crowd-mysql-provider")
public interface MySQLRemoteService {

    /**
     * 远程微服务接口：根据loginacct获取对应MemberPO对象
     * @param loginacct
     * @return
     */
    @RequestMapping("/get/memberpo/by/loginacct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(
            @RequestParam("loginacct") String loginacct
    );

    /**
     * 远程微服务接口：注册会员信息
     * @param memberPO
     * @return
     */
    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);
}
