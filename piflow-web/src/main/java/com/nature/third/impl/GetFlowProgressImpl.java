package com.nature.third.impl;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.common.constant.SysParamsCache;
import com.nature.third.inf.IGetFlowProgress;
import com.nature.third.vo.ProgressVo;

@Component
public class GetFlowProgressImpl implements IGetFlowProgress {

	Logger logger = LoggerUtil.getLogger();

	/**
	 * @Title 发送 post请求
	 */
	@Override
	public ProgressVo getFlowInfo(String appid) {
		ProgressVo jd = null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("appID", appid);
		String doGet = HttpUtils.doGet(SysParamsCache.FLOW_PROGRESS_URL(), map);
		String jsonResult = JSONObject.fromObject(doGet).getString("FlowInfo");
		if (StringUtils.isNotBlank(jsonResult)) {
			// 同样先将json字符串转换为json对象，再将json对象转换为java对象，如下所示。
			JSONObject obj = JSONObject.fromObject(jsonResult);// 将json字符串转换为json对象
			// 将json对象转换为java对象
			jd = (ProgressVo) JSONObject.toBean(obj, ProgressVo.class);
		}
		return jd; 
	}

}
