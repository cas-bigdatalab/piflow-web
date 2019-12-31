package com.nature.controller;

import com.nature.component.template.service.IFlowGroupTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FlowGroupTemplateCtrl
 */
@RestController
@RequestMapping("/flowGroupTemplate")
public class FlowGroupTemplateCtrl {

    @Autowired
    private IFlowGroupTemplateService flowGroupTemplateServiceImpl;


    @RequestMapping("/saveFlowGroupTemplate")
    @ResponseBody
    public String saveFlowGroupTemplate(HttpServletRequest request, Model model) {

        String name = request.getParameter("name");
        String loadId = request.getParameter("load");
        String value = request.getParameter("value");
        return flowGroupTemplateServiceImpl.addFlowGroupTemplate(name, loadId, value);
    }

    @RequestMapping("/flowGroupTemplatePage")
    @ResponseBody
    public String templatePage(HttpServletRequest request, Integer start, Integer length, Integer draw, String extra_search) {
        return flowGroupTemplateServiceImpl.getFlowGroupTemplateListPage(start / length + 1, length, extra_search);
    }

    /**
     * Delete the template based on id
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteFlowGroupTemplate")
    @ResponseBody
    public int deleteFlowGroupTemplate(String id) {
        return flowGroupTemplateServiceImpl.deleteFlowGroupTemplate(id);
    }

    /**
     * Download template
     *
     * @param response
     * @param flowGroupTemplateId
     * @throws Exception
     */
    @RequestMapping("/templateDownload")
    public void templateDownload(HttpServletResponse response, String flowGroupTemplateId) throws Exception {
        flowGroupTemplateServiceImpl.templateDownload(response, flowGroupTemplateId);
    }

    /**
     * Upload xml file and save flowGroupTemplate
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadXmlFile", method = RequestMethod.POST)
    @ResponseBody
    public String uploadXmlFile(@RequestParam("flowGroupTemplateFile") MultipartFile file) {
        return flowGroupTemplateServiceImpl.uploadXmlFile(file);
    }

    /**
     * Query all templates for drop-down displays
     *
     * @return
     */
    @RequestMapping("/flowGroupTemplateAllSelect")
    @ResponseBody
    public String flowGroupTemplateAllSelect() {
        return flowGroupTemplateServiceImpl.flowGroupTemplateAllSelect();
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
        return flowGroupTemplateServiceImpl.loadFlowGroupTemplate(templateId, loadId);
    }
}