package com.nature.third.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.common.constant.SysParamsCache;
import com.nature.third.inf.IGetFlowInfo;
import com.nature.third.vo.flowInfo.FlowInfo;
import com.nature.third.vo.flowInfo.FlowInfoStopVo;

import net.sf.json.JSONObject;

@Component
public class GetFlowInfoImpl implements IGetFlowInfo {

	Logger logger = LoggerUtil.getLogger();

	/**
	 * @Title 发送 post请求
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public FlowInfo getFlowInfo(String appid) {
		FlowInfo jb = null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("appID", appid);
		String doGet = HttpUtils.doGet(SysParamsCache.FLOW_INFO_URL(), map);
		if (StringUtils.isNotBlank(doGet)) {
			// 同样先将json字符串转换为json对象，再将json对象转换为java对象，如下所示。
			JSONObject obj = JSONObject.fromObject(doGet);// 将json字符串转换为json对象
			// 当FlowInfo中有List时需要
			Map<String, Class> classMap = new HashMap<String, Class>();
			// key为FlowInfo中的List的name,value为list的泛型的class
			classMap.put("stops", FlowInfoStopVo.class);
			// 将json对象转换为java对象
			jb = (FlowInfo) JSONObject.toBean(obj, FlowInfo.class, classMap);
		}
		return jb;
	}

}
