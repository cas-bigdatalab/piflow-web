package com.nature.mapper;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.flow.model.Paths;
import com.nature.mapper.flow.PathsMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PathsMapperTest extends ApplicationTests {

	@Autowired
	private PathsMapper pathsMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetPathsListByFlowId() {
		List<Paths> pathsList = pathsMapper.getPathsListByFlowId("497d2b3a5b1d4e2da4c8a372779babd5");
		if (null == pathsList) {
			logger.info("The query result is empty");
		} else {
			logger.info(pathsList.size() + "");
		}
	}

}
