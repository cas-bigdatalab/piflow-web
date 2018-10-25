package com.nature.third;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.third.inf.IGetFlowInfo;
import com.nature.third.vo.flowInfo.FlowInfo;

public class IGetFlowInfoTest extends ApplicationTests {

	@Resource
	private IGetFlowInfo getFlowInfoImpl;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testFlowStop() {
		String appId = "application_1539850523117_0159";
		FlowInfo startFlow2 = getFlowInfoImpl.getFlowInfo(appId);
		logger.info("测试返回信息：" + startFlow2);
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void testAddFlow() {
	}
}
