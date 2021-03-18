package com.project.crowd.test;

import com.project.crowd.entity.po.MemberPO;
import com.project.crowd.entity.po.MemberPOExample;
import com.project.crowd.mapper.MemberPOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    private Logger logger = LoggerFactory.getLogger(MyBatisTest.class);

    @Test
    public void testConnection() throws SQLException {

        Connection conn = dataSource.getConnection();

        logger.info(conn.toString());
    }

    @Test
    public void testMapper() {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String source = "123";

        String encode = passwordEncoder.encode(source);

        MemberPO memberPO = new MemberPO(null, "jack", encode, "杰克", "jack@test.com", 1, 1, "捷克", "88888", 2);

        memberPOMapper.insert(memberPO);
    }

    @Test
    public void testDelete() {

        MemberPOExample example = new MemberPOExample();

        MemberPOExample.Criteria criteria = example.createCriteria();

        criteria.andLoginacctLike("jack");

        memberPOMapper.deleteByExample(example);
    }
}
