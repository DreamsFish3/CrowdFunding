package com.project.crowd.util;

import com.project.crowd.constant.CrowdConstant;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description:
 */
public class CrowdUtil {

    /**
     * 根据请求头信息判断请求类型是否为Ajax请求
     *
     * @param request
     * @return true就是Ajax请求，false就不是
     */
    public static boolean judgeRequestType(HttpServletRequest request) {

        // 获取请求消息头
        String acceptHeader = request.getHeader("Accept");
        String xRequestHeader = request.getHeader("X-Requested-With");

        // 判断
        return (
                (acceptHeader != null && acceptHeader.contains("application/json"))

                        ||

                        (xRequestHeader != null && xRequestHeader.equals("XMLHttpRequest"))
        );
    }

    /**
     * 对明文字符串进行md5加密
     * @param source 传入的明文字符串
     * @return 加密后的结果
     */
    public static String md5(String source) {

        // 判断source是否有效
        if (source == null || source.length() == 0) {
            //如果不是有效字符串，抛出异常信息
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        try {
            // 获取MessageDigest对象，使用JDK提供的md5加密
            String algorithm = "md5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            // 获取明文字符串对应的字节数组
            byte[] input = source.getBytes();

            //执行加密
            byte[] output = messageDigest.digest(input);

            //创建BigInteger对象,把字节数组转为整数
            int signum = 1;// 1表示整数
            BigInteger bigInteger = new BigInteger(signum, output);

            //按照16进制讲bigInteger的值转换为字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix);

            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
