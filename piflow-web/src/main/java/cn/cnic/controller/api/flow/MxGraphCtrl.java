package cn.cnic.controller.api.flow;

import java.util.HashMap;
import java.util.Map;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.component.system.service.ILogHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.cnic.base.utils.JsonUtils;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.flow.service.IFlowGroupService;
import cn.cnic.component.mxGraph.service.IMxGraphModelService;
import cn.cnic.component.mxGraph.service.IMxNodeImageService;
import cn.cnic.component.mxGraph.vo.MxGraphVo;
import io.swagger.annotations.Api;


/**
 * grapheditorctrl
 */
@Api(value = "mxGraph api")
@Controller
@RequestMapping("/mxGraph")
public class MxGraphCtrl {

    private final IFlowGroupService flowGroupServiceImpl;
    private final IMxGraphModelService mxGraphModelServiceImpl;
    private final IMxNodeImageService mxNodeImageServiceImpl;
    private final ILogHelperService logHelperServiceImpl;

    @Autowired
    public MxGraphCtrl(IFlowGroupService flowGroupServiceImpl, IMxGraphModelService mxGraphModelServiceImpl, IMxNodeImageService mxNodeImageServiceImpl, ILogHelperService logHelperServiceImpl) {
        this.flowGroupServiceImpl = flowGroupServiceImpl;
        this.mxGraphModelServiceImpl = mxGraphModelServiceImpl;
        this.mxNodeImageServiceImpl = mxNodeImageServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
    }

    /**
     * save data
     *
     * @param imageXML
     * @param load
     * @param operType
     * @return
     */
    @RequestMapping(value = "/saveDataForTask", method = RequestMethod.POST)
    @ResponseBody
    public String saveDataForTask(String imageXML, String load, String operType) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("saveDataForTask" + load, username);
        return mxGraphModelServiceImpl.saveDataForTask(username, imageXML, load, operType);
    }

    /**
     * save data
     *
     * @param imageXML
     * @param load
     * @param operType
     * @return
     */
    @RequestMapping(value = "/saveDataForGroup", method = RequestMethod.POST)
    @ResponseBody
    public String saveDataForGroup(String imageXML, String load, String operType) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("saveDataForGroup " + load, username);
        return mxGraphModelServiceImpl.saveDataForGroup(username, imageXML, load, operType, true);
    }

    @RequestMapping(value = "/addMxCellAndData", method = RequestMethod.POST)
    @ResponseBody
    public String addMxCellAndData(@RequestBody MxGraphVo mxGraphVo) throws Exception {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("addMxCellAndData" + mxGraphVo.getLoadId(), currentUsername);
        return mxGraphModelServiceImpl.addMxCellAndData(mxGraphVo, currentUsername);
    }

     @RequestMapping(value = "/uploadNodeImage", method = RequestMethod.POST)
    @ResponseBody
    public String uploadNodeImage(@RequestParam("file") MultipartFile file, String imageType) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("uploadNodeImage " + file.getName(),username);
        return mxNodeImageServiceImpl.uploadNodeImage(username, file, imageType);
    }

    @RequestMapping(value = "/nodeImageList", method = RequestMethod.POST)
    @ResponseBody
    public String nodeImageList(String imageType) {
        String username = SessionUserUtil.getCurrentUsername();
        return mxNodeImageServiceImpl.getMxNodeImageList(username, imageType);
    }

    @RequestMapping(value = "/groupRightRun", method = RequestMethod.POST)
    @ResponseBody
    public String groupRightRun(String pId, String nodeId, String nodeType) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("groupRightRun" + nodeId, username);
        return flowGroupServiceImpl.rightRun(username, isAdmin, pId, nodeId, nodeType);
    }

    @RequestMapping(value = "/eraseRecord", method = RequestMethod.POST)
    @ResponseBody
    public String eraseRecord() {
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("flag", true);
    }

}
