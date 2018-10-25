package com.nature.third.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.common.constant.SysParamsCache;
import com.nature.third.inf.IGetFlowLog;
import com.nature.third.vo.flowLog.FlowLog;

import net.sf.json.JSONObject;

@Component
public class GetFlowLogImpl implements IGetFlowLog {

	Logger logger = LoggerUtil.getLogger();

	/**
	 * @Title 发送 post请求
	 */
	@Override
	public FlowLog getFlowLog(String appid) {
		FlowLog flowLog = null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("appID", appid);
		String doGet = HttpUtils.doGet(SysParamsCache.FLOW_LOG_URL(), map);
		logger.info("调用成功 : " + doGet);
		if (StringUtils.isNotBlank(doGet)) {
			// 同样先将json字符串转换为json对象，再将json对象转换为java对象，如下所示。
			JSONObject obj = JSONObject.fromObject(doGet);// 将json字符串转换为json对象
			// 将json对象转换为java对象
			flowLog = (FlowLog) JSONObject.toBean(obj, FlowLog.class);
		}
		return flowLog;
	}

}
