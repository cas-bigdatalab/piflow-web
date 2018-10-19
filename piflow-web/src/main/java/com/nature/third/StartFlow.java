package com.nature.third;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.nature.base.util.HttpUtils;
import com.nature.base.util.JsonFormatTool;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Paths;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;
import com.nature.third.vo.FlowVo;
import com.nature.third.vo.PathVo;
import com.nature.third.vo.StopVo;

@Component
public class StartFlow {

	Logger logger = LoggerUtil.getLogger();

	/**
	 * @Title 发送 post请求
	 */
	public String startFlow(Flow flow) {
		String url = "http://10.0.86.98:8001/flow/start";
		String json = this.flowToJosn(flow);
		String encoding = "";
		String formatJson = JsonFormatTool.formatJson(json);
		logger.debug("\n" + formatJson);
		String doPost = HttpUtils.doPost(url, formatJson, encoding);
		logger.info("返回信息：" + doPost);
		return doPost;
	}

	private String flowToJosn(Flow flow) {
		FlowVo flowVo = new FlowVo();
		// stops
		List<StopVo> stops = new ArrayList<StopVo>();
		// paths
		List<PathVo> paths = new ArrayList<PathVo>();

		flowVo.setName(flow.getName());
		flowVo.setUuid(flow.getUuid());

		List<Stops> stopsList = flow.getStopsList();
		for (Stops stop : stopsList) {
			StopVo stopVo = new StopVo();
			Map<String, Object> properties = new HashMap<String, Object>();
			stopVo.setUuid(stop.getId());
			stopVo.setName(stop.getName());
			stopVo.setBundle(stop.getBundel());
			List<Property> propertiesList = stop.getProperties();
			if (null != propertiesList && propertiesList.size() > 0) {
				for (Property property : propertiesList) {
					String name = property.getName();
					if (StringUtils.isNotBlank(name)) {
						String customValue2 = property.getCustomValue();
						if ("[\"out1\", \"out2\", \"out3\"]".equals(customValue2)) {
							List<String> list = new ArrayList<String>();
							list.add("out1");
							list.add("out2");
							list.add("out3");
							properties.put(name, list);
						} else {
							String customValue = (null != customValue2 ? customValue2 : "");
							properties.put(name, customValue);
						}
					}
				}
			}
			stopVo.setProperties(properties);
			stops.add(stopVo);
		}

		List<Paths> pathsList = flow.getPathsList();
		if (null != pathsList && pathsList.size() > 0) {
			for (Paths path : pathsList) {
				String from = (null != path.getFrom() ? path.getFrom() : "");
				String outport = (null != path.getOutport() ? path.getOutport() : "");
				String inport = (null != path.getInport() ? path.getInport() : "");
				String to = (null != path.getTo() ? path.getTo() : "");
				PathVo pathVo = new PathVo();
				pathVo.setFrom(from);
				pathVo.setOutport(outport);
				pathVo.setInport(inport);
				pathVo.setTo(to);
				paths.add(pathVo);
			}
		}
		flowVo.setPaths(paths);
		flowVo.setStops(stops);
		String json = JSON.toJSON(flowVo).toString();

		return "{\"flow\":" + json + "}";
	}
}
