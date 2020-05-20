package com.nature.third;

import com.nature.ApplicationTests;
import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.component.stopsComponent.model.StopsTemplate;
import com.nature.third.service.IStop;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class IStopTest extends ApplicationTests {

    @Autowired
    private IStop stopImpl;

    Logger logger = LoggerUtil.getLogger();


    @Test
    public void testGetAllGroup() {
        String[] allGroup = stopImpl.getAllGroup();
        if (null != allGroup) {
            logger.info(allGroup.toString());
        } else {
            logger.warn("failed");
        }
    }

    @Test
    public void testGetAllStops() {
        String[] allStops = stopImpl.getAllStops();
        if (null != allStops) {
            logger.info(allStops.toString());
        } else {
            logger.warn("failed");
        }
    }

    @Test
    public void testGetStopInfo() {
        String bundle = "cn.piflow.bundle.common.Route";
        StopsTemplate stopInfo = stopImpl.getStopInfo(bundle);
        if (null!=stopInfo){
            logger.info("return value is not null");
        }else {
            logger.warn("return value is null");
        }
    }
    @Test
    public void testS() {
        String reqJson = "{" +
                "\"group\": {" +
                "\"name\": \"FlowGroup\"," +
                "\"uuid\": \"1111111111111\"," +
                "\"flows\": [{" +
                "\"flow\": {" +
                "\"name\": \"one\"," +
                "\"uuid\": \"1234\"," +
                "\"stops\": [{" +
                "\"uuid\": \"1111\"," +
                "\"name\": \"XmlParser\"," +
                "\"bundle\": \"cn.piflow.bundle.xml.XmlParser\"," +
                "\"properties\": {" +
                "\"xmlpath\": \"hdfs://10.0.86.89:9000/xjzhu/dblp.mini.xml\"," +
                "\"rowTag\": \"phdthesis\"" +
                "}" +
                "}," +
                "{" +
                "\"uuid\": \"2222\"," +
                "\"name\": \"SelectField\"," +
                "\"bundle\": \"cn.piflow.bundle.common.SelectField\"," +
                "\"properties\": {" +
                "\"schema\": \"title,author,pages\"" +
                "}" +
                "" +
                "}," +
                "{" +
                "\"uuid\": \"3333\"," +
                "\"name\": \"PutHiveStreaming\"," +
                "\"bundle\": \"cn.piflow.bundle.hive.PutHiveStreaming\"," +
                "\"properties\": {" +
                "\"database\": \"sparktest\"," +
                "\"table\": \"dblp_phdthesis\"" +
                "}" +
                "}" +
                "]," +
                "\"paths\": [{" +
                "\"from\": \"XmlParser\"," +
                "\"outport\": \"\"," +
                "\"inport\": \"\"," +
                "\"to\": \"SelectField\"" +
                "}," +
                "{" +
                "\"from\": \"SelectField\"," +
                "\"outport\": \"\"," +
                "\"inport\": \"\"," +
                "\"to\": \"PutHiveStreaming\"" +
                "}" +
                "]" +
                "}" +
                "}," +
                "{" +
                "\"flow\": {" +
                "\"name\": \"two\"," +
                "\"uuid\": \"5678\"," +
                "\"stops\": [{" +
                "\"uuid\": \"1111\"," +
                "\"name\": \"XmlParser\"," +
                "\"bundle\": \"cn.piflow.bundle.xml.XmlParser\"," +
                "\"properties\": {" +
                "\"xmlpath\": \"hdfs://10.0.86.89:9000/xjzhu/dblp.mini.xml\"," +
                "\"rowTag\": \"phdthesis\"" +
                "}" +
                "}," +
                "{" +
                "\"uuid\": \"2222\"," +
                "\"name\": \"SelectField\"," +
                "\"bundle\": \"cn.piflow.bundle.common.SelectField\"," +
                "\"properties\": {" +
                "\"schema\": \"title,author,pages\"" +
                "}" +
                "" +
                "}," +
                "{" +
                "\"uuid\": \"3333\"," +
                "\"name\": \"PutHiveStreaming\"," +
                "\"bundle\": \"cn.piflow.bundle.hive.PutHiveStreaming\"," +
                "\"properties\": {" +
                "\"database\": \"sparktest\"," +
                "\"table\": \"dblp_phdthesis\"" +
                "}" +
                "}" +
                "]," +
                "\"paths\": [{" +
                "\"from\": \"XmlParser\"," +
                "\"outport\": \"\"," +
                "\"inport\": \"\"," +
                "\"to\": \"SelectField\"" +
                "}," +
                "{" +
                "\"from\": \"SelectField\"," +
                "\"outport\": \"\"," +
                "\"inport\": \"\"," +
                "\"to\": \"PutHiveStreaming\"" +
                "}" +
                "]" +
                "}" +
                "" +
                "}" +
                "]," +
                "" +
                "\"conditions\": [{" +
                "\"entry\": \"two\"," +
                "\"after\": \"one\"" +
                "}" +
                "" +
                "]" +
                "}" +
                "}";
        String url = "http://10.0.86.191:8002/flowGroup/start";
        logger.info(reqJson);
        String doPost = HttpUtils.doPost(url, reqJson, null);
        logger.info(doPost);
    }

}
