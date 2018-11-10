package com.nature.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.component.workFlow.service.IFlowInfoDbService;
import com.nature.mapper.FlowInfoDbMapper;
import com.nature.third.inf.IGetFlowProgress;
import com.nature.third.vo.ThirdProgressVo;

@Controller
@RequestMapping("/flowInfoDb")
public class FlowInfoDbCtrl {

	/**
	 * @Title 引入日志，注意都是"org.slf4j"包下
	 */
	Logger logger = LoggerUtil.getLogger();

	@Autowired
	private IFlowInfoDbService flowInfoDbServiceImpl;
	
	@Autowired
	private IGetFlowProgress iGetFlowProgress;
	
	@Autowired
	private FlowInfoDbMapper flowInfoDbMapper;

	/**
	 * 查询进度
	 * @param model
	 * @param content
	 * @return
	 */
	@SuppressWarnings("null")
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, String> findAppInfo(String[] content) {
		List<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		if (content.length> 0 && null != content) {
			for (String string : content) {
				list.add(string);
			}
		}
		//通过appId查询数据库,返回list
		List<FlowInfoDb> flowInfoList = flowInfoDbServiceImpl.getFlowInfoByIds(list);
		if (null == flowInfoList && flowInfoList.isEmpty()) {
			return map;
		}
		 for (FlowInfoDb flowInfoDb : flowInfoList) {
			 //遍历list,如果进度小于100去调接口,否则说明已经是100,直接返回
			if (Float.parseFloat(flowInfoDb.getProgress()) < 100/* && "COMPLETED".equals(flowInfoDb.getState())*/) {
				ThirdProgressVo progress = iGetFlowProgress.getFlowInfo(flowInfoDb.getId());
				 //如果接口返回进度为符合100,则更新数据库并返回
				if (StringUtils.isNotBlank(progress.getProgress()) && Float.parseFloat(progress.getProgress()) == 100) {
					FlowInfoDb up = new FlowInfoDb();
					up.setId(flowInfoDb.getId());
					up.setProgress(progress.getProgress());
					up.setLastUpdateDttm(new Date());
					up.setLastUpdateUser("wdd");
					up.setState("COMPLETE");
					flowInfoDbMapper.updateFlowInfo(up);
				} 
				map.put(flowInfoDb.getId(), progress.getProgress());
			}else {
				map.put(flowInfoDb.getId(), flowInfoDb.getProgress());
			}
		  } 
		return map;
	} 

	/**
	 * 查询进度
	 * @param appid
	 * @return
	 */
	@RequestMapping("/getAppInfoProgress")
	@ResponseBody
	public String getAppInfoProgress(String appid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("code","0");
		//通过appId查询数据库
		FlowInfoDb flowInfo = flowInfoDbServiceImpl.getFlowInfoById(appid);
		if (null == flowInfo) {
			return JsonUtils.toJsonNoException(map);
		}
		//如果进度小于100去调接口,否则说明已经是100,直接返回
		if (Float.parseFloat(flowInfo.getProgress()) < 100 /*&& "STARTED".equals(flowInfo.getState())*/) {
			ThirdProgressVo progress = iGetFlowProgress.getFlowInfo(flowInfo.getId());
			//如果接口返回进度为符合100,则更新数据库并返回
			if (StringUtils.isNotBlank(progress.getProgress()) && Float.parseFloat(progress.getProgress()) == 100) {
				FlowInfoDb up = new FlowInfoDb();
				up.setId(flowInfo.getId());
				up.setProgress(progress.getProgress());
				up.setLastUpdateDttm(new Date());
				up.setLastUpdateUser("wdd");
				up.setState("COMPLETE");
				flowInfoDbMapper.updateFlowInfo(up);
			}
			map.put("progress", progress.getProgress());
		}else {
			map.put("progress", flowInfo.getProgress());
		}
		map.put("code","1");
		map.put("state", flowInfo.getState());
		return JsonUtils.toJsonNoException(map);
	}

}
