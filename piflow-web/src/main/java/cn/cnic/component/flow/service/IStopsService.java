package cn.cnic.component.flow.service;


import java.util.List;

import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.controller.requestVo.RunStopsVo;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;

public interface IStopsService {

    public int deleteStopsByFlowId(String id);

    /**
     * Query "stops" based on "flowId" and "pagesId"
     *
     * @param flowId  Required
     * @param pageIds Can be empty
     * @return
     */
    public List<StopsVo> getStopsByFlowIdAndPageIds(String flowId, String[] pageIds);

    /**
     * update stop
     *
     * @param stopsVo
     * @return
     */
    public Integer stopsUpdate(String username, StopsVo stopsVo) throws Exception ;

    /**
     * Modify the "stops" individual fields returned by the interface
     *
     * @param stopVo
     * @return
     */
    public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo stopVo);

    /**
     * Modify the isCheckpoint field
     *
     * @param stopId
     * @param isCheckpointStr
     * @return
     * @throws Exception 
     */
    public String updateStopsCheckpoint(String username, String stopId, String isCheckpointStr) throws Exception;

    /**
     * Modify "stopName" based on id
     *
     * @param id
     * @param stopName
     * @return
     * @throws Exception 
     */
    public int updateStopsNameById(String username, String id, String stopName) throws Exception ;

    /**
     * getStopByNameAndFlowId
     * 
     * @param flowId
     * @param stopName
     * @return
     */
    public String getStopByNameAndFlowId(String flowId, String stopName);

    /**
     * updateStopName
     * 
     * @param username
     * @param isAdmin
     * @param stopId
     * @param flowId
     * @param stopName
     * @param pageId
     * @return
     * @throws Exception 
     */
    public String updateStopName(String username, boolean isAdmin, String stopId, String flowId, String stopName, String pageId) throws Exception;

    public String getStopsPort(String flowId, String sourceId, String targetId, String pathLineId);

    /**
     * fill datasource to stop
     *
     * @param dataSourceId
     * @param stopId
     * @return
     * @throws Exception 
     */
    public String fillDatasource(String username, String dataSourceId, String stopId) throws Exception;

    /**
     * isNeedSource
     *
     * @param username
     * @param isAdmin
     * @param stopsId
     * @return
     */
    public String isNeedSource(String username, boolean isAdmin, String stopsId);
    
    /**
     * run stops
     *
     * @param username
     * @param isAdmin
     * @param runStopsVo
     * @return
     * @throws Exception 
     */
    public String runStops(String username, boolean isAdmin, RunStopsVo runStopsVo) throws Exception;
    
    /**
     * checkDatasourceLinked
     * 
     * @param datasourceId
     * @return
     */
    public String checkDatasourceLinked(String datasourceId);

}
