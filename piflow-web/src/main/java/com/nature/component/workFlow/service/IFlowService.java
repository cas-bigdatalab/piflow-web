package com.nature.component.workFlow.service;

import org.springframework.data.annotation.Transient;

import com.nature.base.vo.StatefulRtnBase;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.component.workFlow.vo.FlowVo;

public interface IFlowService {
	/**
	 * 向数据库修改或添加flow中stops和paths信息
	 * 
	 * @param mxGraphModelVo
	 * @param flowId
	 * @param operType 操作类型（添加，移动，删除）
	 * @param flag 是否添加stop信息
	 * @return
	 */
	@Transient
	public StatefulRtnBase saveOrUpdateFlowAll(MxGraphModelVo mxGraphModelVo, String flowId,String operType,boolean flag);

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
	 * 保存appId
	 * 
	 * @param flowId
	 * @param startFlow
	 * @return
	 */
	@Transient
	public StatefulRtnBase saveAppId(String flowId, FlowInfoDb startFlow);
	
	/**
	 * add flow(包含画板信息)
	 * @param flow
	 * @return
	 */
	@Transient
	public int addFlow(Flow flow);

	@Transient
	public int updateFlow(Flow flow);

	@Transient
	public int deleteFLowInfo(String id);
	
	public String getMaxStopPageId(String flowId);

}
