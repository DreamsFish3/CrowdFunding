package com.project.crowd.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @description:该测试类为测试整合Redis
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    private Logger logger = LoggerFactory.getLogger(RedisTest.class);

    // springboot专门为redis准备的string类型的Template
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testSet() {

        // 获取用来操作string类型数据的ValueOperations对象
        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        // 参数：key，value
        operations.set("test","redis");

        String realValue = operations.get("test");
        System.out.println("value="+realValue);
    }

    @Test
    public void testExSet() {

        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        // 参数：key，value，时间数值，时间单位
        operations.set("extest","redistime",5000, TimeUnit.SECONDS);
    }
}
