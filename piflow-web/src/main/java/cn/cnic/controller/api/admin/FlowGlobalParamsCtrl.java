package cn.cnic.controller.api.admin;

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

@Api(value = "FlowGlobalParams api")
@RestController
@RequestMapping("/flowGlobalParams")
public class FlowGlobalParamsCtrl {

    @Autowired
    private IFlowGlobalParamsService flowGlobalParamsServiceImpl;


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
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping(value = "/globalParamsList", method = RequestMethod.GET)
    @ResponseBody
    @ApiImplicitParam(name = "param", value = "param", required = false, paramType = "query")
    public String globalParamsList(String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGlobalParamsServiceImpl.getFlowGlobalParamsList(username, isAdmin, param);
    }

    /**
     * add GlobalParams
     *
     * @param id
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/addGlobalParams", method = RequestMethod.POST)
    @ResponseBody
    public String addGlobalParams(FlowGlobalParamsVoRequestAdd globalParamsVo) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        //Boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGlobalParamsServiceImpl.addFlowGlobalParams(username, globalParamsVo);
    }

    /**
     * update GlobalParams
     *
     * @param id
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/updateGlobalParams", method = RequestMethod.POST)
    @ResponseBody
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
    public String delGlobalParams(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGlobalParamsServiceImpl.deleteFlowGlobalParamsById(username, isAdmin, id);
    }
}
