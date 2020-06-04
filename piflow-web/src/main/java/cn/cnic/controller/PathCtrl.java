package cn.cnic.controller;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.flow.request.UpdatePathRequest;
import cn.cnic.component.flow.service.IPathsService;
import cn.cnic.component.flow.service.IPropertyService;
import cn.cnic.component.flow.vo.PathsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/path")
public class PathCtrl {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private IPropertyService propertyServiceImpl;

    @Resource
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

    /**
     * Save user-selected ports
     *
     * @param updatePathRequest
     * @return
     */
    @RequestMapping("/savePathsPort")
    @ResponseBody
    public String savePathsPort(UpdatePathRequest updatePathRequest) {
        String username = SessionUserUtil.getCurrentUsername();
        return propertyServiceImpl.saveOrUpdateRoutePath(username, updatePathRequest);
    }


}
