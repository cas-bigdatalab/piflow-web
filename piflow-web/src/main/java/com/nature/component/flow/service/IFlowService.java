package com.nature.component.flow.service;

import com.nature.base.vo.StatefulRtnBase;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowInfoDb;
import com.nature.component.flow.vo.FlowVo;
import org.springframework.data.annotation.Transient;

import java.util.List;

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
	
	public List<FlowVo> getFlowList();

	/**
	 * 分页查询flow
	 *
	 * @param offset 页数
	 * @param limit 每页条数
	 * @param param 搜索关键字
	 * @return
	 */
	public String getFlowListPage(Integer offset, Integer limit, String param);

	public String getFlowExampleList();

}
