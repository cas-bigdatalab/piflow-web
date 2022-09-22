package cn.cnic.controller.api.flow;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.flow.request.UpdatePathRequest;
import cn.cnic.component.flow.service.IPathsService;
import cn.cnic.component.flow.service.IPropertyService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "path api", tags = "path api")
@RestController
@RequestMapping("/path")
public class PathCtrl {

    private final IPropertyService propertyServiceImpl;
    private final IPathsService pathsServiceImpl;

    @Autowired
    public PathCtrl(IPropertyService propertyServiceImpl, IPathsService pathsServiceImpl) {
        this.propertyServiceImpl = propertyServiceImpl;
        this.pathsServiceImpl = pathsServiceImpl;
    }

    /**
     * Query'path'according to'flowId' and'pageId'
     *
     * @param fid
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryPathInfo", method = RequestMethod.POST)
    @ApiOperation(value="queryPathInfo", notes="query Path info")
    public String getStopGroup(String fid, String id) {
        return pathsServiceImpl.getPathsByFlowIdAndPageId(fid, id);
    }

    /**
     * Save user-selected ports
     *
     * @param updatePathRequest
     * @return
     */
    @RequestMapping(value = "/savePathsPort", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="savePathsPort", notes="save Paths port")
    public String savePathsPort(UpdatePathRequest updatePathRequest) {
        String username = SessionUserUtil.getCurrentUsername();
        return propertyServiceImpl.saveOrUpdateRoutePath(username, updatePathRequest);
    }


}
