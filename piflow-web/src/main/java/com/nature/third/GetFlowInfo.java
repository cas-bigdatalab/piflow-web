package com.nature.third;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.mapper.FlowInfoDbMapper;
import com.nature.third.inf.IGetFlowInfo;
import com.nature.third.vo.flowInfo.FlowInfo;

@Component
public class GetFlowInfo {

	Logger logger = LoggerUtil.getLogger();
	
	@Autowired
	private FlowInfoDbMapper flowInfoDbMapper;
	@Resource
	private IGetFlowInfo getFlowInfoImpl;

	/**
	 * @Title 发送 post请求
	 */
	public String flowInfo(String appid) {
		Map<String, String> map = new HashMap<String, String>();
		String url = "http://10.0.86.191:8002/flow/info";
		map.put("appID", appid);
		String doGet = HttpUtils.doGet(url, map);
		System.out.println("接口返回的"+doGet);
		return doGet;
	}
	
	/**
	 * 通过appId调接口返回flowInfo并保存数据库
	 * @param appId
	 * @return
	 */
	public FlowInfoDb AddFlowInfo(String appId) {
		FlowInfoDb db = new FlowInfoDb();
		FlowInfo startFlow2 = getFlowInfoImpl.getFlowInfo(appId);
		logger.info("测试返回信息：" + startFlow2);
		db.setId(startFlow2.getFlow().getId());
		db.setName(startFlow2.getFlow().getName());
		db.setState(startFlow2.getFlow().getState());
		db.setEndTime(startFlow2.getFlow().getEndTime());
		db.setStartTime(startFlow2.getFlow().getStartTime());
		db.setCrtDttm(new Date());
		db.setCrtUser("wdd");
		db.setVersion(0L);
		db.setEnableFlag(true);
		db.setLastUpdateUser("王栋栋");
		db.setLastUpdateDttm(new Date());
		flowInfoDbMapper.addFlowInfo(db);
		return db;
	 
	}

}
