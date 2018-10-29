package com.nature.third.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.common.constant.SysParamsCache;
import com.nature.third.inf.IGetFlowProgress;

@Component
public class GetFlowProgressImpl implements IGetFlowProgress {

	Logger logger = LoggerUtil.getLogger();

	/**
	 * @Title 发送 post请求
	 */
	@Override
	public String getFlowInfo(String appid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("appID", appid);
		String doGet = HttpUtils.doGet(SysParamsCache.FLOW_PROGRESS_URL(), map);
		if (StringUtils.isNotBlank(doGet)) {
			return doGet;
		}
		return null; 
	}

}
