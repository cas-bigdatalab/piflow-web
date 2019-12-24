package com.nature.mapper;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SqlUtils;
import com.nature.component.flow.model.Stops;
import com.nature.mapper.flow.StopsMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		// The basic information
		stops.setId(SqlUtils.getUUID32());
		stops.setCrtDttm(new Date());
		stops.setCrtUser("test");
		stops.setEnableFlag(true);
		stops.setLastUpdateDttm(new Date());
		stops.setLastUpdateUser("test");

		// Test stops components
		stops.setName("test_stops_" + num);
		stops.setBundel("Bundel tests stops components" + num);
		stops.setGroups("Groups tests the stops component" + num);
		stops.setOwner("Owner tests stops components" + num);
		stops.setDescription("Desc tests stops components" + num);

		return stops;
	}

}
