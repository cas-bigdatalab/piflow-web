package com.nature.component.workFlow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.PropertyTemplate;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.model.StopsTemplate;
import com.nature.component.workFlow.service.IPropertyService;
import com.nature.mapper.PropertyMapper;
import com.nature.mapper.StopsMapper;
import com.nature.mapper.StopsTemplateMapper;

@Service
public class PropertyServiceImpl implements IPropertyService {

	Logger logger = LoggerUtil.getLogger();
	
	@Autowired
	PropertyMapper propertyMapper;
	
	@Autowired
	StopsMapper stopsMapper;
	
	@Autowired
	StopsTemplateMapper stopsTemplateMapper;
	
	@Override
	public Stops queryAll(String fid,String id) {
		return propertyMapper.getStopGroupList(fid,id);
	}

	@Override
	public int updateProperty(String content,String id) {
		return propertyMapper.updatePropertyCustomValue(content,id);
	}

	@Override
	public List<Property> getStopsPropertyList() {
		return propertyMapper.getStopsPropertyList();
	}

	@Override
	public int deleteStopsPropertyById(String id) {
		return propertyMapper.deleteStopsPropertyById(id);
	}

	@Override
	public int deleteStopsPropertyByStopId(String id) {
		return propertyMapper.deleteStopsPropertyByStopId(id);
	}

	/**
	 * 对比stops模板如有差异则修改
	 */
	@Override
	public void checkStopTemplateUpdate(Stops stops) {
        Map<String, Property> PropertyMap = new HashMap<String, Property>();
        List<Property> addPropertyList = new ArrayList<Property>();
        //获取stop信息
        Stops stopsList = stopsMapper.getStopsById(stops.getId());
        //获取当前stops的StopsTemplate
        List<StopsTemplate> stopsTemplateList = stopsTemplateMapper.getStopsTemplateByName(stopsList.getName());
        StopsTemplate stopsTemplate = stopsTemplateList.get(0);
        logger.info("stopsTemplateList记录条数:"+stopsTemplateList.size());
          	//拿到StopsTemplate的模板属性
            List<PropertyTemplate> propertiesTemplateList = stopsTemplate.getProperties();
            // 当前stop存在的属性   	
            List<Property> property = stopsList.getProperties();
            if (null != property && property.size() > 0)  
          	  for (Property one : property) {
          		  PropertyMap.put(one.getName(),one);
    			}
            //如果模板的数据大于stop当前的属性个数,一样的做修改操作,多了的新增stops属性
            if (propertiesTemplateList.size()>0 && property.size()>0) {
          	  for (PropertyTemplate pt : propertiesTemplateList) { 
      				if (null != pt) {
      					Property ptname = PropertyMap.get(pt.getName());
      					if (ptname!=null) {
      						PropertyMap.remove(pt.getName());
                            Property update = new Property();
                            String name = pt.getName();
                            Date crtDttm = pt.getCrtDttm();
                            String crtUser = pt.getCrtUser();
							BeanUtils.copyProperties(pt, update);
                          	update.setName(name);
                          	update.setCrtDttm(crtDttm);
                          	update.setCrtUser(crtUser);
                          	update.setId(ptname.getId());
                          	propertyMapper.updateStopsProperty(update);
      					}else {
      						logger.info("===============stop属性与模板不一致,需要添加=================");
      						Property newProperty = new Property();
						    BeanUtils.copyProperties(pt, newProperty);
      						newProperty.setId(Utils.getUUID32());
      						newProperty.setCrtDttm(new Date());
      						newProperty.setCrtUser("Nature");
      						newProperty.setEnableFlag(true);
      						newProperty.setVersion(0L);
      						newProperty.setStops(stopsList);
      						addPropertyList.add(newProperty);
							} 
      					}
      				}
          	  if (addPropertyList.size()>0) {
					propertyMapper.addPropertyList(addPropertyList);
				}
			} 
        	// objectPathsMap中的所有需要修改的移除，剩下为要逻辑删除的
            if (null != PropertyMap && PropertyMap.size()>0)  
				for (String pageid : PropertyMap.keySet()) {
					Property deleteProperty = PropertyMap.get(pageid);
					if (null != deleteProperty) {
						logger.info("===============stop属性与模板不一致,需要删除=================");
						propertyMapper.deleteStopsPropertyById(deleteProperty.getId());
					}
				}
			}
	}
