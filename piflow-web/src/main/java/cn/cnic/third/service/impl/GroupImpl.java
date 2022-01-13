package cn.cnic.third.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cnic.common.constant.MessageConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.process.domain.ProcessGroupDomain;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.third.service.IGroup;
import cn.cnic.third.utils.ThirdFlowGroupInfoResponseUtils;
import cn.cnic.third.vo.flowGroup.ThirdFlowGroupInfoOutResponse;
import cn.cnic.third.vo.flowGroup.ThirdFlowGroupInfoResponse;
import cn.cnic.third.vo.flowGroup.ThirdFlowInfoOutResponse;
import cn.cnic.third.vo.flowGroup.ThirdFlowStopInfoOutResponse;
import net.sf.json.JSONObject;


@Component
public class GroupImpl implements IGroup {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ProcessGroupDomain processGroupDomain;

    @Override
    public Map<String, Object> startFlowGroup(ProcessGroup processGroup, RunModeType runModeType) {
        if (null == processGroup) {
            return ReturnMapUtils.setFailedMsg("processGroup " + MessageConfig.PARAM_IS_NULL_MSG(MessageConfig.LANGUAGE));
        }
        // String json = ProcessUtil.processGroupToJson(processGroup, runModeType);
        // String formatJson = JsonFormatTool.formatJson(json);
        String formatJson = ProcessUtils.processGroupToJson(processGroup, runModeType);
        logger.info("\n" + formatJson);
        //===============================临时===============================
        //String path = FileUtils.createJsonFile(formatJson, processGroup.getName(), SysParamsCache.VIDEOS_PATH);
        //logger.info(path);
        //String doPost = HttpUtils.doPost(SysParamsCache.getFlowGroupStartUrl(), path, null);
        //===============================临时===============================
        String doPost = HttpUtils.doPost(SysParamsCache.getFlowGroupStartUrl(), formatJson, null);
        logger.info("Return information：" + doPost);
        if (StringUtils.isBlank(doPost)) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG(MessageConfig.LANGUAGE));
        }
        if (doPost.contains(HttpUtils.INTERFACE_CALL_ERROR) || doPost.contains("Exception")) {
            logger.warn("Return information：" + doPost);
            return ReturnMapUtils.setFailedMsg(MessageConfig.INTERFACE_CALL_ERROR_MSG(MessageConfig.LANGUAGE)+ ": " + doPost);
        }
        try {
            JSONObject obj = JSONObject.fromObject(doPost).getJSONObject("group");// Convert a json string to a json object
            String groupId = obj.getString("id");
            if (StringUtils.isNotBlank(groupId)) {
                return ReturnMapUtils.setSucceededCustomParam("appId", groupId);
            } else {
                return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG(MessageConfig.LANGUAGE));
            }
        } catch (Exception e) {
            return ReturnMapUtils.setFailedMsg("Error : Interface call succeeded, conversion error");
        }
    }

    @Override
    public String stopFlowGroup(String processGroupId) {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", processGroupId);
        String json = JSON.toJSON(map).toString();
        String doPost = HttpUtils.doPost(SysParamsCache.getFlowGroupStopUrl(), json, 5 * 1000);
        if (StringUtils.isBlank(doPost) || doPost.contains(HttpUtils.INTERFACE_CALL_ERROR) || doPost.contains("Exception")) {
            logger.warn("Interface return exception");
        } else {
            logger.info("Interface return value: " + doPost);
        }
        return doPost;
    }

    @Override
    public String getFlowGroupInfoStr(String groupId) {
        if (StringUtils.isBlank(groupId)) {
        	return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        String doGet = HttpUtils.doGet(SysParamsCache.getFlowGroupInfoUrl(), map, 5 * 1000);
        return doGet;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ThirdFlowGroupInfoResponse getFlowGroupInfo(String groupId) {
        ThirdFlowGroupInfoResponse thirdFlowGroupInfoResponse = null;
        String doGet = getFlowGroupInfoStr(groupId);
        if (StringUtils.isBlank(doGet)) {
            logger.warn("Interface return values is null");
            return null;
        }
        if (doGet.contains(HttpUtils.INTERFACE_CALL_ERROR) || doGet.contains("Exception")) {
            logger.warn("Interface exception: " + doGet);
            return null;
        }
        // Also convert the json string to a json object, and then convert the json object to a java object, as shown below.
        JSONObject obj = JSONObject.fromObject(doGet);// Convert a json string to a json object
        // Needed when there is a List in jsonObj
        Map<String, Class> classMap = new HashMap<>();
        // Key is the name of the List in jsonObj, and the value is a generic class of list
        classMap.put("flows", ThirdFlowInfoOutResponse.class);
        classMap.put("stops", ThirdFlowStopInfoOutResponse.class);
        classMap.put("groups", ThirdFlowGroupInfoOutResponse.class);
        // Convert a json object to a java object
        ThirdFlowGroupInfoOutResponse thirdFlowGroupInfoOutResponse = (ThirdFlowGroupInfoOutResponse) JSONObject.toBean(obj, ThirdFlowGroupInfoOutResponse.class, classMap);
        if (null == thirdFlowGroupInfoOutResponse) {
            logger.warn("conversion exception");
            return null;
        }
        thirdFlowGroupInfoResponse = thirdFlowGroupInfoOutResponse.getGroup();
        if (null == thirdFlowGroupInfoResponse) {
            logger.warn("conversion exception");
            return null;
        }
        String progressNums = thirdFlowGroupInfoResponse.getProgress();
        if (StringUtils.isNotBlank(progressNums)) {
            try {
                double progressNumsD = Double.parseDouble(progressNums);
                thirdFlowGroupInfoResponse.setProgress(String.format("%.2f", progressNumsD));
            } catch (Throwable e) {
                logger.warn("Progress conversion failed");
            }
        }
        return thirdFlowGroupInfoResponse;

    }

    /**
     * getFlowGroupProgress
     *
     * @param groupId
     * @return
     */
    public Double getFlowGroupProgress(String groupId) {
        if (StringUtils.isBlank(groupId)) {
            logger.warn("groupId is null");
            return null;
        }
        Map<String, String> param = new HashMap<>();
        param.put("groupId", groupId);
        String doGet = HttpUtils.doGet(SysParamsCache.getFlowGroupProgressUrl(), param, 5 * 1000);
        if (StringUtils.isBlank(doGet)) {
            logger.warn("The interface return value is empty.");
            return null;
        }
        if (doGet.contains(HttpUtils.INTERFACE_CALL_ERROR)) {
            logger.warn("Interface exception: " + doGet);
            return null;
        }
        try {
            return Double.parseDouble(doGet);
        } catch (Exception e) {
            logger.error("Conversion exception", e);
            return null;
        }
    }

    /**
     * update FlowGroup By Interface
     *
     * @param groupId
     * @throws Exception 
     */
    @Override
    public void updateFlowGroupByInterface(String groupId) throws Exception {
        ThirdFlowGroupInfoResponse thirdFlowGroupInfoResponse = getFlowGroupInfo(groupId);
        Double flowGroupProgress = getFlowGroupProgress(groupId);
        //Determine if the progress returned by the interface is empty
        if (null == thirdFlowGroupInfoResponse) {
            return;
        }
        ProcessGroup processGroupByAppId = processGroupDomain.getProcessGroupByAppId(groupId);
        if (null == processGroupByAppId) {
            return;
        }
        processGroupByAppId = ThirdFlowGroupInfoResponseUtils.setProcessGroup(processGroupByAppId, thirdFlowGroupInfoResponse);
        if (null == flowGroupProgress || Double.isNaN(flowGroupProgress)) {
            flowGroupProgress = 0.0;
        } else if (Double.isInfinite(flowGroupProgress)) {
            flowGroupProgress = 100.0;
        }
        processGroupByAppId.setProgress(String.format("%.2f", flowGroupProgress));
        processGroupDomain.saveOrUpdateSyncTask(processGroupByAppId);
    }

    /**
     * update FlowGroups By Interface
     *
     * @param groupIds
     * @throws Exception 
     */
    @Override
    public void updateFlowGroupsByInterface(List<String> groupIds) throws Exception {
        if (null != groupIds && groupIds.size() > 0) {
            for (String groupId : groupIds) {
                this.updateFlowGroupByInterface(groupId);
            }
        }
    }

}
