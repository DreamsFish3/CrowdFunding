package com.project.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @description:
 */
// 开启网关代理功能
@EnableZuulProxy
@SpringBootApplication
public class CrowdMainClass {

    public static void main(String[] args) {

        SpringApplication.run(CrowdMainClass.class, args);
    }
}
