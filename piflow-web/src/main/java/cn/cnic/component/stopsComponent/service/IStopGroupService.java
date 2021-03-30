package cn.cnic.component.stopsComponent.service;

import java.util.List;

import cn.cnic.component.stopsComponent.vo.StopGroupVo;

public interface IStopGroupService {

    public List<StopGroupVo> getStopGroupAll();

    /**
     * Call getAllStops and Group to manage, and save the stop attribute information
     */
    public void updateGroupAndStopsListByServer(String username);
    

    /**
     * stopsComponentList
     * 
     * @param username
     * @param isAdmin
     */
    public String stopsComponentList(String username, boolean isAdmin);

}
