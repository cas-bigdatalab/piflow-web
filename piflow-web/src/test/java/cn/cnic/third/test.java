package cn.cnic.third;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.common.constant.ApiConfig;
import cn.cnic.common.constant.MessageConfig;
import com.alibaba.fastjson2.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;


public class test {

    private static Logger logger = LoggerUtil.getLogger();

    public static void main(String[] args) {
//        String doPost = "{\n" +
//                "\t\"code\": 0,\n" +
//                "\t\"data\": {},\n" +
//                "\t\"message\": \"操作成功\"\n" +
//                "}";
//        try {
//            // Convert a json string to a json object
//            JSONObject obj = JSONObject.fromObject(doPost);
//            String code = obj.getString("code");
//            if (StringUtils.isBlank(code)) {
//                logger.info(ReturnMapUtils.setFailedMsgRtnJsonStr("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG()));
//            }
//            logger.info(ReturnMapUtils.setFailedMsgRtnJsonStr("error"));
//        } catch (Exception e) {
//            logger.error("error: ", e);
//            logger.info(ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.CONVERSION_FAILED_MSG()));
//        }
//        testJsonParser();
        sendToFairManTest();
    }

    public static void sendToFairManTest(){
        Map<String, Object> param = new HashMap<>();
        param.put("userName", "admin");
        param.put("softwareId", "621f4987583197d50685102b");
        param.put("softwareName", "πFlow");
        param.put("softwareVersion", "V1.6");
        Map<String,Integer> softwareData = new HashMap<>();
        softwareData.put("components",0);
        softwareData.put("pipelines",0);
        softwareData.put("process",0);
        softwareData.put("CPU",0);
        softwareData.put("memory",0);
        softwareData.put("storage",0);
        param.put("softwareData", softwareData);
        HttpUtils.doPost("https://market.casdc.cn/api/v2/OpenApi/software", JSON.toJSONString(param), 10 * 1000);
        System.out.println("===========================================================");
    }

    public static void testJsonParser(){
        String resource = "{\n" +
                "    \"code\": 200,\n" +
                "    \"resourceInfo\": \"{\\n    \\\"resource\\\": {\\n        \\\"cpu\\\": {\\n            \\\"totalVirtualCores\\\": 180.0,\\n            \\\"allocatedVirtualCores\\\": 0.0,\\n            \\\"remainingVirtualCores\\\": 180.0\\n        },\\n        \\\"memory\\\": {\\n            \\\"totalMemoryGB\\\": 600.0,\\n            \\\"allocatedMemoryGB\\\": 0.0,\\n            \\\"remainingMemoryGB\\\": 600.0\\n        },\\n        \\\"hdfs\\\": {\\n            \\\"TotalCapacityGB\\\": 4943,\\n            \\\"remainingCapacityGB\\\": 4241,\\n            \\\"allocatedCapacityGB\\\": 702\\n        }\\n    }\\n}\",\n" +
                "    \"errorMsg\": \"Succeeded\"\n" +
                "}";
        JSONObject resourceObject = JSONObject.fromObject(resource);
        if(resourceObject.getInt("code")==200){
            JSONObject resourceInfo = JSONObject.fromObject(JSONObject.fromObject(resourceObject.get("resourceInfo")).get("resource"));
            JSONObject cpu = resourceInfo.getJSONObject("cpu");
            int cpuTotalVirtualCores = cpu.getInt("totalVirtualCores");

            JSONObject memory = resourceInfo.getJSONObject("memory");
            int memoryTotalVirtualCores = memory.getInt("totalMemoryGB");

            JSONObject hdfs = resourceInfo.getJSONObject("hdfs");
            int hdfsTotalVirtualCores = hdfs.getInt("TotalCapacityGB");

        }
        System.out.println("=============================");
    }
}
