package com.nature.third.service.impl;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.process.model.Process;
import com.nature.mapper.process.ProcessMapper;
import com.nature.third.service.IGetFlowInfo;
import com.nature.third.utils.ThirdFlowInfoVoUtils;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopsVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;
import com.nature.transaction.process.ProcessTransaction;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class GetFlowInfoImpl implements IGetFlowInfo {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessMapper processMapper;

    @Autowired
    private ProcessTransaction processTransaction;


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
    public void getProcessInfoAndSave(String appid) {
        ThirdFlowInfoVo thirdFlowInfoVo = getFlowInfo(appid);
        //Determine if the progress returned by the interface is empty
        if (null != thirdFlowInfoVo) {
            Process processByAppId = processMapper.getProcessNoGroupByAppId(appid);
            processByAppId = ThirdFlowInfoVoUtils.setProcess(processByAppId, thirdFlowInfoVo);
            processByAppId.setProcessPathList(null);
            processTransaction.updateProcessAll(processByAppId);
        }

    }

}
