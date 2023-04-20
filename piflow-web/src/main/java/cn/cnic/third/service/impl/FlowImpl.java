package cn.cnic.third.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cnic.common.Eunm.ComponentFileType;
import cn.cnic.common.constant.ApiConfig;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.process.entity.Process;
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

    private final ProcessDomain processDomain;
    private final StopsComponentDomain stopsComponentDomain;

    @Autowired
    public FlowImpl(ProcessDomain processDomain, StopsComponentDomain stopsComponentDomain) {
        this.processDomain = processDomain;
        this.stopsComponentDomain = stopsComponentDomain;
    }

    /**
     * start process
     *
     * @param process
     * @return
     */
    @Override
    public Map<String, Object> startFlow(Process process, String checkpoint, RunModeType runModeType) {
        if (null == process) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_ERROR_MSG());
        }
        //String json = ProcessUtil.processToJson(process, checkpoint, runModeType);
        //String formatJson = JsonFormatTool.formatJson(json);
        List<ProcessStop> processStopList = process.getProcessStopList();
        if (processStopList == null || processStopList.size() == 0) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("Stop"));
        }else {
            for (ProcessStop processStop : processStopList) {
                StopsComponent stops = stopsComponentDomain.getOnlyStopsComponentByBundle(processStop.getBundel());
                if (stops == null) {
                    return ReturnMapUtils.setFailedMsg(MessageConfig.DATA_ERROR_MSG());
                }
                processStop.setComponentType(stops.getComponentType());
                if (ComponentFileType.PYTHON == processStop.getComponentType()) {
                    processStop.setDockerImagesName(stops.getDockerImagesName());
                }
            }
        }
//        logger.info("==========startFlow::process::"+ JSON.toJSONString(process));
        String formatJson = ProcessUtils.processToJson(process, checkpoint, runModeType, process.getFlowGlobalParamsList());
//        logger.info("====startFlow::formatJson::\n" + formatJson);
        String doPost = HttpUtils.doPost(ApiConfig.getFlowStartUrl(), formatJson, null);
