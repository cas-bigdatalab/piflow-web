package com.nature.third;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.mapper.flow.FlowInfoDbMapper;
import com.nature.third.inf.IGetFlowProgress;
import com.nature.third.vo.flow.ThirdProgressVo;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class GetFlowProgressTest extends ApplicationTests {

	@Resource
	private IGetFlowProgress getFlowProgress;
	
	@Autowired
	private FlowInfoDbMapper flowInfoDbMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testFlowStop() {
		ThirdProgressVo jd = null;
		String appId = "application_1540442049798_0095";
		ThirdProgressVo startFlow2 = getFlowProgress.getFlowProgress(appId);
		logger.info("测试返回信息：" + startFlow2);
	}

}
