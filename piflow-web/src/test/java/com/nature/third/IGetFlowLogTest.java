package com.nature.third;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.third.inf.IGetFlowLog;
import com.nature.third.vo.flowLog.FlowLog;

public class IGetFlowLogTest extends ApplicationTests {

	@Resource
	private IGetFlowLog getFlowLogImpl;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetFlowLog() {
		String appId = "application_1539850523117_0159";
		FlowLog flowLog = getFlowLogImpl.getFlowLog(appId);
		if (null != flowLog) {
			flowLog.getApp();
		}
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void testAddFlow() {
	}

}
