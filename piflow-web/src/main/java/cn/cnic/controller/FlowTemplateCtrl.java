package cn.cnic.controller;

import cn.cnic.base.util.ReturnMapUtils;
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
@RequestMapping("/flowTemplate")
public class FlowTemplateCtrl {

    @Resource
    private IFlowTemplateService flowTemplateServiceImpl;


    @RequestMapping("/saveFlowTemplate")
    @ResponseBody
    public String saveFlowTemplate(HttpServletRequest request, Model model) {

        String name = request.getParameter("name");
        String loadId = request.getParameter("load");
        String templateType = request.getParameter("templateType");
        return flowTemplateServiceImpl.addFlowTemplate(name, loadId, templateType);
    }

    @RequestMapping("/flowTemplatePage")
    @ResponseBody
    public String templatePage(Integer page, Integer limit, String param) {
        return flowTemplateServiceImpl.getFlowTemplateListPage(page, limit, param);
    }

    /**
     * Delete the template based on id
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteFlowTemplate")
    @ResponseBody
    public int deleteFlowTemplate(String id) {
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
    public String uploadXmlFile(@RequestParam("file") MultipartFile file) {
        return flowTemplateServiceImpl.uploadXmlFile(file);
    }

    /**
     * Query all templates for drop-down displays
     *
     * @return
     */
    @RequestMapping("/flowTemplateList")
    @ResponseBody
    public String flowTemplateList() {
        return flowTemplateServiceImpl.flowTemplateList();
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
        if ("TASK".equals(loadType)) {
            return flowTemplateServiceImpl.loadTaskTemplate(templateId, loadId);
        } else if ("GROUP".equals(loadType)) {
            return flowTemplateServiceImpl.loadGroupTemplate(templateId, loadId);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("loadType is null");
        }
    }
}