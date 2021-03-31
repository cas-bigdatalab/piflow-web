package cn.cnic.component.stopsComponent.service;

import cn.cnic.component.stopsComponent.vo.StopsComponentManageVo;

public interface IStopsComponentManageService {

    /**
     * updateStopsComponentsIsShow
     * 
     * @param username
     * @param isAdmin
     * @param stopsManageList
     * @return
     * @throws Exception
     */
    public String updateStopsComponentsIsShow(String username, boolean isAdmin, StopsComponentManageVo[] stopsManageList) throws Exception;
    
    /**
     * updateStopsComponentsIsShow
     * 
     * @param username
     * @param isAdmin
     * @param stopsManageList
     * @return
     * @throws Exception
     */
    public String updateStopsComponentIsShow(String username, boolean isAdmin, StopsComponentManageVo stopsManage) throws Exception;

}
