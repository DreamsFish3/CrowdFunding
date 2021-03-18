package com.project.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:该类为用户登录后回显信息的实体类
 * 因为是存入到session域对象中，再存到Redis，所以需要序列化
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginVO implements Serializable {

    private Integer id;

    private String username;

    private String email;
}