//        logger.info("Return information：" + doPost);
        if (StringUtils.isBlank(doPost)) {
            return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
        }
        if (doPost.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doPost.contains("Exception")) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.INTERFACE_CALL_ERROR_MSG() + " : " + doPost);
        }
        try {
            // Convert a json string to a json object
            JSONObject obj = JSONObject.fromObject(doPost).getJSONObject("flow");
            String appId = obj.getString("id");
            if (StringUtils.isBlank(appId)) {
                return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
            }
            return ReturnMapUtils.setSucceededCustomParam("appId", appId);
        } catch (Exception e) {
            logger.error("error: ", e);
            return ReturnMapUtils.setFailedMsg(MessageConfig.CONVERSION_FAILED_MSG());
        }
    }

    @Override
    public String stopFlow(String appId) {
        Map<String, String> map = new HashMap<>();
        map.put("appID", appId);
        String json = JSONObject.fromObject(map).toString();
        String doPost = HttpUtils.doPost(ApiConfig.getFlowStopUrl(), json, 5 * 1000);
        if (StringUtils.isBlank(doPost) || doPost.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doPost.contains("Exception")) {
            logger.warn("Interface return exception : " + doPost);
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
        Map<String, String> map = new HashMap<>();
        map.put("appID", appId);
        String doGet = HttpUtils.doGet(ApiConfig.getFlowProgressUrl(), map, 10 * 1000);
        if (StringUtils.isBlank(doGet) || doGet.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doGet.contains("Exception")) {
            logger.warn(MessageConfig.INTERFACE_CALL_ERROR_MSG() + ": " + doGet);
            return null;
        }
        String jsonResult = JSONObject.fromObject(doGet).getString("FlowInfo");
        if (StringUtils.isNotBlank(jsonResult)) {
            return null;
        }
        // Also convert the json string to a json object, 
        // and then convert the json object to a java object, 
        // as shown below.

        // Convert a json string to a json object
        JSONObject obj = JSONObject.fromObject(jsonResult);
        // Convert a json object to a java object
        ThirdProgressVo jd = (ThirdProgressVo) JSONObject.toBean(obj, ThirdProgressVo.class);
        String progressNums = jd.getProgress();
        if (StringUtils.isNotBlank(progressNums)) {
            try {
                double progressNumsD = Double.parseDouble(progressNums);
                jd.setProgress(String.format("%.2f", progressNumsD));
            } catch (Throwable e) {
                logger.warn("Progress conversion failed");
            }
        }
        return jd;
    }

    /**
     * send post request
     */
    @Override
    public String getFlowLog(String appId) {
        // ThirdFlowLog thirdFlowLog = null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("appID", appId);
        String doGet = HttpUtils.doGet(ApiConfig.getFlowLogUrl(), map, 5 * 1000);
        if (StringUtils.isBlank(doGet)) {
            logger.info("call failed, return is null ");
            return "";
        }
        if (doGet.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doGet.contains("Exception")) {
            logger.info("call failed : " + doGet);
            return "";
        }
        logger.info("Successful call : " + doGet);
        // Also convert the json string to a json object, 
        // and then convert the json object to a java object, 
        // as shown below.

        // Convert a json string to a json object
        JSONObject obj = JSONObject.fromObject(doGet);
        if (null == obj) {
            return "";
        }
        JSONObject app = obj.getJSONObject("app");
        if (null == app) {
            return "";
        }
        String amContainerLogs = app.getString("amContainerLogs");
        // Convert a json object to a java object
        // thirdFlowLog = (ThirdFlowLog) JSONObject.toBean(obj, ThirdFlowLog.class);
        // return thirdFlowLog;
        return amContainerLogs;
    }

    /**
     * send get
     */
    @Override
    public String getCheckpoints(String appID) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appID", appID);
        String doGet = HttpUtils.doGet(ApiConfig.getFlowCheckpointsUrl(), map, 5 * 1000);
        if (StringUtils.isBlank(doGet)) {
            logger.warn(MessageConfig.INTERFACE_CALL_ERROR_MSG() + " return is null ");
            return null;
        }
        if (doGet.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doGet.contains("Exception")) {
            logger.warn(MessageConfig.INTERFACE_CALL_ERROR_MSG() + ": " + doGet);
            return null;
        }
        // Also convert the json string to a json object, 
        // and then convert the json object to a java object, 
        // as shown below.

        // Convert a json string to a json object
        JSONObject obj = JSONObject.fromObject(doGet);
        if (null == obj) {
            return null;
        }
        String jb = obj.getString("checkpoints");
        return jb;
    }

    @Override
    public String getDebugData(String appID, String stopName, String portName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appID", appID);
        map.put("stopName", stopName);
        map.put("port", portName);
        String doGet = HttpUtils.doGet(ApiConfig.getFlowDebugDataUrl(), map, 5 * 1000);
        logger.info("call succeeded : " + doGet);
        if (StringUtils.isBlank(doGet)) {
            return MessageConfig.INTERFACE_CALL_ERROR_MSG() + " return is null ";
        }
        if (doGet.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doGet.contains("Exception")) {
            return MessageConfig.INTERFACE_CALL_ERROR_MSG() + ": " + doGet;
        }
        // Also convert the json string to a json object, 
        // and then convert the json object to a java object, 
        // as shown below.
        // JSONObject obj = JSONObject.fromObject(doGet);// Convert a json string to a json object
        // if (null != obj) {
        //     jb = obj.getString("checkpoints");
        // }
        return doGet;
    }

    @Override
    public String getVisualizationData(String appID, String stopName, String visualizationType) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appID", appID);
        map.put("stopName", stopName);
        map.put("visualizationType", visualizationType);
        String doGet = HttpUtils.doGet(ApiConfig.getFlowVisualizationDataUrl(), map, 5 * 1000);
        logger.info("call succeeded : " + doGet);
        if (StringUtils.isBlank(doGet)) {
            logger.warn(MessageConfig.INTERFACE_CALL_ERROR_MSG() + " return is null ");
            return null;
        }
		if (doGet.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doGet.contains("Exception")) {
            logger.warn(MessageConfig.INTERFACE_CALL_ERROR_MSG() + ": " + doGet);
            return null;
        }
        return doGet;
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
        String doGet = HttpUtils.doGet(ApiConfig.getFlowInfoUrl(), map, 30 * 1000);
        if (StringUtils.isBlank(doGet)) {
            logger.warn(MessageConfig.INTERFACE_CALL_ERROR_MSG() + " return is null ");
            return null;
        }
        if (doGet.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doGet.contains("Exception")) {
            logger.warn(MessageConfig.INTERFACE_CALL_ERROR_MSG() + ": " + doGet);
            return null;
        }
        // Also convert the json string to a json object, 
        // and then convert the json object to a java object, 
        // as shown below.

        // Convert a json string to a json object
        JSONObject obj = JSONObject.fromObject(doGet).getJSONObject("flow");
        // Needed when there is a List in jsonObj
        Map<String, Class> classMap = new HashMap<String, Class>();
        // Key is the name of the List in jsonObj, 
        // and the value is a generic class of list
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
        return jb;
    }

    @Override
    public void getProcessInfoAndSave(String appId) throws Exception {
        ThirdFlowInfoVo thirdFlowInfoVo = getFlowInfo(appId);
        // Determine if the progress returned by the interface is empty
        if (null != thirdFlowInfoVo) {
            List<Process> processList = processDomain.getProcessNoGroupByAppId(appId);
            for (Process process : processList) {
                process = ThirdFlowInfoVoUtils.setProcess(process, thirdFlowInfoVo);
                if (null != process) {
                    processDomain.saveOrUpdate(process);
                }
            }
        }

    }

    @Override
    public void processInfoAndSaveSync() throws Exception {
        List<String> runningProcess = processDomain.getRunningProcessAppId();
        if (CollectionUtils.isEmpty(runningProcess)) {
            return;
        }
        for (String appId : runningProcess) {
            getProcessInfoAndSave(appId);
        }
        
    }

    @Override
    public String getTestDataPathUrl() {
        return HttpUtils.doGet(ApiConfig.getTestDataPathUrl(), null, null);
    }

}
