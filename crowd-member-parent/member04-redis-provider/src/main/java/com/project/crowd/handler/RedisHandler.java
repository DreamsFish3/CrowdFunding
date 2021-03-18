package com.project.crowd.handler;

import com.project.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 */
@RestController
public class RedisHandler {

    // springboot专门为redis准备的string类型的Template
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 设置key和value
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value
    ) {

        try {
            // 获取用来操作string类型数据的ValueOperations对象
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            // 设置key和value
            operations.set(key,value);
            // 成功就返回成功信息
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            // 失败就返回异常信息
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 设置key和value，以及设置超时时间
     * @param key
     * @param value
     * @param time 时间数值
     * @param timeUnit 时间单位
     * @return
     */
    @RequestMapping("/set/redis/key/value/remote/with/timeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") long time,
            @RequestParam("timeUnit") TimeUnit timeUnit
    ) {
        try {
            // 获取用来操作string类型数据的ValueOperations对象
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            // 设置key、value、时间数值，时间单位
            operations.set(key,value,time,timeUnit);
            // 返回是否成功的信息
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            // 返回异常信息
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据key，获取对应的字符串的value
     * @param key
     * @return
     */
    @RequestMapping("/get/redis/string/value/by/key/remote")
    ResultEntity<String> getRedisStringValueByKeyRemote(
            @RequestParam("key") String key
    ) {

        try {
            // 获取用来操作string类型数据的ValueOperations对象
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            // 获取value
            String value = operations.get(key);

            // 返回是否成功的信息和数据
            return ResultEntity.successWithData(value);

        } catch (Exception e) {
            e.printStackTrace();
            // 返回异常信息
            return ResultEntity.failed(e.getMessage());
        }

    }

    /**
     * 根据key，删除对应的键值对
     * @param key
     * @return
     */
    @RequestMapping("/remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(
            @RequestParam("key") String key
    ) {

        try {
            // 删除键值对
            Boolean result = redisTemplate.delete(key);

            // 返回是否成功的信息
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            // 返回异常信息
            return ResultEntity.failed(e.getMessage());
        }
    }
}
