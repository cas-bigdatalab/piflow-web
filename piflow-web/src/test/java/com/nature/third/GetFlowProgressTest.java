package com.nature.third;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.mapper.FlowInfoDbMapper;
import com.nature.third.inf.IGetFlowProgress;
import com.nature.third.vo.flowInfo.FlowInfo;

public class GetFlowProgressTest extends ApplicationTests {

	@Resource
	private IGetFlowProgress getFlowProgress;
	
	@Autowired
	private FlowInfoDbMapper flowInfoDbMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testFlowStop() {
		String appId = "application_1539850523117_0159";
		FlowInfo startFlow2 = getFlowProgress.getFlowInfo(appId);
		logger.info("测试返回信息：" + startFlow2);
	}

}
