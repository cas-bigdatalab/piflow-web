package com.nature.mapper;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.model.StopsTemplate;

public class StopsTemplateMapperTest extends ApplicationTests {

	@Autowired
	private StopsTemplateMapper stopsTemplateMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetStopsTemplateById() {
		StopsTemplate stopsTemplate = stopsTemplateMapper.getStopsTemplateById("fbb42f0d8ca14a83bfab13e0ba2d7293");
		if (null == stopsTemplate) {
			logger.info("查询结果为空");
			stopsTemplate = new StopsTemplate();
		}
		logger.info(stopsTemplate.toString());
	}

	@Test
	public void testGetStopsPropertyById() {
		StopsTemplate stopsTemplate = stopsTemplateMapper.getStopsPropertyById("fbb42f0d8ca14a83bfab13e0ba2d7293");
		if (null == stopsTemplate) {
			logger.info("查询结果为空");
			stopsTemplate = new StopsTemplate();
		}
		logger.info(stopsTemplate.toString());
	}

	@Test
	public void testGetStopsTemplateListByGroupId() {
		List<StopsTemplate> stopsTemplateList = stopsTemplateMapper
				.getStopsTemplateListByGroupId("fbb42f0d8ca14a83bfab13e0ba2d7290");
		if (null == stopsTemplateList) {
			logger.info("查询结果为空");
		}
		logger.info(stopsTemplateList.size() + "");
	}

}
