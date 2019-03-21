package com.nature.third.impl;

import java.util.HashMap;
import java.util.Map;

import com.nature.base.util.LoggerUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.nature.base.util.HttpUtils;
import com.nature.common.constant.SysParamsCache;
import com.nature.third.inf.IStopFlow;

@Component
public class StopFlowImpl implements IStopFlow {

    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Override
    public String stopFlow(String appId) {
        String encoding = "";
        Map<String, String> map = new HashMap<>();
        map.put("appID", appId);
        String json = JSON.toJSON(map).toString();
        String doPost = HttpUtils.doPost(SysParamsCache.FLOW_STOP_URL(), json, encoding);
        if (StringUtils.isNotBlank(doPost) && !doPost.contains("Exception")) {
            logger.warn("Interface return exception");
        }else {
            logger.info("Interface return value: " + doPost);
        }
        return doPost;
    }
}
