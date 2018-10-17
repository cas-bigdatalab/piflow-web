package com.nature.mapper;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.model.Paths;

public class PathsMapperTest extends ApplicationTests {

	@Autowired
	private PathsMapper pathsMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetPathsListByFlowId() {
		List<Paths> pathsList = pathsMapper.getPathsListByFlowId("497d2b3a5b1d4e2da4c8a372779babd5");
		if (null == pathsList) {
			logger.info("查询结果为空");
		} else {
			logger.info(pathsList.size() + "");
		}
	}

}
