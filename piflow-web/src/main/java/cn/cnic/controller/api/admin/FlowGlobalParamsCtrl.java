package cn.cnic.controller.api.admin;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.flow.service.IFlowGlobalParamsService;
import cn.cnic.controller.requestVo.FlowGlobalParamsVoRequest;
import cn.cnic.controller.requestVo.FlowGlobalParamsVoRequestAdd;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Api(value = "FlowGlobalParams api", tags = "FlowGlobalParams api")
@RestController
@RequestMapping("/flowGlobalParams")
public class FlowGlobalParamsCtrl {

    private final IFlowGlobalParamsService flowGlobalParamsServiceImpl;

    @Autowired
    public FlowGlobalParamsCtrl(IFlowGlobalParamsService flowGlobalParamsServiceImpl) {
        this.flowGlobalParamsServiceImpl = flowGlobalParamsServiceImpl;
    }


    /**
     * Query and enter the GlobalParams list
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping(value = "/globalParamsListPage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="globalParamsListPage", notes="global params list")
    @ApiImplicitParams({ 
        @ApiImplicitParam(name = "page", value = "page", required = true, paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "limit", required = true, paramType = "query"),
        @ApiImplicitParam(name = "param", value = "param", required = false, paramType = "query")
    })
    public String globalParamsListPage(Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGlobalParamsServiceImpl.getFlowGlobalParamsListPage(username, isAdmin, page, limit, param);
    }
    
    /**
     * Query and enter the GlobalParams list
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/globalParamsList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="globalParamsList", notes="global params list")
    @ApiImplicitParam(name = "param", value = "param", required = false, paramType = "query")
    public String globalParamsList(String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGlobalParamsServiceImpl.getFlowGlobalParamsList(username, isAdmin, param);
    }

    /**
     * add GlobalParams
     *
     * @param globalParamsVo
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/addGlobalParams", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="addGlobalParams", notes="add global params")
    public String addGlobalParams(FlowGlobalParamsVoRequestAdd globalParamsVo) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        //Boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGlobalParamsServiceImpl.addFlowGlobalParams(username, globalParamsVo);
    }

    /**
     * update GlobalParams
     *
     * @param globalParamsVo
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/updateGlobalParams", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="updateGlobalParams", notes="update global params")
    public String updateGlobalParams(FlowGlobalParamsVoRequest globalParamsVo) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGlobalParamsServiceImpl.updateFlowGlobalParams(username, isAdmin, globalParamsVo);
    }

    /**
     * get GlobalParams by id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getGlobalParamsById", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="getGlobalParamsById", notes="get global params by id")
    @ApiImplicitParam(name="id", value="id", required = true)
    public String getGlobalParamsById(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGlobalParamsServiceImpl.getFlowGlobalParamsById(username, isAdmin, id);
    }

    /**
     * del GlobalParams
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delGlobalParams", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="delGlobalParams", notes="delete global params by id")
    public String delGlobalParams(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGlobalParamsServiceImpl.deleteFlowGlobalParamsById(username, isAdmin, id);
    }
}
