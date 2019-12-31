package com.nature.third.service.impl;

import com.alibaba.fastjson.JSON;
import com.nature.base.util.HttpUtils;
import com.nature.base.util.JsonFormatTool;
import com.nature.base.util.LoggerUtil;
import com.nature.common.Eunm.RunModeType;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.process.model.ProcessGroup;
import com.nature.domain.process.ProcessGroupDomain;
import com.nature.third.service.IGroup;
import com.nature.third.utils.ProcessUtil;
import com.nature.third.utils.ThirdFlowGroupInfoResponseUtils;
import com.nature.third.vo.flowGroup.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GroupImpl implements IGroup {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ProcessGroupDomain processGroupDomain;

    @Override
    public Map<String, Object> startFlowGroup(ProcessGroup processGroup, RunModeType runModeType) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        if (null != processGroup) {
            String json = ProcessUtil.processGroupToJson(processGroup, runModeType);
            String formatJson = JsonFormatTool.formatJson(json);
            logger.info("\n" + formatJson);
            String doPost = HttpUtils.doPost(SysParamsCache.getFlowGroupStartUrl(), formatJson, null);
            logger.info("Return information：" + doPost);
            if (StringUtils.isNotBlank(doPost) && !doPost.contains("Exception")) {
                try {
                    JSONObject obj = JSONObject.fromObject(doPost).getJSONObject("flowGroup");// Convert a json string to a json object
                    String groupId = obj.getString("id");
                    if (StringUtils.isNotBlank(groupId)) {
                        rtnMap.put("appId", groupId);
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
    public String stopFlowGroup(String processGroupId) {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", processGroupId);
        String json = JSON.toJSON(map).toString();
        String doPost = HttpUtils.doPost(SysParamsCache.getFlowGroupStopUrl(), json, 5 * 1000);
        if (StringUtils.isNotBlank(doPost) && !doPost.contains("Exception")) {
            logger.warn("Interface return exception");
        } else {
            logger.info("Interface return value: " + doPost);
        }
        return doPost;
    }

    @Override
    public String getFlowGroupInfoStr(String groupId) {
        String rtnStr = null;
        if (StringUtils.isNotBlank(groupId)) {
            Map<String, String> map = new HashMap<>();
            map.put("groupId", groupId);
            String doGet = HttpUtils.doGet(SysParamsCache.getFlowGroupInfoUrl(), map, 5 * 1000);
            rtnStr = doGet;
        }
        return rtnStr;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public ThirdFlowGroupInfoResponse getFlowGroupInfo(String groupId) {
        ThirdFlowGroupInfoResponse thirdFlowGroupInfoResponse = null;
        String doGet = getFlowGroupInfoStr(groupId);
        if (StringUtils.isNotBlank(doGet) && !doGet.contains("Exception")) {
            // Also convert the json string to a json object, and then convert the json object to a java object, as shown below.
            JSONObject obj = JSONObject.fromObject(doGet);// Convert a json string to a json object
            // Needed when there is a List in jsonObj
            Map<String, Class> classMap = new HashMap<String, Class>();
            // Key is the name of the List in jsonObj, and the value is a generic class of list
            classMap.put("flows", ThirdFlowInfoOutResponse.class);
            classMap.put("stops", ThirdFlowStopInfoOutResponse.class);
            // Convert a json object to a java object
            ThirdFlowGroupInfoOutResponse thirdFlowGroupInfoOutResponse = (ThirdFlowGroupInfoOutResponse) JSONObject.toBean(obj, ThirdFlowGroupInfoOutResponse.class, classMap);
            if (null != thirdFlowGroupInfoOutResponse) {
                thirdFlowGroupInfoResponse = thirdFlowGroupInfoOutResponse.getGroup();
                if (null != thirdFlowGroupInfoResponse) {
                    String progressNums = thirdFlowGroupInfoResponse.getProgress();
                    if (StringUtils.isNotBlank(progressNums)) {
                        try {
                            double progressNumsD = Double.parseDouble(progressNums);
                            thirdFlowGroupInfoResponse.setProgress(String.format("%.2f", progressNumsD));
                        } catch (Throwable e) {
                            logger.warn("Progress conversion failed");
                        }
                    }
                } else {
                    logger.warn("conversion exception");
                }
            } else {
                logger.warn("conversion exception");
            }
        } else {
            logger.warn("Interface exception");
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
     */
    @Override
    @Transactional
    public void updateFlowGroupByInterface(String groupId) {
        ThirdFlowGroupInfoResponse thirdFlowGroupInfoResponse = getFlowGroupInfo(groupId);
        Double flowGroupProgress = getFlowGroupProgress(groupId);
        //Determine if the progress returned by the interface is empty
        if (null != thirdFlowGroupInfoResponse) {
            ProcessGroup processGroupByGroupId = processGroupDomain.getProcessGroupByGroupId(groupId);
            processGroupByGroupId = ThirdFlowGroupInfoResponseUtils.setProcessGroup(processGroupByGroupId, thirdFlowGroupInfoResponse);
            if (null == flowGroupProgress || Double.isNaN(flowGroupProgress)) {
                flowGroupProgress = 0.0;
            } else if (Double.isInfinite(flowGroupProgress)) {
                flowGroupProgress = 100.0;
            }
            processGroupByGroupId.setProgress(String.format("%.2f", flowGroupProgress));
            processGroupDomain.saveOrUpdateSyncTask(processGroupByGroupId);
        }
    }

    /**
     * update FlowGroups By Interface
     *
     * @param groupIds
     */
    @Override
    @Transactional
    public void updateFlowGroupsByInterface(List<String> groupIds) {
        if (null != groupIds && groupIds.size() > 0) {
            for (String groupId : groupIds) {
                this.updateFlowGroupByInterface(groupId);
            }
        }
    }

}
