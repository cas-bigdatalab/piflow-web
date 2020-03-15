package com.nature.third.service.impl;

import com.alibaba.fastjson.JSON;
import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.common.Eunm.RunModeType;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.process.model.Process;
import com.nature.component.process.utils.ProcessUtils;
import com.nature.third.service.IFlow;
import com.nature.third.vo.flow.ThirdProgressVo;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FlowImpl implements IFlow {

    Logger logger = LoggerUtil.getLogger();

    /**
     * start process
     *
     * @param process
     * @return
     */
    @Override
    public Map<String, Object> startFlow(Process process, String checkpoint, RunModeType runModeType) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        if (null != process) {
            /*String json = ProcessUtil.processToJson(process, checkpoint, runModeType);
            String formatJson = JsonFormatTool.formatJson(json);*/
            String formatJson = ProcessUtils.processToJson(process, checkpoint, runModeType);
            logger.info("\n" + formatJson);
            String doPost = HttpUtils.doPost(SysParamsCache.getFlowStartUrl(), formatJson, null);
            logger.info("Return information：" + doPost);
            if (StringUtils.isNotBlank(doPost) && !doPost.contains("Exception")) {
                try {
                    JSONObject obj = JSONObject.fromObject(doPost).getJSONObject("flow");// Convert a json string to a json object
                    String appId = obj.getString("id");
                    if (StringUtils.isNotBlank(appId)) {
                        rtnMap.put("appId", appId);
                        rtnMap.put("code", 200);
                    } else {
                        rtnMap.put("errorMsg", "Error : Interface return value is null");
                    }
                } catch (Exception e) {
                    rtnMap.put("errorMsg", "Error : Interface call succeeded, conversion error");
                }
            } else {
                rtnMap.put("errorMsg", "Error : Interface call failed");
            }
        }
        return rtnMap;
    }

    @Override
    public String stopFlow(String appId) {
        Map<String, String> map = new HashMap<>();
        map.put("appID", appId);
        String json = JSON.toJSON(map).toString();
        String doPost = HttpUtils.doPost(SysParamsCache.getFlowStopUrl(), json, 5 * 1000);
        if (StringUtils.isNotBlank(doPost) && !doPost.contains("Exception")) {
            logger.warn("Interface return exception");
        } else {
            logger.info("Interface return value: " + doPost);
        }
        return doPost;
    }

    /**
     * send post request
     */
    @Override
    public ThirdProgressVo getFlowProgress(String appId) {
        ThirdProgressVo jd = null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("appID", appId);
        String doGet = HttpUtils.doGet(SysParamsCache.getFlowProgressUrl(), map, 10 * 1000);
        if (StringUtils.isNotBlank(doGet) && !doGet.contains("Exception")) {
            String jsonResult = JSONObject.fromObject(doGet).getString("FlowInfo");
            if (StringUtils.isNotBlank(jsonResult)) {
                // Also convert the json string to a json object, and then convert the json object to a java object, as shown below.
                JSONObject obj = JSONObject.fromObject(jsonResult);// Convert a json string to a json object
                // Convert a json object to a java object
                jd = (ThirdProgressVo) JSONObject.toBean(obj, ThirdProgressVo.class);
                String progressNums = jd.getProgress();
                if (StringUtils.isNotBlank(progressNums)) {
                    try {
                        double progressNumsD = Double.parseDouble(progressNums);
                        jd.setProgress(String.format("%.2f", progressNumsD));
                    } catch (Throwable e) {
                        logger.warn("Progress conversion failed");
                    }
                }
            }
        }
        return jd;
    }

    /**
     * send post request
     */
    @Override
    public String getFlowLog(String appId) {
        //ThirdFlowLog thirdFlowLog = null;
        String amContainerLogs = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("appID", appId);
        String doGet = HttpUtils.doGet(SysParamsCache.getFlowLogUrl(), map, 5 * 1000);
        if (StringUtils.isNotBlank(doGet) && !doGet.contains("Exception")) {
            logger.info("Successful call : " + doGet);
            // Also convert the json string to a json object, and then convert the json object to a java object, as shown below.
            JSONObject obj = JSONObject.fromObject(doGet);// Convert a json string to a json object
            if (null != obj) {
                JSONObject app = obj.getJSONObject("app");
                if (null != app) {
                    amContainerLogs = app.getString("amContainerLogs");
                }
            }
            // Convert a json object to a java object
            // thirdFlowLog = (ThirdFlowLog) JSONObject.toBean(obj, ThirdFlowLog.class);
        } else {
            logger.info("call failed : " + doGet);
        }
        // return thirdFlowLog;
        return amContainerLogs;
    }

    /**
     * send get
     */
    @Override
    public String getCheckpoints(String appID) {
        String jb = null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("appID", appID);
        String doGet = HttpUtils.doGet(SysParamsCache.getFlowCheckpointsUrl(), map, 5 * 1000);
        if (StringUtils.isNotBlank(doGet) && !doGet.contains("Exception")) {
            // Also convert the json string to a json object, and then convert the json object to a java object, as shown below.
            JSONObject obj = JSONObject.fromObject(doGet);// Convert a json string to a json object
            if (null != obj) {
                jb = obj.getString("checkpoints");
            }
        }
        return jb;
    }

    @Override
    public String getDebugData(String appID, String stopName, String portName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appID", appID);
        map.put("stopName", stopName);
        map.put("port", portName);
        String doGet = HttpUtils.doGet(SysParamsCache.getFlowDebugDataUrl(), map, 5 * 1000);
        logger.info("call succeeded : " + doGet);
        if (StringUtils.isNotBlank(doGet) && !doGet.contains("Exception")) {
            // Also convert the json string to a json object, and then convert the json object to a java object, as shown below.
//            JSONObject obj = JSONObject.fromObject(doGet);// Convert a json string to a json object
//            if (null != obj) {
//                jb = obj.getString("checkpoints");
//            }
        }
        return doGet;
    }

}
