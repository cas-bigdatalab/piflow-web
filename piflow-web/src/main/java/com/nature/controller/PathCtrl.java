package com.nature.controller;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.component.flow.service.IPathsService;
import com.nature.component.flow.vo.PathsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/path/*")
public class PathCtrl {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IPathsService pathsServiceImpl;

    /**
     * Query'path'according to'flowId' and'pageId'
     *
     * @param fid
     * @param id
     * @return
     */
    @RequestMapping("/queryPathInfo")
    public String getStopGroup(String fid, String id) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        if (StringUtils.isNotBlank(fid) && StringUtils.isNotBlank(id)) {
            PathsVo queryInfo = pathsServiceImpl.getPathsByFlowIdAndPageId(fid, id);
            if (null != queryInfo) {
                rtnMap.put("code", 200);
                rtnMap.put("queryInfo", queryInfo);
                rtnMap.put("errorMsg", "Success");
            } else {
                rtnMap.put("errorMsg", "No'paths'information was queried");
                logger.warn("No'paths'information was queried");
            }
        } else {
            rtnMap.put("errorMsg", "The parameter'fid'or'id' is empty");
            logger.warn("The parameter'fid'or'id' is empty");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }


}
