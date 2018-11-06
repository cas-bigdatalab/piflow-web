package com.nature.third;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.mapper.FlowInfoDbMapper;
import com.nature.third.inf.IGetFlowProgress;
import com.nature.third.vo.ProgressVo;

public class GetFlowProgressTest extends ApplicationTests {

	@Resource
	private IGetFlowProgress getFlowProgress;
	
	@Autowired
	private FlowInfoDbMapper flowInfoDbMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testFlowStop() {
		ProgressVo jd = null;
		String appId = "application_1540442049798_0095";
		ProgressVo startFlow2 = getFlowProgress.getFlowInfo(appId);
		logger.info("测试返回信息：" + startFlow2);
	}

}
