package com.nature.component.workFlow.service;


import com.nature.base.vo.StatefulRtnBase;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.vo.StopsVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo;
import org.springframework.data.annotation.Transient;

import java.util.List;

public interface IStopsService {

    public int deleteStopsByFlowId(String id);

    /**
     * 根据flowId和pagesId查询stops
     *
     * @param flowId  必填
     * @param pageIds 可为空
     * @return
     */
    public List<StopsVo> getStopsByFlowIdAndPageIds(String flowId, String[] pageIds);

    /**
     * 修改stop
     *
     * @param stopsVo
     * @return
     */
    public Integer stopsUpdate(StopsVo stopsVo);

    /**
     * 修改接口返回的stops个别字段
     *
     * @param stopVo
     * @return
     */
    public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo stopVo);

    /**
     * 修改isCheckpoint字段
     *
     * @param stopId
     * @param isCheckpoint
     * @return
     */
    public int updateStopsCheckpoint(String stopId, boolean isCheckpoint);
    
    /**
     * 修改stopname根据id
     * @param id
     * @param stopName
     * @return
     */
    public int updateStopsNameById(String id,String stopName);

    public String getStopByNameAndFlowId(String flowId, String stopName);
    
	@Transient
    public StatefulRtnBase updateStopName(String stopId, Flow flow, String stopName, String pageId);


}
