package com.project.crowd.service.api;

import com.project.crowd.entity.po.MemberPO;

/**
 * @description:
 */
public interface MemberService {

    MemberPO getMemberPOByLoginAcct(String loginacct);

    void saveMember(MemberPO memberPO);
}
