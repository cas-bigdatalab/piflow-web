package cn.cnic.component.flow.service;

import java.util.List;

import cn.cnic.component.flow.vo.StopsCustomizedPropertyVo;
import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.controller.requestVo.RunStopsVo;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;

public interface IStopsService {

    /**
     * Modify the "isCheckpoint" field
     *
     * @param username
     * @param stopId
     * @param isCheckpointStr
     * @return
     * @throws Exception
     */
    public String updateStopsCheckpoint(String username, String stopId, String isCheckpointStr) throws Exception;

    /**
     * Modify "stopName" based on id
     *
     * @param username
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

    /**
     * get Stop port info
     *
     * @param flowId
     * @param sourceId
     * @param targetId
     * @param pathLineId
     * @return
     */
    public String getStopsPort(String flowId, String sourceId, String targetId, String pathLineId);

    /**
     * fill datasource to stop
     *
     * @param username
     * @param isAdmin
     * @param dataSourceId
     * @param stopId
     * @return
     * @throws Exception
     */
    public String fillDatasource(String username, boolean isAdmin, String dataSourceId, String stopId) throws Exception;

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

    /**
     * Add Stop customized property
     *
     * @param username
     * @param stopsCustomizedPropertyVo
     * @return
     * @throws Exception
     */
    public String addStopCustomizedProperty(String username, StopsCustomizedPropertyVo stopsCustomizedPropertyVo) throws Exception;

    /**
     * Update Stop customized property
     *
     * @param username
     * @param stopsCustomizedPropertyVo
     * @return
     */
    public String updateStopsCustomizedProperty(String username, StopsCustomizedPropertyVo stopsCustomizedPropertyVo);

    /**
     * Delete Stop customized property
     *
     * @param username
     * @param customPropertyId
     * @return
     */
    public String deleteStopsCustomizedProperty(String username, String customPropertyId);

    /**
     * Delete RouterStop customized property
     *
     * @param username
     * @param customPropertyId
     * @return
     */
    public String deleteRouterStopsCustomizedProperty(String username, String customPropertyId);

    /**
     * Get RouterStops customized property
     *
     * @param customPropertyId
     * @return
     */
    public String getRouterStopsCustomizedProperty(String customPropertyId);

    /**
     * Get stops name by flow id
     *
     * @param flowId
     * @return
     */
    public String getStopsNameByFlowId(String flowId);

    /**
     * Get stops info by id,if type is python,it's file_record_id,if type is scala,it's flow_stops_template_id
     * @param id
     * @param type
     * @return
     */
    String getStopsInfoById(String id,String type);

    String getStopsInfoByFlowId(String flowId);
}
