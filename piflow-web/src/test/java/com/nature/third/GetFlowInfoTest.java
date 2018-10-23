package com.nature.third;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;

public class GetFlowInfoTest extends ApplicationTests {

	@Resource
	private GetFlowInfo getFlowInfo;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testFlowStop() {
		String appId = "application_1539850523117_0103";
		String startFlow2 = getFlowInfo.flowInfo(appId);
		logger.info("测试返回信息：" + startFlow2);
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void testAddFlow() {
	}
}
