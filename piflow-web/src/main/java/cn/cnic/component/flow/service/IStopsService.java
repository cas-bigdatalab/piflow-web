package cn.cnic.component.flow.service;


import java.util.List;

import cn.cnic.component.flow.vo.StopsVo;
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
    public Integer stopsUpdate(String username, StopsVo stopsVo);

    /**
     * Modify the "stops" individual fields returned by the interface
     *
     * @param stopVo
     * @return
     */
    public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo stopVo);

    /**
     * Modify the "isCheckpoint" field
     *
     * @param stopId
     * @param isCheckpointStr
     * @return
     */
    public String updateStopsCheckpoint(String username, String stopId, String isCheckpointStr);

    /**
     * Modify "stopName" based on id
     *
     * @param id
     * @param stopName
     * @return
     */
    public int updateStopsNameById(String username, String id, String stopName);

    public String getStopByNameAndFlowId(String flowId, String stopName);

    public String updateStopName(String username, boolean isAdmin, String stopId, String flowId, String stopName, String pageId);

    public String getStopsPort(String flowId, String sourceId, String targetId, String pathLineId);

    /**
     * fill datasource to stop
     *
     * @param dataSourceId
     * @param stopId
     * @return
     */
    public String fillDatasource(String username, String dataSourceId, String stopId);

    /**
     * isNeedSource
     *
     * @param username
     * @param isAdmin
     * @param stopsId
     * @return
     */
    public String isNeedSource(String username, boolean isAdmin, String stopsId);

}
