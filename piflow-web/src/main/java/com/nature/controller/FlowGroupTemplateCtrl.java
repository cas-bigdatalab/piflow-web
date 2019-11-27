package com.nature.controller;

import com.alibaba.fastjson.JSON;
import com.nature.base.util.*;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.base.vo.UserVo;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Stops;
import com.nature.component.flow.service.IFlowService;
import com.nature.component.flow.service.IPathsService;
import com.nature.component.group.service.IStopGroupService;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.template.model.StopTemplateModel;
import com.nature.component.template.model.Template;
import com.nature.component.template.service.IFlowAndStopsTemplateVoService;
import com.nature.component.template.service.IFlowGroupTemplateService;
import com.nature.component.template.service.ITemplateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FlowGroupTemplateCtrl
 */
@RestController
@RequestMapping("/flowGroupTemplate")
public class FlowGroupTemplateCtrl {

    @Autowired
    private IFlowGroupTemplateService flowGroupTemplateServiceImpl;

    @Autowired
    private ITemplateService iTemplateService;


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