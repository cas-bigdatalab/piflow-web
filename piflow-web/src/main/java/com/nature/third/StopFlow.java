package com.nature.third;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.nature.base.util.HttpUtils;

@Component
public class StopFlow {

	public String flowStop(String appId) {
		String encoding = "";
		String url = "http://10.0.86.98:8001/flow/stop";
		Map<String, String> map = new HashMap<>();
		map.put("appID", appId);
		String json = JSON.toJSON(map).toString();
		String doPost = HttpUtils.doPost(url, json, encoding);
		return doPost;
	}
}
