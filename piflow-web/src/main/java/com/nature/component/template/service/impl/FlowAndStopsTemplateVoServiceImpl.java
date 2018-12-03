package com.nature.component.template.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.component.template.service.IFlowAndStopsTemplateVoService;
import com.nature.component.template.vo.FlowTemplateVo;
import com.nature.component.template.vo.StopTemplateVo;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.model.Template;
import com.nature.component.workFlow.vo.PropertyVo;
import com.nature.mapper.FlowMapper;
import com.nature.mapper.PropertyMapper;
import com.nature.mapper.StopsMapper;
import com.nature.mapper.template.FlowAndStopsTemplateVoMapper;

@Service
@Transactional
public class FlowAndStopsTemplateVoServiceImpl implements IFlowAndStopsTemplateVoService {

    Logger logger = LoggerUtil.getLogger();
    
    
    @Autowired
    private FlowAndStopsTemplateVoMapper flowAndStopsTemplateVoMapper;
    
    @Autowired
    private FlowMapper flowMapper;
    
    @Autowired
    private StopsMapper stopsMapper;
    
    @Autowired
    private PropertyMapper propertyMapper;

	@Override
	public int addStops(StopTemplateVo stops) {
		return flowAndStopsTemplateVoMapper.addStops(stops);
	}

	@Override
	public int addFlow(FlowTemplateVo flow) {
		return flowAndStopsTemplateVoMapper.addFlow(flow);
	}

	@Override
	public int addPropertyList(List<PropertyVo> propertyList) {
		return flowAndStopsTemplateVoMapper.addPropertyList(propertyList);
	}

	@Override
	public int deleteStopTemByTemplateId(String templateId) {
		return flowAndStopsTemplateVoMapper.deleteStopTemByTemplateId(templateId);
	}

	@Override
	public int deleteStopPropertyTemByStopId(String stopId) {
		return flowAndStopsTemplateVoMapper.deleteStopPropertyTemByStopId(stopId);
	}

	@Override
	public List<StopTemplateVo> getStopsListByTemPlateId(String templateId) {
		return flowAndStopsTemplateVoMapper.getStopsListByTemPlateId(templateId);
	}

	@Override
	public List<PropertyVo> getPropertyListByStopsId(String stopsId) {
		return flowAndStopsTemplateVoMapper.getPropertyListByStopsId(stopsId);
	}

	@Override
	public void addTemplateStopsToFlow(Template template,String flowId) {
		int addPropertyList = 0;
		List<Property> list = new ArrayList<Property>();
		Flow flowById = flowMapper.getFlowById(flowId);
		// 获取stop 中 pageId最大值
		String maxStopPageId = flowMapper.getMaxStopPageId(flowId);
		maxStopPageId = StringUtils.isNotBlank(maxStopPageId) ? maxStopPageId : "1";
		// 获取stop信息
		List<StopTemplateVo> stopsList = template.getStopsList();
		int num = 0;
		// 开始遍历保存stop和属性信息
		 if (null != stopsList && stopsList.size() > 0) {
			for (StopTemplateVo stopsVo : stopsList) {
				num++;
				Stops stop = new Stops();
				BeanUtils.copyProperties(stopsVo, stop);
				//pageId最大值开始增加
				stop.setPageId(String.valueOf(Integer.parseInt(maxStopPageId)+num));
				stop.setId(Utils.getUUID32());
				stop.setCrtUser("wdd");
				stop.setLastUpdateUser("wdd");
				stop.setFlow(flowById);
				int addStops = stopsMapper.addStops(stop);
				logger.info("addStops影响行数"+addStops);
				if (addStops > 0) {
					if (null != stopsVo.getProperties()) {
						List<PropertyVo> properties = stopsVo.getProperties();
						if (null != properties && properties.size() > 0) {
							for (PropertyVo property : properties) {
								Property propertyModel = new Property();
								BeanUtils.copyProperties(property, propertyModel);
								propertyModel.setStops(stop);
								propertyModel.setId(Utils.getUUID32());
								propertyModel.setCrtUser("wdd");
								propertyModel.setLastUpdateUser("wdd");
								propertyModel.setVersion(0L);
								list.add(propertyModel);
							}
						}
					}
				}else {
	                logger.error("新建保存失败propertyModel", new Exception("新建保存失败propertyModel"));
	            }
				
			
			}
			if (null != list && list.size() > 0) {
				System.out.println(list.size()+"属性");
				addPropertyList = propertyMapper.addPropertyList(list);
				logger.info("addStops影响行数"+addPropertyList);
			}
		}
	}
}
