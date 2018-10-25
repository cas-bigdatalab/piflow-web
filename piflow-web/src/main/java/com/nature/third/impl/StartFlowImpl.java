package com.nature.third.impl;

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
import com.nature.common.constant.SysParamsCache;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Paths;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;
import com.nature.third.inf.IStartFlow;
import com.nature.third.vo.FlowVo;
import com.nature.third.vo.PathVo;
import com.nature.third.vo.StopVo;

@Component
public class StartFlowImpl implements IStartFlow {

	Logger logger = LoggerUtil.getLogger();

	/**
	 * @Title 发送 post请求
	 */
	@Override
	public String startFlow(Flow flow) {
		String json = this.flowToJosn(flow);
		String encoding = "";
		String formatJson = JsonFormatTool.formatJson(json);
		logger.debug("\n" + formatJson);

		// String ss =
		// "{\"flow\":{\"name\":\"默认\",\"stops\":[{\"name\":\"CsvParser\",\"bundle\":\"cn.piflow.bundle.csv.CsvParser\",\"uuid\":\"d1f7d585539742458d7c9ec54ae6a976\",\"properties\":{\"schema\":\"title,author,pages\",\"csvPath\":\"hdfs://10.0.86.89:9000/xjzhu/phdthesis.csv\",\"header\":\"false\",\"delimiter\":\",\"}},{\"name\":\"XmlParser\",\"bundle\":\"cn.piflow.bundle.xml.XmlParser\",\"uuid\":\"f159aa490b0247759177cc4d5abf4a8a\",\"properties\":{\"rowTag\":\"phdthesis\",\"xmlpath\":\"hdfs://10.0.86.89:9000/xjzhu/dblp.mini.xml\"}},{\"name\":\"SelectField\",\"bundle\":\"cn.piflow.bundle.common.SelectField\",\"uuid\":\"7ab77c116cc24d6286a91422858d8d4d\",\"properties\":{\"schema\":\"title,author,pages\"}},{\"name\":\"Merge\",\"bundle\":\"cn.piflow.bundle.common.Merge\",\"uuid\":\"2401bf4590a240fe811114757af702c2\",\"properties\":{\"inports\":\"\"}},{\"name\":\"PutHiveStreaming\",\"bundle\":\"cn.piflow.bundle.hive.PutHiveStreaming\",\"uuid\":\"6c5b1b1cc4b44804bc90b73b4edc1ee9\",\"properties\":{\"database\":\"sparktest\",\"table\":\"dblp_phdthesis\"}}],\"uuid\":\"69e3046832b0410fbb2f374996e297ca\",\"paths\":[{\"inport\":\"\",\"from\":\"Merge\",\"to\":\"PutHiveStreaming\",\"outport\":\"\"},{\"inport\":\"\",\"from\":\"SelectField\",\"to\":\"Merge\",\"outport\":\"\"}]}}";
		// String s0 =
		// "{\"flow\":{\"name\":\"test\",\"uuid\":\"1234\",\"checkpoint\":\"Merge\",\"stops\":[{\"uuid\":\"1111\",\"name\":\"XmlParser\",\"bundle\":\"cn.piflow.bundle.xml.XmlParser\",\"properties\":{\"xmlpath\":\"hdfs://10.0.86.89:9000/xjzhu/dblp.mini.xml\",\"rowTag\":\"phdthesis\"}},{\"uuid\":\"2222\",\"name\":\"SelectField\",\"bundle\":\"cn.piflow.bundle.common.SelectField\",\"properties\":{\"schema\":\"title,author,pages\"}},{\"uuid\":\"3333\",\"name\":\"PutHiveStreaming\",\"bundle\":\"cn.piflow.bundle.hive.PutHiveStreaming\",\"properties\":{\"database\":\"sparktest\",\"table\":\"dblp_phdthesis\"}},{\"uuid\":\"4444\",\"name\":\"CsvParser\",\"bundle\":\"cn.piflow.bundle.csv.CsvParser\",\"properties\":{\"csvPath\":\"hdfs://10.0.86.89:9000/xjzhu/phdthesis.csv\",\"header\":\"false\",\"delimiter\":\",\",\"schema\":\"title,author,pages\"}},{\"uuid\":\"555\",\"name\":\"Merge\",\"bundle\":\"cn.piflow.bundle.common.Merge\",\"properties\":{}}],\"paths\":[{\"from\":\"XmlParser\",\"outport\":\"\",\"inport\":\"\",\"to\":\"SelectField\"},{\"from\":\"SelectField\",\"outport\":\"\",\"inport\":\"data1\",\"to\":\"Merge\"},{\"from\":\"CsvParser\",\"outport\":\"\",\"inport\":\"data2\",\"to\":\"Merge\"},{\"from\":\"Merge\",\"outport\":\"\",\"inport\":\"\",\"to\":\"PutHiveStreaming\"}]}}";
		// String formatJson2 = JsonFormatTool.formatJson(ss);
		String doPost = HttpUtils.doPost(SysParamsCache.FLOW_START_URL(), formatJson, encoding);
		logger.info("返回信息：" + doPost);
		return doPost;
	}

	private String flowToJosn(Flow flow) {
		FlowVo flowVo = new FlowVo();
		// stops
		List<StopVo> stops = new ArrayList<StopVo>();
		// paths
		List<PathVo> paths = new ArrayList<PathVo>();
		// stops
		Map<String, StopVo> stopsMap = new HashMap<String, StopVo>();

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
			stopsMap.put(stop.getPageId(), stopVo);
			stops.add(stopVo);
		}

		List<Paths> pathsList = flow.getPathsList();
		if (null != pathsList && pathsList.size() > 0) {
			for (Paths path : pathsList) {
				StopVo fromStopVo = stopsMap.get(path.getFrom());
				StopVo StopVoto = stopsMap.get(path.getTo());
				if (null == fromStopVo) {
					fromStopVo = new StopVo();
				}
				if (null == StopVoto) {
					fromStopVo = new StopVo();
				}
				String from = (null != StopVoto.getName() ? path.getFrom() : "");
				String outport = (null != path.getOutport() ? path.getOutport() : "");
				String inport = (null != path.getInport() ? path.getInport() : "");
				String to = (null != fromStopVo.getName() ? path.getTo() : "");
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
