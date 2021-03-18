package com.project.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 */
// 扫描MyBatis的Mapper接口所在的包
@MapperScan("com.project.crowd.mapper")
// 设置当前类为SpringBoot的启动类
@SpringBootApplication
public class CrowdMainClass {

    public static void main(String[] args) {

        SpringApplication.run(CrowdMainClass.class, args);
    }
}
