package com.nature.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.PropertyTemplate;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.model.StopsTemplate;

public class PropertyMapperTest extends ApplicationTests {

	@Autowired
	private PropertyMapper propertyMapper;
	@Autowired
	private StopsMapper stopsMapper;
	@Autowired
	private StopsTemplateMapper stopsTemplateMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetPropertyListByStopsId() {
		List<Property> propertyList = propertyMapper.getPropertyListByStopsId("85f90a18423245b09cde371cbb3330sd");
		if (null == propertyList) {
			logger.info("查询结果为空");
		} else {
			logger.info(propertyList.size() + "");
		}
	}

	@Test
	public void testGetStopsPropertyById() {
		Stops stops = propertyMapper.getStopGroupList("fbb42f0d8ca14a83bfab13e0ba2d7292", "2");
		if (null == stops) {
			logger.info("查询结果为空");
		}
		logger.info(stops.toString() + "         ---------------------     name：" + stops.getName());
	}

	@Test
	public void updateStopsPropertyById() {
		int update = propertyMapper.updateStops("hahah", "8731612e48cc4cc89a24191e737817f2");
		if (0 == update) {
			logger.info("修改失败了"+",影响行数:"+update);
		}else {
			logger.info("修改成功了"+",影响行数:"+update);
		}
	}

	@Test
	public void deleteStopsPropertyById() {
		int delete = propertyMapper.deleteStopsPropertyById("8731612e42cc4cc89a24191e737817f2");
		if (0 == delete) {
			logger.info("删除失败了"+",影响行数："+delete);
		}else {
			logger.info("删除成功了"+",影响行数："+delete);
		}
	}
	
	@Test
	public void updateTempleteProperyInfo() {
          Map<String, Property> PropertyMap = new HashMap<String, Property>();
          List<Property> addPropertyList = new ArrayList<Property>();
          //获取stop信息
          Stops stopsList = stopsMapper.getStopsById("32a3e372084d4f4eac97853c66b7b2d8");
          //获取当前stops的StopsTemplate
          List<StopsTemplate> stopsTemplateList = stopsTemplateMapper.getStopsTemplateByName(stopsList.getName());
          StopsTemplate stopsTemplate = stopsTemplateList.get(0);
          logger.info("stopsTemplateList记录条数:"+stopsTemplateList.size());
            //拿到StopsTemplate的模板属性6
              List<PropertyTemplate> propertiesTemplateList = stopsTemplate.getProperties();
              // 当前stop存在的属性   4
              List<Property> property = stopsList.getProperties();
              if (null != property && property.size()>0)  
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
                            	try {
									BeanUtils.copyProperties(pt, update);
								} catch (Exception e) {
									e.printStackTrace();
								} 
                            	update.setName(name);
                            	update.setCrtDttm(crtDttm);
                            	update.setCrtUser(crtUser);
                            	update.setId(ptname.getId());
                            	propertyMapper.updateStopsProperty(update);
        					}else {
        						logger.info("===============stop属性与模板不一致,需要添加=================");
        						Property newProperty = new Property();
        						try {
									BeanUtils.copyProperties(pt, newProperty);
								} catch (Exception e) {
									e.printStackTrace();
								}  
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
