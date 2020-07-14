package cn.cnic.controller.modify.user;

import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.controller.modify.utils.UserUtils;
import cn.cnic.component.template.service.IFlowTemplateService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * template的ctrl
 */
@RestController
@RequestMapping("/user/flowTemplate")
public class UserFlowTemplateCtrl {

    @Resource
    private IFlowTemplateService flowTemplateServiceImpl;


    @RequestMapping("/saveFlowTemplate")
    @ResponseBody
    public String saveFlowTemplate(HttpServletRequest request, Model model) {
        String name = request.getParameter("name");
        String loadId = request.getParameter("load");
        String templateType = request.getParameter("templateType");
        String username = UserUtils.getUsername(request);
        return flowTemplateServiceImpl.addFlowTemplate(username, name, loadId, templateType);
    }

    @RequestMapping("/flowTemplatePage")
    @ResponseBody
    public String templatePage(HttpServletRequest request, Integer page, Integer limit, String param) {
        String username = UserUtils.getUsername(request);
        return flowTemplateServiceImpl.getFlowTemplateListPage(username, false, page, limit, param);
    }

    /**
     * Delete the template based on id
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteFlowTemplate")
    @ResponseBody
    public String deleteFlowTemplate(String id) {
        return flowTemplateServiceImpl.deleteFlowTemplate(id);
    }

    /**
     * Download template
     *
     * @param response
     * @param flowTemplateId
     * @throws Exception
     */
    @RequestMapping("/templateDownload")
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
    public String uploadXmlFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String username = UserUtils.getUsername(request);
        return flowTemplateServiceImpl.uploadXmlFile(username, file);
    }

    /**
     * Query all templates for drop-down displays
     *
     * @return
     */
    @RequestMapping("/flowTemplateList")
    @ResponseBody
    public String flowTemplateList(HttpServletRequest request) {
        String username = UserUtils.getUsername(request);
        return flowTemplateServiceImpl.flowTemplateList(username, false);
    }

    /**
     * load template
     *
     * @param request
     * @return
     */
    @RequestMapping("/loadingXmlPage")
    @ResponseBody
    @Transactional
    public String loadingXml(HttpServletRequest request) {
        String templateId = request.getParameter("templateId");
        String loadId = request.getParameter("load");
        String loadType = request.getParameter("loadType");
        String username = UserUtils.getUsername(request);
        if ("TASK".equals(loadType)) {
            return flowTemplateServiceImpl.loadTaskTemplate(username, templateId, loadId);
        } else if ("GROUP".equals(loadType)) {
            return flowTemplateServiceImpl.loadGroupTemplate(username, templateId, loadId);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("loadType is null");
        }
    }
}