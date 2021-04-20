package cn.cnic.component.stopsComponent.domain;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.stopsComponent.model.StopsHub;
import cn.cnic.component.stopsComponent.mapper.StopsHubMapper;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class StopsHubDomain {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private StopsHubMapper stopsHubMapper;

    /**
     * add StopsHub
     *
     * @param stopsHub
     * @return
     */
    public int addStopHub(StopsHub stopsHub) {
        return stopsHubMapper.addStopHub(stopsHub);
    }

    /**
     * update StopsHub
     *
     * @param stopsHub
     * @return
     */
    public int updateStopHub(StopsHub stopsHub) {
        return stopsHubMapper.updateStopHub(stopsHub);
    }

    /**
     * query all StopsHub
     *
     * @return
     */
    public List<StopsHub> getStopsHubList(String username, boolean isAdmin) {
        return stopsHubMapper.getStopsHubList(username, isAdmin);
    }

    public List<StopsHub> getStopsHubByName(String username, boolean isAdmin, String jarName) {
        return stopsHubMapper.getStopsHubByName(username, isAdmin, jarName);
    }

    public StopsHub getStopsHubById(String username, boolean isAdmin, String id) {
        return stopsHubMapper.getStopsHubById(username, isAdmin, id);
    }

    public int deleteStopsHubById(String username, String id) {
        return stopsHubMapper.deleteStopsHubById(username, id);
    }

    public List<StopsHub> getStopsHubListParam(String username, boolean isAdmin, String param) {
        return stopsHubMapper.getStopsHubListParam(username, isAdmin, param);
    }


}
