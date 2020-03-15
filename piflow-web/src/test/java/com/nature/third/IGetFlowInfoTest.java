package com.nature.third;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.third.service.IGetFlowInfo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class IGetFlowInfoTest extends ApplicationTests {

	@Autowired
	private IGetFlowInfo getFlowInfoImpl;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testFlowStop() {
		String appId = "application_1544066083705_0409";
		ThirdFlowInfoVo startFlow2 = getFlowInfoImpl.getFlowInfo(appId);
		logger.info("Test return information：" + startFlow2);
	}

}
