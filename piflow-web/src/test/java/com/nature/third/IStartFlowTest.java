package com.nature.third;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Paths;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;
import com.nature.third.inf.IStartFlow;

public class IStartFlowTest extends ApplicationTests {

	@Resource
	private IStartFlow startFlowImpl;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testStartFlow() {
		Flow setFlow = this.setFlow();
		String startFlow2 = startFlowImpl.startFlow(setFlow);
		logger.info("测试返回信息：" + startFlow2);
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void testAddFlow() {
	}

	private Flow setFlow() {
		Flow flow = new Flow();
		flow.setName("test");
		flow.setUuid("000");

		// stops
		List<Stops> stopsList = new ArrayList<Stops>();
		// 1
		{
			Stops stops = new Stops();
			stops.setId("111");
			stops.setName("XmlParser");
			stops.setBundel("cn.piflow.bundle.xml.XmlParser");
			List<Property> properties = new ArrayList<Property>();
			Property property0 = new Property();
			property0.setName("xmlpath");
			property0.setCustomValue("hdfs://10.0.86.89:9000/xjzhu/dblp.xml");
			Property property1 = new Property();
			property1.setName("rowTag");
			property1.setCustomValue("phdthesis");
			properties.add(property0);
			properties.add(property1);
			stops.setProperties(properties);
			stopsList.add(stops);
		}
		// 2
		{
			Stops stops = new Stops();
			stops.setId("222");
			stops.setName("SelectField");
			stops.setBundel("cn.piflow.bundle.common.SelectField");
			List<Property> properties = new ArrayList<Property>();
			Property property0 = new Property();
			property0.setName("schema");
			property0.setCustomValue("title,author,pages");
			properties.add(property0);
			stops.setProperties(properties);
			stopsList.add(stops);
		}
		// 3
		{
			Stops stops = new Stops();
			stops.setId("333");
			stops.setName("PutHiveStreaming");
			stops.setBundel("cn.piflow.bundle.hive.PutHiveStreaming");
			List<Property> properties = new ArrayList<Property>();
			Property property0 = new Property();
			property0.setName("table");
			property0.setCustomValue("dblp_phdthesis");
			Property property1 = new Property();
			property1.setName("database");
			property1.setCustomValue("sparktest");
			properties.add(property0);
			properties.add(property1);
			stops.setProperties(properties);
			stopsList.add(stops);
		}
		// 4
		{
			Stops stops = new Stops();
			stops.setId("444");
			stops.setName("CsvParser");
			stops.setBundel("cn.piflow.bundle.csv.CsvParser");
			List<Property> properties = new ArrayList<Property>();
			Property property0 = new Property();
			property0.setName("csvPath");
			property0.setCustomValue("hdfs://10.0.86.89:9000/xjzhu/phdthesis.csv");
			Property property1 = new Property();
			property1.setName("header");
			property1.setCustomValue("false");
			Property property2 = new Property();
			property2.setName("delimiter");
			property2.setCustomValue(",");
			Property property3 = new Property();
			property3.setName("schema");
			property3.setCustomValue("title,author,pages");
			properties.add(property0);
			properties.add(property1);
			properties.add(property2);
			properties.add(property3);
			stops.setProperties(properties);
			stopsList.add(stops);
		}
		// 5
		{
			Stops stops = new Stops();
			stops.setId("555");
			stops.setName("Merge");
			stops.setBundel("cn.piflow.bundle.common.Merge");
			List<Property> properties = new ArrayList<Property>();
			stops.setProperties(properties);
			stopsList.add(stops);
		}
		// 6
		{
			Stops stops = new Stops();
			stops.setId("666");
			stops.setName("Fork");
			stops.setBundel("cn.piflow.bundle.common.Fork");
			List<Property> properties = new ArrayList<Property>();
			Property property0 = new Property();
			property0.setName("outports");
			property0.setCustomValue("[\"out1\", \"out2\", \"out3\"]");
			properties.add(property0);
			stops.setProperties(properties);
			stopsList.add(stops);
		}
		// 7
		{
			Stops stops = new Stops();
			stops.setId("777");
			stops.setName("JsonSave");
			stops.setBundel("cn.piflow.bundle.json.JsonSave");
			List<Property> properties = new ArrayList<Property>();
			Property property0 = new Property();
			property0.setName("jsonSavePath");
			property0.setCustomValue("hdfs://10.0.86.89:9000/xjzhu/phdthesis.json");
			properties.add(property0);
			stops.setProperties(properties);
			stopsList.add(stops);
		}
		// 8
		{
			Stops stops = new Stops();
			stops.setId("888");
			stops.setName("CsvSave");
			stops.setBundel("cn.piflow.bundle.csv.CsvSave");
			List<Property> properties = new ArrayList<Property>();
			Property property0 = new Property();
			property0.setName("csvSavePath");
			property0.setCustomValue("hdfs://10.0.86.89:9000/xjzhu/phdthesis_result.csv");
			Property property1 = new Property();
			property1.setName("header");
			property1.setCustomValue("true");
			Property property2 = new Property();
			property2.setName("delimiter");
			property2.setCustomValue(",");
			properties.add(property0);
			properties.add(property1);
			properties.add(property2);
			stops.setProperties(properties);
			stopsList.add(stops);
		}

		// paths
		List<Paths> paths = new ArrayList<Paths>();
		{
			Paths pathVo = new Paths();
			pathVo.setFrom("XmlParser");
			pathVo.setOutport("");
			pathVo.setInport("");
			pathVo.setTo("SelectField");
			paths.add(pathVo);
		}
		{
			Paths pathVo = new Paths();
			pathVo.setFrom("SelectField");
			pathVo.setOutport("");
			pathVo.setInport("data1");
			pathVo.setTo("Merge");
			paths.add(pathVo);
		}
		{
			Paths pathVo = new Paths();
			pathVo.setFrom("CsvParser");
			pathVo.setOutport("");
			pathVo.setInport("data2");
			pathVo.setTo("Merge");
			paths.add(pathVo);
		}
		{
			Paths pathVo = new Paths();
			pathVo.setFrom("Merge");
			pathVo.setOutport("");
			pathVo.setInport("");
			pathVo.setTo("Fork");
			paths.add(pathVo);
		}
		{
			Paths pathVo = new Paths();
			pathVo.setFrom("Fork");
			pathVo.setOutport("out1");
			pathVo.setInport("");
			pathVo.setTo("PutHiveStreaming");
			paths.add(pathVo);
		}
		{
			Paths pathVo = new Paths();
			pathVo.setFrom("Fork");
			pathVo.setOutport("out2");
			pathVo.setInport("");
			pathVo.setTo("JsonSave");
			paths.add(pathVo);
		}
		{
			Paths pathVo = new Paths();
			pathVo.setFrom("Fork");
			pathVo.setOutport("out3");
			pathVo.setInport("");
			pathVo.setTo("CsvSave");
			paths.add(pathVo);
		}
		flow.setPathsList(paths);
		flow.setStopsList(stopsList);

		return flow;
	}
}
