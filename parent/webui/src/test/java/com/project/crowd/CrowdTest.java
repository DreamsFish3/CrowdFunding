package com.project.crowd;

import com.project.crowd.entity.Admin;
import com.project.crowd.mapper.AdminMapper;
import com.project.crowd.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @description:
 */
//在类上标记必要的注解，Spring整合Junit
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testInsertAdmin() {
        Admin admin = new Admin(null, "tom", "123456", "汤姆", "tom@test.com", null);
        int count = adminMapper.insert(admin);
        System.out.println("受影响的行数：" + count);
    }

    @Test
    public void testLog() {
        // 获取logger对象
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);
        // 根据不用日志级别打印日志
        logger.debug("Hello debug");
        logger.info("Hello info");
        logger.warn("Hello warn");
        logger.error("Hello error");
    }

    @Test
    public void testTx() {
        Admin admin = new Admin(null, "jerry", "654321", "杰瑞", "jerry@test.com", null);
        adminService.saveAdmin(admin);
    }
}
