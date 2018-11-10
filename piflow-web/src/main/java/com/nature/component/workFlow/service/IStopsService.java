package com.nature.component.workFlow.service;


import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.vo.StopsVo;

import java.util.List;

public interface IStopsService {
	
	public int deleteStopsByFlowId(String id);

	/**
	 * 根据flowId和pagesId查询stops
	 * @param flowId 必填
	 * @param pageIds 可为空
	 * @return
	 */
	public List<StopsVo> getStopsByFlowIdAndPageIds(String flowId, String[] pageIds);

}
