package cn.cnic.component.stopsComponent.service;

import cn.cnic.base.vo.UserVo;
import cn.cnic.component.stopsComponent.vo.StopGroupVo;

import java.util.List;

public interface IStopGroupService {

    public List<StopGroupVo> getStopGroupAll();

    /**
     * Call getAllStops and Group to manage, and save the stop attribute information
     */
    public void updateGroupAndStopsListByServer(String username);

}
