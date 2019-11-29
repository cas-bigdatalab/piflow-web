package com.nature.third;

import com.nature.ApplicationTests;
import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.process.model.Process;
import com.nature.component.process.utils.ProcessUtils;
import com.nature.mapper.flow.FlowMapper;
import com.nature.third.service.IFlow;
import com.nature.third.utils.ProcessUtil;
import com.nature.third.vo.flow.ThirdProgressVo;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

public class IFlowTest extends ApplicationTests {

    @Autowired
    private IFlow flowImpl;

    @Resource
    private FlowMapper flowMapper;

    Logger logger = LoggerUtil.getLogger();

    @Test
    @Transactional
    @Rollback(value = false)
    public void testStartFlow() {
        Flow flowById = flowMapper.getFlowById("0641076d5ae840c09d2be5b71fw00001");
        Process process = ProcessUtils.flowToProcess(flowById, null);
        //flowImpl.startFlow(processById,null, RunModeType.RUN);
        String s = ProcessUtil.processToJson(process, null, RunModeType.DEBUG);
        logger.info(s);
    }

    @Test
    public void testGetFlowProgress() {
        ThirdProgressVo jd = null;
        String appId = "application_1540442049798_0095";
        ThirdProgressVo startFlow2 = flowImpl.getFlowProgress(appId);
        logger.info("Test return information：" + startFlow2);
    }

    @Test
    public void testGetCheckpoints() {
        String checkpoints = flowImpl.getCheckpoints("process_4885bd7e-a369-4531-9649-e41e2d5990b9_1");
        logger.info(checkpoints);
    }

    @Test
    public void testGetFlowLog() {
        String appId = "application_1539850523117_0159";
        String thirdFlowLog = flowImpl.getFlowLog(appId);
    }

    @Test
    public void testStopFlow() {
        String appId = "application_1562293222869_0097";
        String startFlow2 = flowImpl.stopFlow(appId);
        logger.info("Test return information：" + startFlow2);
    }

    @Test
    public void testGetDebugData() {
        String getDebugData = flowImpl.getDebugData("application_1562293222869_0031", "XmlParser", "default");
        //String getDebugData = flowImpl.getDebugData("application_1562293222869_0031","Fork","out2");
        logger.info("Test return information：" + getDebugData);
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
