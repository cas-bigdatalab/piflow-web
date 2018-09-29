package com.nature.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.model.StopGroup;
import com.nature.component.workFlow.model.StopsTemplate;

public class StopGroupMapperTest extends ApplicationTests {

	@Autowired
	private StopGroupMapper stopGroupMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetStopGroupList() {
		List<StopGroup> stopGroupList = stopGroupMapper.getStopGroupList();
		if (null == stopGroupList) {
			logger.info("查询结果为空");
		}
		logger.info(stopGroupList.size() + "");
	}

	private List<StopGroup> getStopGroupTemplate() {
		// 测试stops组件1
		StopsTemplate stopsTemplate1 = new StopsTemplate();
		stopsTemplate1.setName("test_stops_Components_1");
		stopsTemplate1.setBundel("测试stops组件1");
		stopsTemplate1.setGroups("测试stops组件1");
		stopsTemplate1.setOwner("测试stops组件1");
		stopsTemplate1.setDescription("测试stops组件1");
		stopsTemplate1.setNumberOfEntrances(1);
		stopsTemplate1.setNumberOfExports(1);
		// 测试stops组件2
		StopsTemplate stopsTemplate2 = new StopsTemplate();
		stopsTemplate2.setName("test_stops_Components_2");
		stopsTemplate2.setBundel("测试stops组件2");
		stopsTemplate2.setGroups("测试stops组件2");
		stopsTemplate2.setOwner("测试stops组件2");
		stopsTemplate2.setDescription("测试stops组件2");
		stopsTemplate2.setNumberOfEntrances(1);
		stopsTemplate2.setNumberOfExports(1);
		// 测试stops组件3
		StopsTemplate stopsTemplate3 = new StopsTemplate();
		stopsTemplate3.setName("test_stops_Components_3");
		stopsTemplate3.setBundel("测试stops组件3");
		stopsTemplate3.setGroups("测试stops组件3");
		stopsTemplate3.setOwner("测试stops组件3");
		stopsTemplate3.setDescription("测试stops组件3");
		stopsTemplate3.setNumberOfEntrances(1);
		stopsTemplate3.setNumberOfExports(1);

		List<StopGroup> groupsList = new ArrayList<StopGroup>();
		StopGroup stopGroup = null;
		List<StopsTemplate> stopsTemplateList = null;
		// 测试一组
		stopGroup = new StopGroup();
		stopsTemplateList = new ArrayList<StopsTemplate>();
		stopGroup.setGroupName("test_group_1");
		stopsTemplateList.add(stopsTemplate1);
		stopsTemplateList.add(stopsTemplate2);
		stopGroup.setStopsTemplateList(stopsTemplateList);
		groupsList.add(stopGroup);
		// 测试二组
		stopGroup = new StopGroup();
		stopsTemplateList = new ArrayList<StopsTemplate>();
		stopGroup.setGroupName("Test_group_2");
		stopsTemplateList.add(stopsTemplate1);
		stopsTemplateList.add(stopsTemplate3);
		stopGroup.setStopsTemplateList(stopsTemplateList);
		groupsList.add(stopGroup);
		// 测试三组
		stopGroup = new StopGroup();
		stopsTemplateList = new ArrayList<StopsTemplate>();
		stopGroup.setGroupName("Test_group_3");
		stopsTemplateList.add(stopsTemplate2);
		stopsTemplateList.add(stopsTemplate3);
		stopGroup.setStopsTemplateList(stopsTemplateList);
		groupsList.add(stopGroup);
		return groupsList;
	}

}
