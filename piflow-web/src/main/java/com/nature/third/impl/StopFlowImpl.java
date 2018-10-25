package com.nature.third.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.nature.base.util.HttpUtils;
import com.nature.common.constant.SysParamsCache;
import com.nature.third.inf.IStopFlow;

@Component
public class StopFlowImpl implements IStopFlow {

	@Override
	public String stopFlow(String appId) {
		String encoding = "";
		Map<String, String> map = new HashMap<>();
		map.put("appID", appId);
		String json = JSON.toJSON(map).toString();
		String doPost = HttpUtils.doPost(SysParamsCache.FLOW_STOP_URL(), json, encoding);
		return doPost;
	}
}
