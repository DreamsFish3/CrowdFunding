package com.project.security.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @description:security的实现接口加密密码方式（已过时）
 */
@Component
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {

        try {
            // 创建使用“MD5”的MessageDigest对象
            String algorithm = "MD5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            // 获取rawPassword的字节数组
            byte[] input = ((String) rawPassword).getBytes();

            // 加密
            byte[] ouput = messageDigest.digest(input);

            // 转为16进制的字符便于存储（“1”表示整数）
            String encoded = new BigInteger(1, ouput).toString(16);

            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        // 对明文密码进行加密处理
        String formEncode = encode(rawPassword);

        // 返回跟数据库的密文密码进行比较的结果
        return Objects.equals(formEncode,encodedPassword);
    }
}
