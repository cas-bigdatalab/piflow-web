package cn.cnic.third;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.HttpUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.third.service.IStop;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


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
    public void testGetStopsListWithGroup() {
        Map<String, List<String>> stopsListWithGroup = stopImpl.getStopsListWithGroup();
        logger.debug(stopsListWithGroup.toString());
    }

    @Test
    public void testGetStopInfo() {
        //String bundle1 = "cn.piflow.bundle.mongodb.GetMongo";
        String bundle = "cn.piflow.bundle.visualization.LineChart";
        ThirdStopsComponentVo thirdStopsComponentVo = stopImpl.getStopInfo(bundle);
        if (null != thirdStopsComponentVo) {
            logger.info("return value is not null");
        } else {
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
