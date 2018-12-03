package com.nature.component.template.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nature.component.template.vo.FlowTemplateVo;
import com.nature.component.template.vo.StopTemplateVo;
import com.nature.component.workFlow.model.Template;
import com.nature.component.workFlow.vo.PropertyVo;



public interface IFlowAndStopsTemplateVoService {
	 
	/**
	 * 添加单个stops
	 * 
	 * @param stops
	 * @return
	 */
	public int addStops(StopTemplateVo stops);
	
	/**
	 * 新增Flow
	 * 
	 * @param flow
	 * @return
	 */
	public int addFlow(FlowTemplateVo flow);
	
	/**
	 * 插入list<PropertyVo> 注意拼sql的方法必须用map接 Param内容为键值
	 * 
	 * @param propertyList (内容： 键为propertyList,值为List<PropertyVo>)
	 * @return
	 */
	public int addPropertyList(@Param("propertyList") List<PropertyVo> propertyList);
	
	public int deleteStopTemByTemplateId(@Param("id") String templateId);
	
	public int deleteStopPropertyTemByStopId(@Param("id") String stopId);
	
	public List<StopTemplateVo> getStopsListByTemPlateId(@Param("templateId") String templateId);
	 
	public List<PropertyVo> getPropertyListByStopsId(String stopsId);
	
	public void addTemplateStopsToFlow(Template template,String flowId);
}
