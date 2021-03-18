package com.project.crowd.util;

import com.project.crowd.constant.CrowdConstant;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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


    /**
     * 给远程第三方短信接口发送请求，把验证码发送到用户的手机上
     * @param host 短信接口调用的URL地址
     * @param path 具体发送短信功能的地址
     * @param method 请求方式
     * @param phoneNum 接受验证码的手机号
     * @param appCode 用于调用第三方API的AppCode
     * @param smsSignId 签名编号
     * @param templateId 模板编号
     * @return 返回调用结果信息,成功就返回验证码，失败就返回失败消息
     * 状态码：200 正常，400 URL无效，401 appCode错误，403次数用完，500 API网关错误
     */
    public static ResultEntity<String> sendCodeByShortMessage(
            String host,
            String path,
            String method,
            String phoneNum,
            String appCode,
            String smsSignId,
            String templateId
    ) {
        // 生成验证码
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            // Math.random()：随机生成0~1之间的小数，乘以10后就是整数
            int random = (int) (Math.random() * 10);
            builder.append(random);
        }
        String code = builder.toString();

        // 封装消息头
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);

        // 封装传输数据
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phoneNum);
        querys.put("param", "**code**:"+ code +",**minute**:5");
        // 签名ID
        querys.put("smsSignId", smsSignId);
        // 模板ID
        querys.put("templateId", templateId);

        Map<String, String> bodys = new HashMap<String, String>();


        try {
            // 调用第三方的导入的工具类，把封装好的信息发送请求，返回HttpResponse对象
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            //System.out.println(response.toString());

            // 获取响应状态码
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            // 获取失效原因消息
            String reasonPhrase = statusLine.getReasonPhrase();

            if (statusCode == 200) {
                // 状态码为200就返回成功信息和验证码
                return ResultEntity.successWithData(code);
            }
            // 其他状态码就返回失效原因消息
            return ResultEntity.failed(reasonPhrase);

        } catch (Exception e) {
            e.printStackTrace();

            // 系统异常就返回异常信息
            return ResultEntity.failed(e.getMessage());
        }
    }
}
