package cn.cnic.component.stop.mapper;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.stopsComponent.mapper.StopsComponentGroupMapper;
import cn.cnic.component.stopsComponent.entity.StopsComponentGroup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class StopsComponentGroupMapperTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    private final StopsComponentGroupMapper stopsComponentGroupMapper;

    @Autowired
    public StopsComponentGroupMapperTest(StopsComponentGroupMapper stopsComponentGroupMapper) {
        this.stopsComponentGroupMapper = stopsComponentGroupMapper;
    }

    @Test
    public void testGetStopGroupList() {
        List<StopsComponentGroup> stopGroupList = stopsComponentGroupMapper.getStopGroupList();
        if (null == stopGroupList) {
            logger.info("The query result is empty");
        }
        logger.info(stopGroupList.size() + "");
    }

}
