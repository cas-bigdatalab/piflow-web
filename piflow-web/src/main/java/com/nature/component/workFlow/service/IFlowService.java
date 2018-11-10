package com.nature.component.workFlow.service;

import com.nature.base.vo.StatefulRtnBase;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.component.workFlow.vo.FlowVo;
import org.springframework.data.annotation.Transient;

public interface IFlowService {
	/**
	 * @Title 向数据库添加或修改flow
	 * 
	 * @param mxGraphModelVo
	 * @param flowId
	 * @param isAdd
	 * @return
	 */
	@Transient
	public StatefulRtnBase saveOrUpdateFlowAll(MxGraphModelVo mxGraphModelVo, String flowId);

	/**
	 * 根据id查询流信息
	 *
	 * @param id
	 * @return
	 */
	@Transient
	public Flow getFlowById(String id);

	/**
	 * 根据id查询流信息
	 *
	 * @param id
	 * @return
	 */
	@Transient
	public FlowVo getFlowVoById(String id);

	/**
	 * @Title 保存appId
	 * 
	 * @param flowId
	 * @param startFlow
	 * @return
	 */
	@Transient
	public StatefulRtnBase saveAppId(String flowId, FlowInfoDb startFlow);
	
	/**
	 * add flow
	 * @param flow
	 * @return
	 */
	@Transient
	public int addFlow(Flow flow);

	@Transient
	public int updateFlow(Flow flow);

	@Transient
	public int deleteFLowInfo(String id);

}
