package com.nature.controller;

import com.nature.base.util.DateUtils;
import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.component.workFlow.service.IFlowInfoDbService;
import com.nature.component.workFlow.service.IStopsService;
import com.nature.mapper.FlowInfoDbMapper;
import com.nature.third.inf.IGetFlowInfo;
import com.nature.third.inf.IGetFlowProgress;
import com.nature.third.vo.flow.ThirdProgressVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopsVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;
import org.apache.commons.lang.StringUtils;
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
     * 引入日志，注意都是"org.slf4j"包下
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IFlowInfoDbService flowInfoDbServiceImpl;

    @Autowired
    private IGetFlowProgress iGetFlowProgress;

    @Autowired
    private FlowInfoDbMapper flowInfoDbMapper;

    @Autowired
    private IGetFlowInfo iGetFlowInfo;

    @Autowired
    private IStopsService sStopsServiceImpl;


    /**
     * 查询进度
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
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "0");
        if (null != content && content.length > 0) {
            for (String string : content) {
                list.add(string);
            }
        }
        //通过appId查询数据库,返回list
        List<FlowInfoDb> flowInfoList = flowInfoDbServiceImpl.getFlowInfoByIds(list);
        if (null == flowInfoList && flowInfoList.isEmpty()) {
            return JsonUtils.toJsonNoException(map);
        }
        Map<String, String> progressAndUpdate = null;
        for (FlowInfoDb flowInfoDb : flowInfoList) {
            progressAndUpdate = getProgressAndUpdate(map, flowInfoDb);
            progressAndUpdate.put("code", "1");
        }
        return JsonUtils.toJsonNoException(progressAndUpdate);
    }

    /**
     * 查询进度
     *
     * @param appid
     * @return
     */
    @RequestMapping("/getAppInfoProgress")
    @ResponseBody
    public String getAppInfoProgress(String appid) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "0");
        //通过appId查询数据库
        FlowInfoDb flowInfo = flowInfoDbServiceImpl.getFlowInfoById(appid);
        if (null == flowInfo) {
            return JsonUtils.toJsonNoException(map);
        }
        Map<String, String> result = getProgressAndUpdate(map, flowInfo);
        result.put("code", "1");
        return JsonUtils.toJsonNoException(result);
    }

    /**
     * 调取接口及时更新并返回
     *
     * @param map
     * @param flowInfo
     * @return
     */
    private Map<String, String> getProgressAndUpdate(Map<String, String> map, FlowInfoDb flowInfo) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        //如果进度等于100或者状态是COMPLETED,直接返回
        if (Float.parseFloat(flowInfo.getProgress()) == 100 || "COMPLETED".equals(flowInfo.getState())) {
            map.put("id", flowInfo.getId());
            map.put("progress", flowInfo.getProgress());
            map.put("state", flowInfo.getState());
            map.put("startTime", DateUtils.dateTimeToStr(flowInfo.getStartTime()));
            map.put("endTime", DateUtils.dateTimeToStr(flowInfo.getEndTime()));
            return map;
        }
        //进度接口
        ThirdProgressVo progress = iGetFlowProgress.getFlowProgress(flowInfo.getId());
        //再次调用flowInfo信息,获取开始和结束时间
        ThirdFlowInfoVo thirdFlowInfoVo = iGetFlowInfo.getFlowInfo(flowInfo.getId());
        //如果接口返回进度为符合100,则更新数据库并返回
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
                            //更新stop状态信息
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
