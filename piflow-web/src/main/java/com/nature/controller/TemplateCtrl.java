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
import com.nature.component.template.service.ITemplateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * template的ctrl
 */
@RestController
@RequestMapping("/template")
public class TemplateCtrl {

    @Autowired
    private IFlowService iFlowServiceImpl;

    @Autowired
    private ITemplateService iTemplateService;

    @Autowired
    private IStopGroupService stopGroupServiceImpl;

    @Autowired
    private IFlowAndStopsTemplateVoService flowAndStopsTemplateVoServiceImpl;

    @Autowired
    private IPathsService pathsServiceImpl;


    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @RequestMapping("/saveTemplate")
    @ResponseBody
    @Transactional
    public String saveData(HttpServletRequest request, Model model) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        String name = request.getParameter("name");
        String loadId = request.getParameter("load");
        String value = request.getParameter("value");
        MxGraphModelVo mxGraphModelVo = null;
        if (StringUtils.isAnyEmpty(name, loadId)) {
            rtnMap.put("errorMsg", "Some incoming parameters are empty");
            logger.info("Some incoming parameters are empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        Flow flowById = iFlowServiceImpl.getFlowById(loadId);
        if (null != flowById) {
            if (StringUtils.isBlank(value)) {
                MxGraphModel mxGraphModel = flowById.getMxGraphModel();
                if (null != mxGraphModel) {
                    mxGraphModelVo = FlowXmlUtils.mxGraphModelPoToVo(mxGraphModel);
                    // Convert the query mxGraphModelVo to XML
                    value = FlowXmlUtils.mxGraphModelToXml(mxGraphModelVo);
                }
            }
            //Concatenate the XML according to the flowById
            String flowAndStopInfoToXml = FlowXmlUtils.flowAndStopInfoToXml(flowById, value);
            logger.info(flowAndStopInfoToXml);

            Template template = new Template();
            template.setId(SqlUtils.getUUID32());
            template.setCrtDttm(new Date());
            template.setCrtUser(username);
            template.setEnableFlag(true);
            template.setLastUpdateUser(username);
            template.setLastUpdateDttm(new Date());
            template.setName(name);
            //Keep one copy in the database
            template.setValue(value);
            template.setFlow(flowById);
            //XML to file and save to the specified directory
            String path = FileUtils.createXml(flowAndStopInfoToXml, name, ".xml", SysParamsCache.XML_PATH);
            template.setPath(path);
            int addTemplate = iTemplateService.addTemplate(template);
            if (addTemplate > 0) {
                rtnMap.put("code", 200);
                rtnMap.put("errorMsg", "save template success");
                //Save the stop, property information
                List<Stops> stopsList = flowById.getStopsList();
                if (null != stopsList && stopsList.size() > 0) {
                    flowAndStopsTemplateVoServiceImpl.addStopsList(stopsList, template);
                }
            } else {
                rtnMap.put("errorMsg", "failed to save template");
                logger.info("Failure to save template");
            }
            return JsonUtils.toJsonNoException(rtnMap);
        } else {
            rtnMap.put("errorMsg", "Flow information is empty");
            logger.info("Flow information is empty,loadId：" + loadId);
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    /**
     * Delete the template based on id
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteTemplate")
    @ResponseBody
    @Transactional
    public int deleteTemplate(String id) {
        int deleteTemplate = 0;
        if (StringUtils.isNoneBlank(id)) {
            Template template = iTemplateService.queryTemplate(id);
            if (null != template) {
                List<StopTemplateModel> stopsList = template.getStopsList();
                if (null != stopsList && stopsList.size() > 0) {
                    for (StopTemplateModel stopTemplateVo : stopsList) {
                        //First remove the stop attribute based on stopid
                        flowAndStopsTemplateVoServiceImpl.deleteStopPropertyTemByStopId(stopTemplateVo.getId());
                    }
                    //First delete stop based on templateId
                    flowAndStopsTemplateVoServiceImpl.deleteStopTemByTemplateId(template.getId());
                }
                //Delete the template
                deleteTemplate = iTemplateService.deleteTemplate(template.getId());
            }
        }
        return deleteTemplate;
    }

    /**
     * Upload xml file and save template
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("templateFile") MultipartFile file) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        if (!file.isEmpty()) {
            String upload = FileUtils.upload(file, SysParamsCache.XML_PATH);
            Map<String, Object> map = JSON.parseObject(upload);
            if (!map.isEmpty() && null != map) {
                Integer code = (Integer) map.get("code");
                if (500 == code) {
                    rtnMap.put("errorMsg", "failed to upload file");
                    JsonUtils.toJsonNoException(rtnMap);
                }
                String name = (String) map.get("fileName");
                String path = (String) map.get("url");
                Template template = new Template();
                template.setId(SqlUtils.getUUID32());
                template.setCrtDttm(new Date());
                template.setCrtUser(username);
                template.setEnableFlag(true);
                template.setLastUpdateUser(username);
                template.setLastUpdateDttm(new Date());
                SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmSSSS");
                Date nowDate = new Date();
                String fileName = sdf.format(nowDate);
                //File name prefix
                String prefix = name.substring(0, name.length() - 4);
                //Suffix .xml
                String Suffix = name.substring(name.length() - 4);
                //Add timestamp
                String uploadfileName = prefix + "-" + fileName;
                template.setName(uploadfileName + Suffix);
                template.setPath(path);
                //Read the xml file according to the saved file path and return the xml string
                String xmlFileToStr = FileUtils.XmlFileToStr(template.getPath());
                if (StringUtils.isBlank(xmlFileToStr)) {
                    logger.info("The xml file failed to read and the template failed to be uploaded.");
                    rtnMap.put("errorMsg", "The xml file failed to read. Please try again.");
                    return JsonUtils.toJsonNoException(rtnMap);
                }
                List<StopTemplateModel> stopsList = null;
                //Xml conversion Template object, including stops and attributes
                Template xmlToFlowStopInfo = FlowXmlUtils.xmlToFlowStopInfo(xmlFileToStr);
                if (null != xmlToFlowStopInfo) {
                    stopsList = xmlToFlowStopInfo.getStopsList();
                }
                //Get the mxGraphModel part from the xml string and save it to value
                MxGraphModelVo xmlToMxGraphModelVo = FlowXmlUtils.allXmlToMxGraphModelVo(xmlFileToStr, 0);
                if (null != xmlToMxGraphModelVo) {
                    // Convert the mxGraphModelVo from the query to XML
                    String loadXml = FlowXmlUtils.mxGraphModelToXml(xmlToMxGraphModelVo);
                    template.setValue(loadXml);
                }

                int addTemplate = iTemplateService.addTemplate(template);
                if (addTemplate > 0) {
                    //Save stop, attribute information
                    if (null != stopsList && stopsList.size() > 0) {
                        List<Stops> stop = FlowXmlUtils.stopTemplateVoToStop(stopsList);
                        if (null != stop && stop.size() > 0) {
                            flowAndStopsTemplateVoServiceImpl.addStopsList(stop, template);
                        }
                    }
                    rtnMap.put("code", 200);
                    rtnMap.put("errorMsg", "successful template upload");
                    logger.info("Template upload succeeded");
                } else {
                    rtnMap.put("errorMsg", "template upload failed");
                    logger.info("template upload failed");
                }
                return JsonUtils.toJsonNoException(rtnMap);
            }
        }
        rtnMap.put("errorMsg", "Upload failed, please try again later");
        return JsonUtils.toJsonNoException(rtnMap);
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
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        String templateId = request.getParameter("templateId");
        String loadId = request.getParameter("load");
        Flow flowById = iFlowServiceImpl.getFlowById(loadId);
        if (null == flowById) {
            logger.info("Template is empty, loading template failed");
            rtnMap.put("errorMsg", "Loading failed, please try again");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        Template template = iTemplateService.queryTemplate(templateId);
        if (null == template) {
            logger.info("Template is empty, loading template failed");
            rtnMap.put("errorMsg", "Loading failed, please try again");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        //Read the xml file according to the saved file path and return
        String xmlFileToStr = FileUtils.XmlFileToStr(template.getPath());
        if (StringUtils.isBlank(xmlFileToStr)) {
            logger.info("The xml file failed to read and the template failed to be loaded.");
            rtnMap.put("errorMsg", "The xml file failed to read. Please try again.");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Get the maximum pageId in stop
        String maxStopPageId = iFlowServiceImpl.getMaxStopPageId(loadId);
        maxStopPageId = StringUtils.isNotBlank(maxStopPageId) ? maxStopPageId : "0";
        int maxPageId = Integer.parseInt(maxStopPageId);
        StatefulRtnBase addFlow = null;
        MxGraphModelVo xmlToMxGraphModel = FlowXmlUtils.allXmlToMxGraphModelVo(xmlFileToStr, maxPageId);
        if (null != xmlToMxGraphModel) {
            xmlToMxGraphModel.getRootVo();
            addFlow = iFlowServiceImpl.saveOrUpdateFlowAll(xmlToMxGraphModel, loadId, "ADD", false);
        }
        List<Paths> pathsList = FlowXmlUtils.xmlToPaths(xmlFileToStr);
        if (null != pathsList && pathsList.size() > 0) {
            pathsServiceImpl.addPathsList(pathsList, flowById);
        }
        // addFlow is not empty and the value of ReqRtnStatus is true, then the save is successful.
        if (null != addFlow && addFlow.isReqRtnStatus()) {
            if (null != template) {
                //Save stops and attribute information
                flowAndStopsTemplateVoServiceImpl.addTemplateStopsToFlow(template, flowById, maxPageId);
            }
            logger.info("Successfully loaded template");
            return "grapheditor/index";
        } else {
            logger.warn("Failed to load template");
            return "errorPage";
        }
    }

    /**
     * Query all templates for drop-down displays
     *
     * @return
     */
    @RequestMapping("/templateAllSelect")
    @ResponseBody
    public String template() {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        List<Template> findTemPlateList = iTemplateService.findTemPlateList();
        if (null != findTemPlateList && findTemPlateList.size() > 0) {
            rtnMap.put("code", 200);
            rtnMap.put("temPlateList", findTemPlateList);
        } else {
            rtnMap.put("errorMsg", "The query result is empty");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Download template
     *
     * @param response
     * @param templateId
     * @throws Exception
     */
    @RequestMapping("/templateDownload")
    public void downloadLocal(HttpServletResponse response, String templateId) throws Exception {
        Template template = iTemplateService.queryTemplate(templateId);
        if (null == template) {
            logger.info("Template is empty,Download template failed");
        }
        // Download local files
        String fileName = template.getName() + ".xml".toString(); // The default save name of the file
        // Read to the stream
        InputStream inStream = new FileInputStream(template.getPath());// File storage path
        // Format the output
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // Loop out the data in the stream
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/templatePage")
    @ResponseBody
    public String templatePage(HttpServletRequest request, Integer start, Integer length, Integer draw, String extra_search) {
        return iTemplateService.getTemplateListPage(start / length + 1, length, extra_search);
    }
}