package com.nature.third.service.impl;

import com.nature.base.util.DateUtils;
import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.FlowState;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowInfoDb;
import com.nature.component.process.model.Process;
import com.nature.mapper.flow.FlowInfoDbMapper;
import com.nature.mapper.process.ProcessMapper;
import com.nature.third.service.IFlow;
import com.nature.third.service.IGetFlowInfo;
import com.nature.third.utils.ThirdFlowInfoVoUtils;
import com.nature.third.vo.flow.ThirdProgressVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopsVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;
import com.nature.transaction.process.ProcessTransaction;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class GetFlowInfoImpl implements IGetFlowInfo {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private FlowInfoDbMapper flowInfoDbMapper;

    @Resource
    private ProcessMapper processMapper;

    @Autowired
    private IFlow flowImpl;

    @Autowired
    private ProcessTransaction processTransaction;


    /**
     * Send post request
     */
    @Override
    public ThirdFlowInfoVo getFlowInfo(String appid) {
        ThirdFlowInfoVo jb = null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("appID", appid);
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

    /**
     * Return flowInfo and save the database through the appId interface
     */
    @Override
    public FlowInfoDb AddFlowInfo(String appId, Flow flow) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        ThirdFlowInfoVo startFlow2 = getFlowInfo(appId);
        ThirdProgressVo progress = flowImpl.getFlowProgress(appId);
        logger.info("Test return information：" + startFlow2);
        if (null != startFlow2 && null != startFlow2.getId()) {
            FlowInfoDb flowInfoDb = flowInfoDbMapper.flowInfoDb(startFlow2.getId());
            //Update the data returned by the interface if the database is not empty
            if (null != flowInfoDb) {
                FlowInfoDb up = new FlowInfoDb();
                up.setId(flowInfoDb.getId());
                up.setName(startFlow2.getName());
                up.setState(startFlow2.getState());
                up.setEndTime(DateUtils.strCstToDate(startFlow2.getEndTime()));
                up.setStartTime(DateUtils.strCstToDate(startFlow2.getStartTime()));
                if (null == progress.getProgress() || "NaN".equals(progress.getProgress())) {
                    up.setProgress("0");
                } else {
                    up.setProgress(progress.getProgress());
                }
                up.setLastUpdateUser(username);
                up.setLastUpdateDttm(new Date());
                int updateFlowInfo = flowInfoDbMapper.updateFlowInfo(up);
                if (updateFlowInfo > 0) {
                    return up;
                }
            } else {
                //If the interface returns is not empty, the database is saved.
                FlowInfoDb add = new FlowInfoDb();
                add.setId(startFlow2.getId());
                add.setName(startFlow2.getName());
                add.setState(progress.getState());
                add.setEndTime(DateUtils.strCstToDate(startFlow2.getEndTime()));
                add.setStartTime(DateUtils.strCstToDate(startFlow2.getStartTime()));
                add.setProgress(progress.getProgress());
                add.setCrtDttm(new Date());
                add.setCrtUser(username);
                add.setEnableFlag(true);
                add.setLastUpdateUser(username);
                add.setLastUpdateDttm(new Date());
                add.setFlow(flow);
                int addFlowInfo = flowInfoDbMapper.addFlowInfo(add);
                if (addFlowInfo > 0) {
                    return add;
                }
            }
        }
        FlowInfoDb kong = new FlowInfoDb();
        //The flowInfo interface returns empty
        kong.setId(appId);
        kong.setCrtDttm(new Date());
        kong.setCrtUser(username);
        kong.setEnableFlag(true);
        kong.setLastUpdateUser(username);
        kong.setProgress("0");
        kong.setState(FlowState.STARTED.toString());
        kong.setLastUpdateDttm(new Date());
        kong.setFlow(flow);
        flowInfoDbMapper.addFlowInfo(kong);
        return kong;
    }

}
