package cn.cnic.mapper;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.stopsComponent.mapper.StopGroupMapper;
import cn.cnic.component.stopsComponent.model.StopsComponentGroup;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class StopGroupMapperTest extends ApplicationTests {

	@Autowired
	private StopGroupMapper stopGroupMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetStopGroupList() {
		List<StopsComponentGroup> stopGroupList = stopGroupMapper.getStopGroupList();
		if (null == stopGroupList) {
			logger.info("The query result is empty");
		}
		logger.info(stopGroupList.size() + "");
	}

}
