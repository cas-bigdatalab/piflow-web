package com.nature.third;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.mapper.FlowInfoDbMapper;
import com.nature.third.inf.IGetFlowInfo;
import com.nature.third.vo.flowInfo.FlowInfo;

public class IGetFlowInfoTest extends ApplicationTests {

	@Resource
	private IGetFlowInfo getFlowInfoImpl;
	
	@Autowired
	private FlowInfoDbMapper flowInfoDbMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testFlowStop() {
		String appId = "application_1540442049798_0057";
		FlowInfo startFlow2 = getFlowInfoImpl.getFlowInfo(appId);
		logger.info("测试返回信息：" + startFlow2);
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void testAddFlow() {
		FlowInfoDb db = new FlowInfoDb();
		String appId = "application_1539850523117_0159";
		FlowInfo startFlow2 = getFlowInfoImpl.getFlowInfo(appId);
		logger.info("测试返回信息：" + startFlow2);
		db.setId(startFlow2.getFlow().getId());
		db.setName(startFlow2.getFlow().getName());
		db.setState(startFlow2.getFlow().getState());
		db.setEndTime(startFlow2.getFlow().getEndTime());
		db.setStartTime(startFlow2.getFlow().getStartTime());
		db.setCrtDttm(new Date());
		db.setCrtUser("wdd");
		db.setVersion(0L);
		db.setEnableFlag(true);
		db.setLastUpdateUser("王栋栋");
		db.setLastUpdateDttm(new Date());
		flowInfoDbMapper.addFlowInfo(db);
	}
	
	@Test
	public void findFlowInfoDb(){
		FlowInfoDb flowInfoDb = flowInfoDbMapper.flowInfoDb("application_1539850523117_0159");
		if (null != flowInfoDb) {
			logger.info("flowInfoDb存在：" + flowInfoDb);
		}else {
			logger.info("flowInfoDb不存在");
		}
	}
}
