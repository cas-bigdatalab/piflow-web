package com.nature.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.service.IPropertyService;

@RestController
@RequestMapping("/stops")
public class StopsCtrl {
	
	Logger logger = LoggerUtil.getLogger();
	
	@Autowired
	IPropertyService propertyServiceImpl;

	@RequestMapping("/queryIdInfo")
	public Stops getStopGroup(String fid,String id) {
		if (StringUtils.isNotBlank(fid) && StringUtils.isNotBlank(id)) {
			Stops queryInfo = propertyServiceImpl.queryAll(fid, id);
			if (null != queryInfo) {
				//对比stops模板属性并作出修改
				//propertyServiceImpl.checkStopTemplateUpdate(queryInfo.getId());
				return queryInfo;
			}
		}
		return null;
	}
	
	/**
	 * 多个一保存起修改
	 * @param content
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateStops")
	public Integer updateStops(String[] content,String id){
		int updateStops = 0;
		if (null != content && content.length > 0 ){  
		for (String string : content) {
			//使用#id#标记来截取数据,第一为内容，第二个为要修改记录的id
			String[] split = string.split("#id#");
			if (null != split && split.length == 2) {
				String updateContent = split[0];
				String updateId = split[1];
				updateStops = propertyServiceImpl.updateProperty(updateContent,updateId);
				}
			}
		}
		if (updateStops >0) {
			logger.info("stops属性修改成功:"+updateStops);
			return updateStops;
		}else {
			return 0;
		}
	}
	
	@RequestMapping("/updateStopsOne")
	public Integer updateStops(String content,String id){
		int updateStops = 0;
		updateStops = propertyServiceImpl.updateProperty(content,id);
		if (updateStops >0) {
			logger.info("stops属性修改成功:"+updateStops);
			return updateStops;
		}else {
			return updateStops;
		}
	}

}
