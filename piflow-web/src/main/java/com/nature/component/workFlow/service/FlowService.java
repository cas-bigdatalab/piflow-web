package com.nature.component.workFlow.service;

import com.nature.base.vo.StatefulRtnBase;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.workFlow.model.Flow;

public interface FlowService {
	/**
	 * @Title 向数据库添加或修改flow
	 * 
	 * @param mxGraphModelVo
	 * @param flowId
	 * @param isAdd
	 * @return
	 */
	public StatefulRtnBase saveOrUpdateFlow(MxGraphModelVo mxGraphModelVo, String flowId);

	/**
	 * @Title 根据id查询流信息
	 * 
	 * @param id
	 * @return
	 */
	public Flow getFlowById(String id);
}
