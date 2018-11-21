package com.nature.component.workFlow.service;


import java.util.List;

import com.nature.component.workFlow.vo.StopsVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo2;

public interface IStopsService {
	
	public int deleteStopsByFlowId(String id);

	/**
	 * 根据flowId和pagesId查询stops
	 * @param flowId 必填
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
	 * @param stopVo
	 * @return
	 */
	public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo2 stopVo);
	
	

}
