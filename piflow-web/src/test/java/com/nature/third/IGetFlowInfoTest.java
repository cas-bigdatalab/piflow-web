package com.nature.third;

import com.nature.ApplicationTests;
import com.nature.base.util.DateUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.component.flow.model.FlowInfoDb;
import com.nature.mapper.flow.FlowInfoDbMapper;
import com.nature.third.service.IGetFlowInfo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

public class IGetFlowInfoTest extends ApplicationTests {

	@Autowired
	private IGetFlowInfo getFlowInfoImpl;

	@Autowired
	private FlowInfoDbMapper flowInfoDbMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testFlowStop() {
		String appId = "application_1544066083705_0409";
		ThirdFlowInfoVo startFlow2 = getFlowInfoImpl.getFlowInfo(appId);
		logger.info("Test return information：" + startFlow2);
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void testAddFlow() {
		FlowInfoDb db = new FlowInfoDb();
		String appId = "application_1539850523117_0159";
		ThirdFlowInfoVo startFlow2 = getFlowInfoImpl.getFlowInfo(appId);
		logger.info("Test return information：" + startFlow2);
		db.setId(startFlow2.getId());
		db.setName(startFlow2.getName());
		db.setState(startFlow2.getState());
		db.setEndTime(DateUtils.strCstToDate(startFlow2.getEndTime()));
		db.setStartTime(DateUtils.strCstToDate(startFlow2.getStartTime()));
		db.setCrtDttm(new Date());
		db.setCrtUser("wdd");
		db.setEnableFlag(true);
		db.setLastUpdateUser("wdd");
		db.setLastUpdateDttm(new Date());
		flowInfoDbMapper.addFlowInfo(db);
	}
	
	@Test
	public void findFlowInfoDb(){
		FlowInfoDb flowInfoDb = flowInfoDbMapper.flowInfoDb("application_1539850523117_0159");
		if (null != flowInfoDb) {
			logger.info("FlowInfoDb exist：" + flowInfoDb);
		}else {
			logger.info("FlowInfoDb does not exist");
		}
	}
}
