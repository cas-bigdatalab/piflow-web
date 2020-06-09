package cn.cnic.controller.modify.admin;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.flow.request.UpdatePathRequest;
import cn.cnic.component.flow.service.IPathsService;
import cn.cnic.component.flow.service.IPropertyService;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/path")
public class AdminPathCtrl {

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
    @ResponseBody
    public String getStopGroup(String fid, String id) {
        return pathsServiceImpl.getPathsByFlowIdAndPageId(fid, id);
    }

    /**
     * Save user-selected ports
     *
     * @param updatePathRequest
     * @return
     */
    @RequestMapping("/savePathsPort")
    @ResponseBody
    public String savePathsPort(HttpServletRequest request, UpdatePathRequest updatePathRequest) {
        String username = SessionUserUtil.getUsername(request);
        return propertyServiceImpl.saveOrUpdateRoutePath(username, updatePathRequest);
    }


}
