package com.nature.mapper;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.stopsComponent.model.StopGroup;
import com.nature.mapper.stopsComponent.StopGroupMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class StopGroupMapperTest extends ApplicationTests {

	@Autowired
	private StopGroupMapper stopGroupMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetStopGroupList() {
		List<StopGroup> stopGroupList = stopGroupMapper.getStopGroupList();
		if (null == stopGroupList) {
			logger.info("The query result is empty");
		}
		logger.info(stopGroupList.size() + "");
	}

}
