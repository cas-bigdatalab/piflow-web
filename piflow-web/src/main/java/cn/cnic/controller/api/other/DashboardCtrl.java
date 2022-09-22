package cn.cnic.controller.api.other;

import java.util.Map;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.component.dashboard.service.IResourceService;
import cn.cnic.component.dashboard.service.IStatisticService;
import io.swagger.annotations.Api;

@Api(value = "dashboard api", tags = "dashboard api")
@RestController
@RequestMapping("/dashboard")
public class DashboardCtrl {

    private final IResourceService resourceServiceImpl;
    private final IStatisticService statisticServiceImpl;

    @Autowired
    public DashboardCtrl(IResourceService resourceServiceImpl,
                         IStatisticService statisticServiceImpl) {
        this.resourceServiceImpl = resourceServiceImpl;
        this.statisticServiceImpl = statisticServiceImpl;
    }

    /**
     * resource info,include cpu,memory,disk
     *
     * @return
     */
    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="resource", notes="resource info")
    public String getResourceInfo() {
        String resourceInfo = resourceServiceImpl.getResourceInfo();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("resourceInfo", resourceInfo);
    }

    /**
     * static
     *
     * @return
     */
    @RequestMapping(value = "/flowStatistic", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="flowStatistic", notes="flow Statistic")
    public String getFlowStatisticInfo() {
        Map<String, String> flowResourceInfo = statisticServiceImpl.getFlowStatisticInfo();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flowResourceInfo", flowResourceInfo);
    }

    @RequestMapping(value = "/groupStatistic", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="groupStatistic", notes="group Statistic")
    public String getGroupStatisticInfo() {
        Map<String, String> groupResourceInfo = statisticServiceImpl.getGroupStatisticInfo();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("groupResourceInfo", groupResourceInfo);
    }

    @RequestMapping(value = "/scheduleStatistic", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="scheduleStatistic", notes="schedule Statistic")
    public String getScheduleStatisticInfo() {
        Map<String, String> scheduleResourceInfo = statisticServiceImpl.getScheduleStatisticInfo();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("scheduleResourceInfo", scheduleResourceInfo);
    }

    @RequestMapping(value = "/templateAndDataSourceStatistic", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="scheduleStatistic", notes="schedule Statistic")
    public String getTemplateAndDataSourceStatisticInfo() {
        Map<String, String> templateAndDataSourceResourceInfo = statisticServiceImpl.getTemplateAndDataSourceStatisticInfo();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("templateAndDataSourceResourceInfo", templateAndDataSourceResourceInfo);
    }

    @RequestMapping(value = "/stopStatistic", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="stopStatistic", notes="stop Statistic")
    public String getStopStatisticInfo() {
        Map<String, String> stopResourceInfo = statisticServiceImpl.getStopStatisticInfo();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("stopResourceInfo", stopResourceInfo);
    }
}
