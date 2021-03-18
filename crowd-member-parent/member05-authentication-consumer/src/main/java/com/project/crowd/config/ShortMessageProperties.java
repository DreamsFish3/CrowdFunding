package com.project.crowd.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description:该类是为了发送短信函数提供参数的配置文件类，在YML中配置属性值
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix = "short.message")// 设置该类的配置属性的前缀
public class ShortMessageProperties {

    private String host;
    private String path;
    private String method;
    private String appCode;
    private String smsSignId;
    private String templateId;
}
