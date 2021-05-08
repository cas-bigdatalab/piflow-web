package cn.cnic.component.stop.mapper;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.stopsComponent.mapper.StopsComponentGroupMapper;
import cn.cnic.component.stopsComponent.model.StopsComponentGroup;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class StopsComponentGroupMapperTest extends ApplicationTests {

    @Autowired
    private StopsComponentGroupMapper stopsComponentGroupMapper;

    Logger logger = LoggerUtil.getLogger();

    @Test
    public void testGetStopGroupList() {
        List<StopsComponentGroup> stopGroupList = stopsComponentGroupMapper.getStopGroupList();
        if (null == stopGroupList) {
            logger.info("The query result is empty");
        }
        logger.info(stopGroupList.size() + "");
    }

}
