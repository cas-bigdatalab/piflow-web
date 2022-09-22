package cn.cnic.controller.api.flow;

import cn.cnic.component.flow.service.IFlowGroupPathsService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "flowGroupPath api", tags = "flowGroupPath api")
@RestController
@RequestMapping("/flowGroupPath/")
public class FlowGroupPathCtrl {

    private final IFlowGroupPathsService flowGroupPathsServiceImpl;

    @Autowired
    public FlowGroupPathCtrl(IFlowGroupPathsService flowGroupPathsServiceImpl) {
        this.flowGroupPathsServiceImpl = flowGroupPathsServiceImpl;
    }

    /**
     * Query'path'according to'flowId' and'pageId'
     *
     * @param fid
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryPathInfoFlowGroup", method = RequestMethod.POST)
    @ApiOperation(value="queryPathInfoFlowGroup", notes="query Path info FlowGroup")
    public String queryPathInfoFlowGroup(String fid, String id) {
        return flowGroupPathsServiceImpl.queryPathInfoFlowGroup(fid, id);
    }


}
