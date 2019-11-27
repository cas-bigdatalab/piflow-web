package com.nature.controller;

import com.nature.base.util.DateUtils;
import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.FlowInfoDb;
import com.nature.component.flow.service.IFlowInfoDbService;
import com.nature.component.flow.service.IStopsService;
import com.nature.mapper.flow.FlowInfoDbMapper;
import com.nature.third.service.IFlow;
import com.nature.third.service.IGetFlowInfo;
import com.nature.third.vo.flow.ThirdProgressVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopsVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/flowInfoDb")
public class FlowInfoDbCtrl {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IFlowInfoDbService flowInfoDbServiceImpl;

    @Autowired
    private IFlow flowImpl;

    @Autowired
    private FlowInfoDbMapper flowInfoDbMapper;

    @Autowired
    private IGetFlowInfo iGetFlowInfo;

    @Autowired
    private IStopsService sStopsServiceImpl;


    /**
     * Query progress
     *
     * @param content
     * @param content
     * @return
     */
    @SuppressWarnings("null")
    @RequestMapping("/list")
    @ResponseBody
    public String findAppInfo(String[] content) {
        List<String> list = new ArrayList<String>();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 500);
        if (null != content && content.length > 0) {
            for (String string : content) {
                list.add(string);
            }
        }
        //Query the database by appId, return list
        List<FlowInfoDb> flowInfoList = flowInfoDbServiceImpl.getFlowInfoByIds(list);
        if (null == flowInfoList && flowInfoList.isEmpty()) {
            return JsonUtils.toJsonNoException(map);
        }
        Map<String, Object> progressAndUpdate = null;
        for (FlowInfoDb flowInfoDb : flowInfoList) {
            progressAndUpdate = getProgressAndUpdate(map, flowInfoDb);
            progressAndUpdate.put("code", 500);
        }
        return JsonUtils.toJsonNoException(progressAndUpdate);
    }

    /**
     * Query progress
     *
     * @param appid
     * @return
     */
    @RequestMapping("/getAppInfoProgress")
    @ResponseBody
    public String getAppInfoProgress(String appid) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 500);
        //Query the database by appId
        FlowInfoDb flowInfo = flowInfoDbServiceImpl.getFlowInfoById(appid);
        if (null == flowInfo) {
            return JsonUtils.toJsonNoException(map);
        }
        Map<String, Object> result = getProgressAndUpdate(map, flowInfo);
        result.put("code", 200);
        return JsonUtils.toJsonNoException(result);
    }

    /**
     * Retrieve the interface to update and return
     *
     * @param map
     * @param flowInfo
     * @return
     */
    private Map<String, Object> getProgressAndUpdate(Map<String, Object> map, FlowInfoDb flowInfo) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        //If the progress is equal to 100 or the status is COMPLETED, return directly
        if (Float.parseFloat(flowInfo.getProgress()) == 100 || "COMPLETED".equals(flowInfo.getState())) {
            map.put("id", flowInfo.getId());
            map.put("progress", flowInfo.getProgress());
            map.put("state", flowInfo.getState());
            map.put("startTime", DateUtils.dateTimeToStr(flowInfo.getStartTime()));
            map.put("endTime", DateUtils.dateTimeToStr(flowInfo.getEndTime()));
            return map;
        }
        //Progress interface
        ThirdProgressVo progress = flowImpl.getFlowProgress(flowInfo.getId());
        //Call flowInfo information again to get the start and end time
        ThirdFlowInfoVo thirdFlowInfoVo = iGetFlowInfo.getFlowInfo(flowInfo.getId());
        //If the interface returns to a progress of 100, update the database and return
        if (StringUtils.isNotBlank(progress.getProgress()) || !"STARTED".equals(progress.getState()) || Float.parseFloat(progress.getProgress()) > Float.parseFloat(flowInfo.getProgress())) {
            FlowInfoDb up = new FlowInfoDb();
            if (null != thirdFlowInfoVo) {
                up.setEndTime(DateUtils.strCstToDate(thirdFlowInfoVo.getEndTime()));
                up.setStartTime(DateUtils.strCstToDate(thirdFlowInfoVo.getStartTime()));
                up.setName(thirdFlowInfoVo.getName());
                List<ThirdFlowInfoStopsVo> stops = thirdFlowInfoVo.getStops();
                if (stops.size() > 0 && !stops.isEmpty()) {
                    for (ThirdFlowInfoStopsVo thirdFlowInfoStopVo : stops) {
                        if (null != thirdFlowInfoStopVo.getStop()) {
                            //Update stop status information
                            ThirdFlowInfoStopVo stop = thirdFlowInfoStopVo.getStop();
                            stop.setFlowId(flowInfo.getFlow().getId());
                            sStopsServiceImpl.updateStopsByFlowIdAndName(stop);
                        }
                    }
                }
            }
            up.setName(progress.getName());
            up.setId(flowInfo.getId());
            up.setState(StringUtils.isNotBlank(progress.getState()) ? progress.getState() : "FAILED");
            up.setLastUpdateDttm(new Date());
            up.setLastUpdateUser(username);
            if (null == progress.getProgress() || "NaN".equals(progress.getProgress())) {
                up.setProgress("0");
            } else {
                up.setProgress(progress.getProgress());
            }
            flowInfoDbMapper.updateFlowInfo(up);
        }
        map.put("id", flowInfo.getId());
        map.put("progress", "NaN".equals(progress.getProgress()) ? "0.00" : progress.getProgress());
        map.put("state", progress.getState());
        if (null != thirdFlowInfoVo) {
            map.put("startTime", thirdFlowInfoVo.getStartTime());
            map.put("endTime", thirdFlowInfoVo.getEndTime());
        }
        return map;
    }

}
