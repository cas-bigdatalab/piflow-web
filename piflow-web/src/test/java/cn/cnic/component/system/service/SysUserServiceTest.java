package cn.cnic.component.system.service;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class SysUserServiceTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ISysUserService sysUserServiceImpl;

    @Test
    public void testGetFlowById() {
        String userName = sysUserServiceImpl.checkUserName("admin");
        logger.info(userName);
    }
}
