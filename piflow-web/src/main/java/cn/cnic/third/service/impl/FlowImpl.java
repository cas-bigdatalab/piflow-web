package cn.cnic.third.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.jpa.domain.ProcessDomain;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.third.service.IFlow;
import cn.cnic.third.utils.ThirdFlowInfoVoUtils;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopsVo;
import cn.cnic.third.vo.flow.ThirdFlowInfoVo;
import cn.cnic.third.vo.flow.ThirdProgressVo;
import net.sf.json.JSONObject;

@Component
public class FlowImpl implements IFlow {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ProcessDomain processDomain;

    /**
     * start process
     *
     * @param process
     * @return
     */
    @Override
    public Map<String, Object> startFlow(Process process, String checkpoint, RunModeType runModeType) {
        if (null == process) {
            return ReturnMapUtils.setFailedMsg("process is null");
        }
        /*String json = ProcessUtil.processToJson(process, checkpoint, runModeType);
        String formatJson = JsonFormatTool.formatJson(json);*/
        String formatJson = ProcessUtils.processToJson(process, checkpoint, runModeType);
        logger.info("\n" + formatJson);
        String doPost = HttpUtils.doPost(SysParamsCache.getFlowStartUrl(), formatJson, null);
        logger.info("Return information：" + doPost);
        if (StringUtils.isBlank(doPost) || doPost.contains("Exception")) {
            return ReturnMapUtils.setFailedMsg("Error : Interface call failed");
        }
        try {
            JSONObject obj = JSONObject.fromObject(doPost).getJSONObject("flow");// Convert a json string to a json object
            String appId = obj.getString("id");
            if(StringUtils.isBlank(appId)){
                return ReturnMapUtils.setFailedMsg("Error : Interface return value is null");
            }
            return ReturnMapUtils.setSucceededCustomParam("appId", appId);
        } catch (Exception e) {
            logger.error("error: ", e);
            return ReturnMapUtils.setFailedMsg("Error : Interface call succeeded, conversion error");
        }
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
        Map<String, String> map = new HashMap<>();
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

    @Override
    public String getVisualizationData(String appID, String stopName, String visualizationType ) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appID", appID);
        map.put("stopName", stopName);
        map.put("visualizationType",visualizationType);
        String doGet = HttpUtils.doGet(SysParamsCache.getFlowVisualizationDataUrl(), map, 5 * 1000);
        logger.info("call succeeded : " + doGet);
        if (StringUtils.isNotBlank(doGet) && !doGet.contains("Exception")) {
            return doGet;
        }
        return null;
    }


    /**
     * Send post request
     */
    @SuppressWarnings("rawtypes")
    @Override
    public ThirdFlowInfoVo getFlowInfo(String appId) {
        ThirdFlowInfoVo jb = null;
        Map<String, String> map = new HashMap<>();
        map.put("appID", appId);
        String doGet = HttpUtils.doGet(SysParamsCache.getFlowInfoUrl(), map, 30 * 1000);
        if (StringUtils.isNotBlank(doGet) && !doGet.contains("Exception")) {
            // Also convert the json string to a json object, and then convert the json object to a java object, as shown below.
            JSONObject obj = JSONObject.fromObject(doGet).getJSONObject("flow");// Convert a json string to a json object
            // Needed when there is a List in jsonObj
            Map<String, Class> classMap = new HashMap<String, Class>();
            // Key is the name of the List in jsonObj, and the value is a generic class of list
            classMap.put("stops", ThirdFlowInfoStopsVo.class);
            // Convert a json object to a java object
            jb = (ThirdFlowInfoVo) JSONObject.toBean(obj, ThirdFlowInfoVo.class, classMap);
            String progressNums = jb.getProgress();
            if (StringUtils.isNotBlank(progressNums)) {
                try {
                    double progressNumsD = Double.parseDouble(progressNums);
                    jb.setProgress(String.format("%.2f", progressNumsD));
                } catch (Throwable e) {
                    logger.warn("Progress conversion failed");
                }
            }
        }
        return jb;
    }

    @Override
    @Transactional
    public void getProcessInfoAndSave(String appid) {
        ThirdFlowInfoVo thirdFlowInfoVo = getFlowInfo(appid);
        //Determine if the progress returned by the interface is empty
        if (null != thirdFlowInfoVo) {
            Process processByAppId = processDomain.getProcessNoGroupByAppId(appid);
            processByAppId = ThirdFlowInfoVoUtils.setProcess(processByAppId, thirdFlowInfoVo);
            if (null != processByAppId) {
                processDomain.saveOrUpdate(processByAppId);
            }
        }

    }

    @Override
    public String getTestDataPathUrl() {
        return HttpUtils.doGet(SysParamsCache.getTestDataPathUrl(), null, null);
    }

}
