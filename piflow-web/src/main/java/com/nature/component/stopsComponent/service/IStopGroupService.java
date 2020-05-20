package com.nature.component.stopsComponent.service;

import com.nature.base.vo.UserVo;
import com.nature.component.stopsComponent.vo.StopGroupVo;

import java.util.List;

public interface IStopGroupService {

    public List<StopGroupVo> getStopGroupAll();

    /**
     * Call getAllStops and Group to manage, and save the stop attribute information
     */
    public void addGroupAndStopsList(UserVo user);

}
