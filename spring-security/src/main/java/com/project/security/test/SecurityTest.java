package com.project.security.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @description:
 */
public class SecurityTest {

    public static void main(String[] args) {

        // 创建BCryptPasswordEncoder对象
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 准备一个明文密码
        String rawPassword = "456";

        // 加密
        String encodePassword = passwordEncoder.encode(rawPassword);


        System.out.println(encodePassword);
    }

}
