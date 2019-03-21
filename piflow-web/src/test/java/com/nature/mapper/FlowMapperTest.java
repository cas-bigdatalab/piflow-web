package com.nature.mapper;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.component.flow.model.Flow;

public class FlowMapperTest extends ApplicationTests {

	@Autowired
	private FlowMapper flowMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetFlowById() {
		Flow flow = flowMapper.getFlowById("85f90a18423245b09cde371cbb333021");
		if (null == flow) {
			logger.info("查询结果为空");
			flow = new Flow();
		}
		logger.info(flow.toString());
	}

	@Test
	@Rollback(true)
	public void testAddFlow() {
		Flow flow = new Flow();
		flow.setId(Utils.getUUID32());
		flow.setCrtUser("Nature");
		flow.setCrtDttm(new Date());
		flow.setLastUpdateUser("Nature");
		flow.setLastUpdateDttm(new Date());
		flow.setEnableFlag(true);
		//flow.setAppId("kongkong");
		flow.setUuid(flow.getId());
		flow.setName("sss");

		int addFlow = flowMapper.addFlow(flow);
		logger.info(addFlow + "");
	}

}
