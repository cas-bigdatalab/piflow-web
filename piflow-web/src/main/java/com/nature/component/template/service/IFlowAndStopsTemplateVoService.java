package com.nature.component.template.service;

import com.nature.component.template.model.FlowTemplateModel;
import com.nature.component.template.model.PropertyTemplateModel;
import com.nature.component.template.model.StopTemplateModel;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.model.Template;
import org.apache.ibatis.annotations.Param;

import java.util.List;



public interface IFlowAndStopsTemplateVoService {
	 
	/**
	 * 添加单个stops
	 * 
	 * @param stops
	 * @return
	 */
	public int addStops(StopTemplateModel stops);
	
	/**
	 * 新增Flow
	 * 
	 * @param flow
	 * @return
	 */
	public int addFlow(FlowTemplateModel flow);
	
	/**
	 * 插入list<PropertyVo> 注意拼sql的方法必须用map接 Param内容为键值
	 * 
	 * @param propertyList (内容： 键为propertyList,值为List<PropertyVo>)
	 * @return
	 */
	public int addPropertyList(@Param("propertyList") List<PropertyTemplateModel> propertyList);
	
	public int deleteStopTemByTemplateId(@Param("id") String templateId);
	
	public int deleteStopPropertyTemByStopId(@Param("id") String stopId);
	
	/**
	 * 根据模板id查询所有stop信息
	 * @param templateId
	 * @return
	 */
	public List<StopTemplateModel> getStopsListByTemPlateId(@Param("templateId") String templateId);
	 
	/**
	 * 根据stopId查询所有stop属性信息
	 * @param templateId
	 * @return
	 */
	public List<PropertyTemplateModel> getPropertyListByStopsId(String stopsId);
	
	/**
	 * 加载模板保存stop信息
	 * @param template
	 * @param flowId
	 * @param maxPageId  flow中stop中的最大值
	 */
	public void addTemplateStopsToFlow(Template template,Flow flow, int maxPageId);
	
	public void addStopsList(List<Stops> stops,Template template);
}
