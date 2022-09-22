package cn.cnic.controller.api.flow;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.system.service.ILogHelperService;
import cn.cnic.component.template.service.IFlowTemplateService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * template的ctrl
 */
@Api(value = "flowTemplate api", tags = "flowTemplate api")
@RestController
@RequestMapping("/flowTemplate")
public class FlowTemplateCtrl {

    private final IFlowTemplateService flowTemplateServiceImpl;
    private final ILogHelperService logHelperServiceImpl;

    @Autowired
    public FlowTemplateCtrl(IFlowTemplateService flowTemplateServiceImpl, ILogHelperService logHelperServiceImpl) {
        this.flowTemplateServiceImpl = flowTemplateServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
    }


    @RequestMapping(value = "/saveFlowTemplate", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="saveFlowTemplate", notes="save FlowTemplate")
    public String saveFlowTemplate(String name, String load, String templateType) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("saveFlowTemplate",username);
        return flowTemplateServiceImpl.addFlowTemplate(username, name, load, templateType);
    }

    @RequestMapping(value = "/flowTemplatePage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="flowTemplatePage", notes="get FlowTemplate list page")
    public String templatePage(Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowTemplateServiceImpl.getFlowTemplateListPage(username, isAdmin, page, limit, param);
    }

    /**
     * Delete the template based on id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteFlowTemplate", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="deleteFlowTemplate", notes="delete FlowTemplate")
    public String deleteFlowTemplate(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("deleteFlowTemplate"+ id,username);
        return flowTemplateServiceImpl.deleteFlowTemplate(id);
    }

    /**
     * Download template
     *
     * @param response
     * @param flowTemplateId
     * @throws Exception
     */
    @RequestMapping(value = "/templateDownload", method = RequestMethod.GET)
    @ApiOperation(value="templateDownload", notes="download FlowTemplate")
    public void templateDownload(HttpServletResponse response, String flowTemplateId) throws Exception {
        flowTemplateServiceImpl.templateDownload(response, flowTemplateId);
    }

    /**
     * Upload xml file and save flowTemplate
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadXmlFile", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="uploadXmlFile", notes="upload FlowTemplate")
    public String uploadXmlFile(@RequestParam("file") MultipartFile file) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("uploadXmlFile" + file.getName() , username);
        return flowTemplateServiceImpl.uploadXmlFile(username, file);
    }

    /**
     * Query all templates for drop-down displays
     *
     * @return
     */
    @RequestMapping(value = "/flowTemplateList", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="flowTemplateList", notes="flowTemplate List")
    public String flowTemplateList() {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowTemplateServiceImpl.flowTemplateList(username, isAdmin);
    }

    /**
     * load template
     *
     * @param templateId
     * @param load
     * @param loadType
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/loadingXmlPage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="loadingXmlPage", notes="loading xml Page")
    public String loadingXml(String templateId, String load, String loadType) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        if ("TASK".equals(loadType)) {
            return flowTemplateServiceImpl.loadTaskTemplate(username, templateId, load);
        } else if ("GROUP".equals(loadType)) {
            return flowTemplateServiceImpl.loadGroupTemplate(username, templateId, load);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.LOAD_TYPE_ERROR_MSG());
        }
    }
}