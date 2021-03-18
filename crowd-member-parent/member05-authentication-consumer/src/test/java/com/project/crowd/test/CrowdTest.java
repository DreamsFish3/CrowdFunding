package com.project.crowd.test;

import com.project.crowd.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CrowdTest {

    private Logger logger = LoggerFactory.getLogger(CrowdTest.class);

    @Test
    public void testSendMessage() {

        // 短信接口调用的URL地址
        String host = "https://gyytz.market.alicloudapi.com";
        // 具体发送短信功能的地址
        String path = "/sms/smsSend";
        // 请求方式
        String method = "POST";
        // 登录阿里云进入控制台找到已购买的短信接口的AppCode
        String appcode = "9ace4c80ae6346fe853993f074782a3e";

        // 封装headers
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        // 封装传输数据
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "13631870196");
        querys.put("param", "**code**:4231,**minute**:5");
        // 签名ID
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        // 模板ID
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");

        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());

            // 获取响应状态码
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            logger.info("状态码："+statusCode);
            // 获取失效原因消息
            String reasonPhrase = statusLine.getReasonPhrase();
            logger.info("响应消息:"+reasonPhrase);

            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            logger.info(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
