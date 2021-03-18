package com.project.crowd.api;

import com.project.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 */
// 设置该接口对应的远程调用，指定远程微服务名称
@FeignClient("crowd-redis-provider")
public interface RedisRemoteService {

    /**
     * 远程微服务调用接口：设置key和value
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value
    );

    /**
     * 远程微服务调用接口：设置key和value，以及设置超时时间（过期后删除）
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
    );

    /**
     * 远程微服务调用接口：根据key，获取对应的字符串的value
     * @param key
     * @return
     */
    @RequestMapping("/get/redis/string/value/by/key/remote")
    ResultEntity<String> getRedisStringValueByKeyRemote(
            @RequestParam("key") String key
    );

    /**
     * 远程微服务调用接口：根据key，删除对应的键值对
     * @param key
     * @return
     */
    @RequestMapping("/remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(
            @RequestParam("key") String key
    );


}
