package com.nature.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.Stops;

public class StopMapperTest extends ApplicationTests {

	@Autowired
	private StopsMapper stopMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetStopsAll() {
		List<Stops> stopsAll = stopMapper.getStopsAll();
		logger.info(stopsAll + "");
	}

	@Test
	public void testGetStopsByFlowId() {
		List<Stops> stopsAll = stopMapper.getStopsListByFlowId("85f90a18423245b09cde371cbb333021");
		logger.info(stopsAll + "");
	}

	@Test
	public void testAddStopsAll() {
		List<Stops> setStops = new ArrayList<>();
		for (int i = 7; i < 10; i++) {
			Stops stops = setStops(i + "");
			setStops.add(stops);
		}
		int addStopsAll = stopMapper.addStopsList(setStops);
		logger.info(addStopsAll + "");
	}

	private Stops setStops(String num) {
		Stops stops = new Stops();
		// 基本信息
		stops.setId(Utils.getUUID32());
		stops.setCrtDttm(new Date());
		stops.setCrtUser("test");
		stops.setEnableFlag(true);
		stops.setLastUpdateDttm(new Date());
		stops.setLastUpdateUser("test");
		stops.setVersion(0L);

		// 测试stops组件
		stops.setName("test_stops_" + num);
		stops.setBundel("Bundel测试stops组件" + num);
		stops.setGroups("Groups测试stops组件" + num);
		stops.setOwner("Owner测试stops组件" + num);
		stops.setDescription("Desc测试stops组件" + num);

		return stops;
	}

}
