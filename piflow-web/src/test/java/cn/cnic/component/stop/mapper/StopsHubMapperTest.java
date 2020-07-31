package cn.cnic.component.stop.mapper;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.common.Eunm.StopsHubState;
import cn.cnic.component.stopsComponent.model.StopsHub;
import cn.cnic.component.stopsComponent.mapper.StopsHubMapper;
import org.junit.Test;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public class StopsHubMapperTest extends ApplicationTests {

    @Resource
    private StopsHubMapper stopsHubMapper;

    Logger logger = LoggerUtil.getLogger();

    @Test
    public void testAddStopsHub() {

        StopsHub stopsHub = new StopsHub();
        stopsHub.setId("222");
        stopsHub.setCrtUser("Nature");
        stopsHub.setCrtDttm(new Date());
        stopsHub.setLastUpdateUser("Nature");
        stopsHub.setLastUpdateDttm(new Date());
        stopsHub.setEnableFlag(true);
        stopsHub.setVersion(1L);
        stopsHub.setJarName("sks-piflow.jar");
        stopsHub.setJarUrl("/data/piflowServer/class/sks-piflow.jar");
        stopsHub.setStatus(StopsHubState.UNMOUNT);
        int result = stopsHubMapper.addStopHub(stopsHub);
        if (1 == result) {
            logger.info("The query result is empty");
        }
        logger.info(stopsHub.toString());
    }

    @Test
    public void testUpdateStopsHub() {

        StopsHub stopsHub = new StopsHub();
        stopsHub.setId("d6850e11266b42c9bdeab7595921a647");
        stopsHub.setCrtUser("Nature");
        stopsHub.setCrtDttm(new Date());
        stopsHub.setLastUpdateUser("Nature");
        stopsHub.setLastUpdateDttm(new Date());
        stopsHub.setEnableFlag(true);
        stopsHub.setVersion(1L);
        stopsHub.setJarName("piflowexternal1.jar");
        stopsHub.setJarUrl("/data/piflowServer/class/piflowexternal1.jar");
        stopsHub.setStatus(StopsHubState.UNMOUNT);
        int result = stopsHubMapper.updateStopHub(stopsHub);
        if (1 == result) {
            logger.info("The query result is empty");
        }
        logger.info(stopsHub.toString());
    }

    @Test
    public void testGetAllStopsHub() {
        List<StopsHub> result = stopsHubMapper.getStopsHubList("Nature",false);
        for(int i=0; i<result.size(); i++){
            StopsHub s = result.get(i);
            logger.info(s.toString());
        }
    }

    @Test
    public void testGetStopsHubByName() {
        List<StopsHub> result = stopsHubMapper.getStopsHubByName("Nature",false, "piflowexternal");
        for(int i=0; i<result.size(); i++){
            StopsHub s = result.get(i);
            logger.info(s.toString());
        }
    }

    @Test
    public void testGetStopsHubById() {
        StopsHub s = stopsHubMapper.getStopsHubById("Nature",false, "d6850e11266b42c9bdeab7595921a647");
        if(s != null)
         logger.info(s.toString());
        else
            logger.info("No StopsHub found!");
    }

    @Test
    public void testDeleteStopsHub() {
        int result = stopsHubMapper.deleteStopsHubById("Nature","1afa70ebe67e446bafe76b3626408575");

        logger.info("Delete StopsHub result: " + result);

    }
}
