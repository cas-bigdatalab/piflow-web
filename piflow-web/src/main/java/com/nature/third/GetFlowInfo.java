package com.nature.third;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.third.vo.FlowInfo;

import net.sf.json.JSONObject;

@Component
public class GetFlowInfo {

	Logger logger = LoggerUtil.getLogger();

	/**
	 * @Title 发送 post请求
	 */
	public String flowInfo(String appid) {
		Map<String, String> map = new HashMap<String, String>();
		String url = "http://10.0.86.98:8002/flow/info";
		map.put("appID", appid);
		String doGet = HttpUtils.doGet(url, map);
		// 同样先将json字符串转换为json对象，再将json对象转换为java对象，如下所示。
		JSONObject obj = new JSONObject().fromObject(doGet);// 将json字符串转换为json对象
		// 将json对象转换为java对象
		FlowInfo jb = (FlowInfo) JSONObject.toBean(obj, FlowInfo.class);//
		// 将建json对象转换为Person对象
		return doGet;
	}

}
