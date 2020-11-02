package cn.cnic.controller;

import cn.cnic.component.flow.service.IFlowGroupPathsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/flowGroupPath/")
public class FlowGroupPathCtrl {

    @Resource
    private IFlowGroupPathsService flowGroupPathsServiceImpl;

    /**
     * Query'path'according to'flowId' and'pageId'
     *
     * @param fid
     * @param id
     * @return
     */
    @RequestMapping("/queryPathInfoFlowGroup")
    public String queryPathInfoFlowGroup(String fid, String id) {
        return flowGroupPathsServiceImpl.queryPathInfoFlowGroup(fid, id);
    }


}
