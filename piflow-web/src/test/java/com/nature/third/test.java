package com.nature.third;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class test {

    static Logger logger = LoggerUtil.getLogger();

    public static void main(String[] args) {
        //testFlowGroup();
    }

    public static void testFlowGroup(){
        String url_progress = "http://10.0.86.191:8002/flowGroup/progress";
        String url_info = "http://10.0.86.191:8002/flowGroup/info";
        Map<String,String> param = new HashMap<>();
        param.put("groupId","group_fe835872-8301-45b7-85ac-942a6a23980a");
        String doGet0 = HttpUtils.doGet(url_progress, param, 50000);
        String doGet1 = HttpUtils.doGet(url_info, param, 50000);
        logger.info("-------------------------doGet0------------------------------------");
        logger.info(doGet0);
        logger.info("-------------------------doGet1------------------------------------");
        logger.info(doGet1);
    }
}
