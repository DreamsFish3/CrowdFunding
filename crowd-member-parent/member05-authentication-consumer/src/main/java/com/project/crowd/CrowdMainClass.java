package com.project.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description:
 */
// 启用Feign客户端功能
@EnableFeignClients
@SpringBootApplication
public class CrowdMainClass {

    public static void main(String[] args) {

        SpringApplication.run(CrowdMainClass.class, args);
    }
}
