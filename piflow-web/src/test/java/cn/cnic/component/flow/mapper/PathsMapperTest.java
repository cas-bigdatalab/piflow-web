package cn.cnic.component.flow.mapper;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.flow.entity.Paths;

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
