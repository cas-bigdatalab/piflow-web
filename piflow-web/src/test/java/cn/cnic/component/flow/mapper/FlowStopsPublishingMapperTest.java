package cn.cnic.component.flow.mapper;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.flow.entity.FlowStopsPublishing;
import cn.cnic.component.flow.vo.FlowStopsPublishingVo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FlowStopsPublishingMapperTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    private final FlowStopsPublishingMapper flowStopsPublishingMapper;

    @Autowired
    public FlowStopsPublishingMapperTest(FlowStopsPublishingMapper flowStopsPublishingMapper) {
        this.flowStopsPublishingMapper = flowStopsPublishingMapper;
    }

    @Test
    public void testGetFlowIdByPublishingId() {
        List<String> flowIdByPublishingId = flowStopsPublishingMapper.getFlowIdByPublishingId("bff398355a5c41d8bd784803ac9fc9bb");
        logger.info(flowIdByPublishingId.size() + "");
    }

    @Test
    public void testAddFlowStopsPublishingList() {
        FlowStopsPublishing flowStopsPublishing = new FlowStopsPublishing();
        // The basic information
        flowStopsPublishing.setId(UUIDUtils.getUUID32());
        flowStopsPublishing.setCrtDttm(new Date());
        flowStopsPublishing.setCrtUser("test");
        flowStopsPublishing.setEnableFlag(true);
        flowStopsPublishing.setLastUpdateDttm(new Date());
        flowStopsPublishing.setLastUpdateUser("test");
        // Test stops components
        flowStopsPublishing.setName("test_stops");
        flowStopsPublishing.setPublishingId("XXX");
        flowStopsPublishing.setState("NONE");
        List<String> stopsIdList = new ArrayList<>();
        for (int i = 7; i < 10; i++) {
            stopsIdList.add("stops" + i);
        }
        flowStopsPublishing.setStopsIds(stopsIdList);
        int addFlowStopsPublishingList = flowStopsPublishingMapper.addFlowStopsPublishing(flowStopsPublishing);
        logger.info(addFlowStopsPublishingList + "");
    }

    @Test
    public void testGetStopsFlowStopsPublishingByPublishingId() {
        List<FlowStopsPublishingVo> flowStopsPublishingList = flowStopsPublishingMapper.getFlowStopsPublishingVoByPublishingId("XXX0");
        logger.info(flowStopsPublishingList.size() + "");
    }

}
