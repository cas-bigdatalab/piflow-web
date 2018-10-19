package com.nature.third;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;

@Component
public class GetStopInfo {

	Logger logger = LoggerUtil.getLogger();

	public void get() {
		String url = "http://10.0.86.98:8002/stop/info";
		String encoding = "cn.piflow.bundle.csv.CsvParser";
		Map<String, String> map = new HashMap<String, String>();
		map.put("bundle", encoding);
		String sendGetData = HttpUtils.doGet(url, map);
		logger.info("返回信息：" + sendGetData);
	}
}
