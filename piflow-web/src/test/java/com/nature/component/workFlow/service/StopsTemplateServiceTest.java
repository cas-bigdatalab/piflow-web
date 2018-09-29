package com.nature.component.workFlow.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.model.StopsTemplate;

public class StopsTemplateServiceTest extends ApplicationTests {

	@Autowired
	private StopsTemplateService stopsTemplateService;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetStopsTemplateById() {
		StopsTemplate stopsTemplate = stopsTemplateService.getStopsTemplateById("fbb42f0d8ca14a83bfab13e0ba2d7293");
		if (null == stopsTemplate) {
			logger.info("查询结果为空");
			stopsTemplate = new StopsTemplate();
		}
		logger.info(stopsTemplate.toString());
	}

	@Test
	public void testGetStopsPropertyById() {
		StopsTemplate stopsTemplate = stopsTemplateService.getStopsPropertyById("fbb42f0d8ca14a83bfab13e0ba2d7293");
		if (null == stopsTemplate) {
			logger.info("查询结果为空");
			stopsTemplate = new StopsTemplate();
		}
		logger.info(stopsTemplate.toString());
	}

}
