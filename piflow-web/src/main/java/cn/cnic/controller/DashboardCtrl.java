package cn.cnic.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.component.dashboard.service.IResourceService;
import cn.cnic.component.dashboard.service.IStatisticService;

@RestController
@RequestMapping("/dashboard")
public class DashboardCtrl {

    @Autowired
    private IResourceService resourceServiceImpl;

    @Autowired
    private IStatisticService statisticServiceImpl;

    /**
     * resource info,include cpu,memory,disk
     *
     * @return
     */
    @RequestMapping("/resource")
    @ResponseBody
    public String getResourceInfo() {
        String resourceInfo = resourceServiceImpl.getResourceInfo();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("resourceInfo", resourceInfo);
    }

    /**
     * static
     *
     * @return
     */
    @RequestMapping("/flowStatistic")
    @ResponseBody
    public String getFlowStatisticInfo() {
        Map<String, String> flowResourceInfo = statisticServiceImpl.getFlowStatisticInfo();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flowResourceInfo", flowResourceInfo);
    }

    @RequestMapping("/groupStatistic")
    @ResponseBody
    public String getGroupStatisticInfo() {
        Map<String, String> groupResourceInfo = statisticServiceImpl.getGroupStatisticInfo();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("groupResourceInfo", groupResourceInfo);
    }

    @RequestMapping("/scheduleStatistic")
    @ResponseBody
    public String getScheduleStatisticInfo() {
        Map<String, String> scheduleResourceInfo = statisticServiceImpl.getScheduleStatisticInfo();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("scheduleResourceInfo", scheduleResourceInfo);
    }

    @RequestMapping("/templateAndDataSourceStatistic")
    @ResponseBody
    public String getTemplateAndDataSourceStatisticInfo() {
        Map<String, String> tempalateAndDataSourceResourceInfo = statisticServiceImpl.getTemplateAndDataSourceStatisticInfo();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("templateAndDataSourceResourceInfo", tempalateAndDataSourceResourceInfo);
    }

    @RequestMapping("/stopStatistic")
    @ResponseBody
    public String getStopStatisticInfo() {
        Map<String, String> stopResourceInfo = statisticServiceImpl.getStopStatisticInfo();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("stopResourceInfo", stopResourceInfo);
    }
}
