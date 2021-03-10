package com.project.crowd;

import com.project.crowd.util.CrowdUtil;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @description:
 */
public class StringTest {

    /**
     * 测试JDK自带的MD5加密
     */
    @Test
    public void testMd5() {

        String source = "123";

        String encoded = CrowdUtil.md5(source);

        System.out.println(encoded);

    }

    /**
     * 测试springSecurity的BCryptPasswordEncoder加密
     */
    @Test
    public void testBcpe() {

        String source = "123456";

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String encode = bCryptPasswordEncoder.encode(source);

        System.out.println(encode);

    }
}
