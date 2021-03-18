package com.project.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:该类为用户注册提交表单的实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {

    private String loginacct;

    private String userpswd;

    private String username;

    private String email;

    private String phoneNum;

    private String code;
}
