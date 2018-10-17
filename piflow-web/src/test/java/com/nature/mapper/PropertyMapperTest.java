package com.nature.mapper;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;

public class PropertyMapperTest extends ApplicationTests {

	@Autowired
	private PropertyMapper propertyMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetPropertyListByStopsId() {
		List<Property> propertyList = propertyMapper.getPropertyListByStopsId("85f90a18423245b09cde371cbb3330sd");
		if (null == propertyList) {
			logger.info("查询结果为空");
		} else {
			logger.info(propertyList.size() + "");
		}
	}

	@Test
	public void testGetStopsPropertyById() {
		Stops stops = propertyMapper.getStopGroupList("fbb42f0d8ca14a83bfab13e0ba2d7292", "2");
		if (null == stops) {
			logger.info("查询结果为空");
		}
		logger.info(stops.toString() + "         ---------------------     name：" + stops.getName());
	}

	@Test
	public void updateStopsPropertyById() {
		int update = propertyMapper.updateStops("hahah", "12332", "1232", "I'm miaoshuInfo", "1", "8731612e48cc4cc89a24191e737817f2");
		if (0 == update) {
			logger.info("修改失败了"+",影响行数:"+update);
		}else {
			logger.info("修改成功了"+",影响行数:"+update);
		}
	}

	@Test
	public void deleteStopsPropertyById() {
		int delete = propertyMapper.deleteStopsProperty("8731612e42cc4cc89a24191e737817f2");
		if (0 == delete) {
			logger.info("删除失败了"+",影响行数："+delete);
		}else {
			logger.info("删除成功了"+",影响行数："+delete);
		}
	}

}
