package com.nature.third;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;

@Component
public class GetAllGroups {

	Logger logger = LoggerUtil.getLogger();

	public void get() {
		String url = "http://10.0.86.98:8002/stop/groups";
		String sendGetData = HttpUtils.doGet(url, null);
		logger.info("返回信息：" + sendGetData);
	}

}
