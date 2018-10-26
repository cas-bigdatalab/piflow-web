package com.nature.component.workFlow.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.Flow;

public class FlowServiceTest extends ApplicationTests {

	@Autowired
	private FlowService flowService;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetFlowById() {
		Flow flow = flowService.getFlowById("85f90a18423245b09cde371cbb333021");
		if (null == flow) {
			logger.info("查询结果为空");
			flow = new Flow();
		}
		logger.info(flow.toString());
	}

	@Test
	@Rollback(false)
	public void testAddFlow() {
		Flow flow = new Flow();
		flow.setId(Utils.getUUID32());
		//flow.setAppId("kongkong");
		flow.setCrtUser("Nature");
		flow.setLastUpdateUser("Nature");
		flow.setUuid(flow.getId());
		flow.setName("testFlow");
		flow.setVersion(0L);
		// StatefulRtnBase addFlow = flowService.addFlow(MxGraphModel mxGraphModel,
		// String flowId);
		// logger.info(addFlow + "");
	}
}
