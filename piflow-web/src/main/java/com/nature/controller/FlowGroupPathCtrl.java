package com.nature.controller;

import com.nature.base.util.LoggerUtil;
import com.nature.component.flow.service.IFlowGroupPathsService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/flowGroupPath/")
public class FlowGroupPathCtrl {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
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
