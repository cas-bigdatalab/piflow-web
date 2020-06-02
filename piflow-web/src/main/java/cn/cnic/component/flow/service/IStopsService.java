package cn.cnic.component.flow.service;


import cn.cnic.base.vo.StatefulRtnBase;
import cn.cnic.component.flow.model.Flow;
import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;

import javax.transaction.Transactional;
import java.util.List;

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
    public Integer stopsUpdate(StopsVo stopsVo);

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
     * @param isCheckpoint
     * @return
     */
    public int updateStopsCheckpoint(String stopId, boolean isCheckpoint);

    /**
     * Modify "stopName" based on id
     *
     * @param id
     * @param stopName
     * @return
     */
    public int updateStopsNameById(String id, String stopName);

    public String getStopByNameAndFlowId(String flowId, String stopName);

    @Transactional
    public StatefulRtnBase updateStopName(String stopId, Flow flow, String stopName, String pageId);

    public String getStopsPort(String flowId, String sourceId, String targetId, String pathLineId);

    /**
     * fill datasource to stop
     *
     * @param dataSourceId
     * @param stopId
     * @return
     */
    public String fillDatasource(String dataSourceId, String stopId);

}
