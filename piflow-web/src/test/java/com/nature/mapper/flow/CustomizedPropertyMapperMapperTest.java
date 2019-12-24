package com.nature.mapper.flow;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.flow.model.CustomizedProperty;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;

public class CustomizedPropertyMapperMapperTest extends ApplicationTests {

	@Resource
	private CustomizedPropertyMapper customizedPropertyMapper;


	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetPropertyListByStopsId() {
		List<CustomizedProperty> customizedPropertyListByStopsId = customizedPropertyMapper.getCustomizedPropertyListByStopsId("66cb0f08e4964d94912b8503f06b73fa");
		if (null == customizedPropertyListByStopsId) {
			logger.info("The query result is empty");
		} else {
			logger.info(customizedPropertyListByStopsId.size() + "");
		}
	}



}
