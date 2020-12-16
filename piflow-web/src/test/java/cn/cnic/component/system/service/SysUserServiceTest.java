package cn.cnic.component.system.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;

public class SysUserServiceTest extends ApplicationTests {

	@Autowired
	private ISysUserService sysUserServiceImpl;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetFlowById() {
		String userName = sysUserServiceImpl.checkUserName("admin");
		logger.info(userName);
	}
}
