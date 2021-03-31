package cn.cnic.controller.api;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.flow.service.IStopsService;
import cn.cnic.component.stopsComponent.service.IStopGroupService;
import cn.cnic.component.stopsComponent.service.IStopsComponentManageService;
import cn.cnic.component.stopsComponent.vo.StopsComponentManageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "stops api")
@RequestMapping("/stopsManage")
public class StopsManageCtrl {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private IStopGroupService stopGroupServiceImpl;

    @Resource
    private IStopsService stopsServiceImpl;
    
    @Resource
    private IStopsComponentManageService stopsComponentManageServiceImpl;
    
    /**
     * stopsComponentList all
     * 
     * @return
     */
    @RequestMapping(value = "/stopsComponentList", method = RequestMethod.POST)
    @ResponseBody
    public String stopsComponentList() {
    	String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
    	return stopGroupServiceImpl.stopsComponentList(username, isAdmin);
    }

    /**
     * updatestopsComponentIsShow
     * 
     * @return
     * @throws Exception 
     */
    @ApiOperation("StopsComponent Manage")
    @RequestMapping(value = "/updatestopsComponentIsShow", method = RequestMethod.POST)
    @ResponseBody
    public String updatestopsComponentIsShow(StopsComponentManageVo stopsManage) throws Exception {
    	String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
    	return stopsComponentManageServiceImpl.updateStopsComponentIsShow(username, isAdmin,stopsManage);
    }

    /**
     * isNeedSource
     *
     * @return json
     */
    @RequestMapping(value = "/isNeedSource", method = RequestMethod.POST)
    @ResponseBody
    public String isNeedSource(String stopsId) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return stopsServiceImpl.isNeedSource(username, isAdmin, stopsId);
    }

    /**
     * runStops
     *
     * @return json
     */
    @RequestMapping(value = "/runStops", method = RequestMethod.POST)
    @ResponseBody
    public String runStops(String stopsId) {
        return "isNeedSource";
    }
}
