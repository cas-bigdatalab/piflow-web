package cn.cnic.component.stopsComponent.service;

import cn.cnic.controller.requestVo.UpdatestopsComponentIsShow;

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
    public String updateStopsComponentsIsShow(String username, boolean isAdmin, UpdatestopsComponentIsShow[] stopsManageList) throws Exception;
    
    /**
     * updateStopsComponentsIsShow
     * 
     * @param username
     * @param isAdmin
     * @param stopsManage
     * @return
     * @throws Exception
     */
    public String updateStopsComponentIsShow(String username, boolean isAdmin, UpdatestopsComponentIsShow stopsManage) throws Exception;

}
