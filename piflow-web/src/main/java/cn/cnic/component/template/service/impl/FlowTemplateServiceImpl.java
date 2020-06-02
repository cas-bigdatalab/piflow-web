package cn.cnic.component.template.service.impl;

import cn.cnic.base.util.*;
import cn.cnic.common.Eunm.TemplateType;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.flow.model.*;
import cn.cnic.component.mxGraph.model.MxCell;
import cn.cnic.component.mxGraph.model.MxGraphModel;
import cn.cnic.component.mxGraph.utils.MxCellUtils;
import cn.cnic.component.mxGraph.utils.MxGraphModelUtils;
import cn.cnic.component.template.model.FlowTemplate;
import cn.cnic.component.template.service.IFlowTemplateService;
import cn.cnic.component.template.utils.FlowTemplateUtils;
import cn.cnic.component.template.vo.FlowTemplateVo;
import cn.cnic.domain.flow.FlowDomain;
import cn.cnic.domain.flow.FlowGroupDomain;
import cn.cnic.domain.flow.StopsDomain;
import cn.cnic.domain.mxGraph.MxCellDomain;
import cn.cnic.domain.template.FlowTemplateDomain;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Transactional
public class FlowTemplateServiceImpl implements IFlowTemplateService {
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private FlowTemplateDomain flowTemplateDomain;

    @Resource
    private FlowGroupDomain flowGroupDomain;

    @Resource
    private FlowDomain flowDomain;

    @Resource
    private StopsDomain stopsDomain;

    @Resource
    private MxCellDomain mxCellDomain;


    /**
     * add FlowTemplate
     *
     * @param name
     * @param loadId
     * @param templateType
     * @return
     */
    @Override
    @Transactional
    public String addFlowTemplate(String name, String loadId, String templateType) {
        String username = SessionUserUtil.getCurrentUsername();
        if (StringUtils.isBlank(name)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        if (StringUtils.isBlank(loadId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'loadId' is empty");
        }
        String templateXmlStr = "";
        TemplateType saveTemplateType = null;
        switch (templateType) {
            case "TASK": {
                Flow flowById = flowDomain.getFlowById(loadId);
                if (null == flowById) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("Flow information is empty,loadId：" + loadId);
                }
                saveTemplateType = TemplateType.TASK;
                String mxGraphXml_Flow = "";
                MxGraphModel mxGraphModel = flowById.getMxGraphModel();
                if (null != mxGraphModel) {
                    // Convert the query mxGraphModel to XML
                    mxGraphXml_Flow = FlowXmlUtils.mxGraphModelToXml(mxGraphModel);
                }
                //Splicing XML according to flowById
                templateXmlStr = FlowXmlUtils.flowAndStopInfoToXml(flowById, mxGraphXml_Flow);
                break;
            }
            case "GROUP": {
                FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(loadId);
                if (null == flowGroupById) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("Group information is empty,loadId：" + loadId);
                }
                saveTemplateType = TemplateType.GROUP;
                //Splicing XML according to flowGroupById
                templateXmlStr = FlowXmlUtils.flowGroupToXmlStr(flowGroupById);
                break;
            }
            default:
                return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'templateType' is error");
        }

        logger.info(templateXmlStr);
        String saveFileName = UUIDUtils.getUUID32();
        String path = FileUtils.createXml(templateXmlStr, saveFileName, SysParamsCache.XML_PATH);

        FlowTemplate flowTemplate = FlowTemplateUtils.newFlowTemplateNoId(username);
        //flowTemplate.setId(SqlUtils.getUUID32());
        flowTemplate.setName(name);
        //XML to file and save to specified directory
        flowTemplate.setPath(path);
        flowTemplate.setTemplateType(saveTemplateType);
        flowTemplate.setUrl("/xml/" + saveFileName + ".xml");
        flowTemplate.setDescription(name);
        flowTemplateDomain.saveOrUpdate(flowTemplate);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("save template success");
    }

    @Override
    public String getFlowTemplateListPage(Integer offset, Integer limit, String param) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        if (null != offset && null != limit) {
            Page<FlowTemplate> flowTemplateListPage = flowTemplateDomain.getFlowTemplateListPage(offset - 1, limit, param);
            rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
            rtnMap.put("msg", "");
            rtnMap.put("count", flowTemplateListPage.getTotalElements());
            rtnMap.put("data", flowTemplateListPage.getContent());//Data collection
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Delete the template based on id
     *
     * @param id
     * @return
     */
    @Override
    public int deleteFlowTemplate(String id) {
        int deleteTemplate = 0;
        if (StringUtils.isNoneBlank(id)) {
            deleteTemplate = flowTemplateDomain.updateEnableFlagById(id, false);
        }
        return deleteTemplate;
    }

    /**
     * Download template
     *
     * @param flowTemplateId
     */
    @Override
    public void templateDownload(HttpServletResponse response, String flowTemplateId) {
        FlowTemplate flowTemplate = flowTemplateDomain.getFlowTemplateById(flowTemplateId);
        if (null == flowTemplate) {
            logger.info("Template is empty,Download template failed");
        } else {
            String fileName = flowTemplate.getName() + ".xml".toString(); // The default save name of the file
            String filePath = flowTemplate.getPath();// File storage path
            FileUtils.downloadFileResponse(response, fileName, filePath);
        }

    }

    /**
     * Upload xml file and save flowTemplate
     *
     * @param file
     * @return
     */
    @Override
    public String uploadXmlFile(MultipartFile file) {
        String username = SessionUserUtil.getCurrentUsername();
        if (file.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
        }
        Map<String, Object> uploadMap = FileUtils.uploadRtnMap(file, SysParamsCache.XML_PATH);
        if (null == uploadMap || uploadMap.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
        }
        Integer code = (Integer) uploadMap.get("code");
        if (500 == code) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed to upload file");
        }
        String saveFileName = (String) uploadMap.get("saveFileName");
        String fileName = (String) uploadMap.get("fileName");
        String path = (String) uploadMap.get("path");
        //Read the XML file according to the saved file path and return the XML string
        String xmlFileToStr = FileUtils.XmlFileToStrByAbsolutePath(path);
        if (StringUtils.isBlank(xmlFileToStr)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("XML file read failed, upload template failed");
        }
        TemplateType templateType = MxGraphUtils.determineTemplateType(xmlFileToStr);
        if (null == templateType) {
            FileUtils.deleteFile(path);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("There is a problem with the template, please check and try again");
        }
        FlowTemplate flowTemplate = FlowTemplateUtils.newFlowTemplateNoId(username);
        flowTemplate.setId(SqlUtils.getUUID32());
        flowTemplate.setName(fileName);
        flowTemplate.setPath(path);
        flowTemplate.setUrl("/xml/" + saveFileName);
        flowTemplate.setTemplateType(templateType);
        flowTemplate.setDescription(fileName);
        flowTemplateDomain.saveOrUpdate(flowTemplate);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("successful template upload");
    }

    @Override
    public String flowTemplateList() {
        List<FlowTemplate> findTemPlateList = flowTemplateDomain.getFlowTemplateList();
        if (null == findTemPlateList || findTemPlateList.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Query result is empty");
        }
        List<FlowTemplateVo> flowTemplateVoList = new ArrayList<>();
        for (FlowTemplate flowTemplate : findTemPlateList) {
            if (null != flowTemplate) {
                FlowTemplateVo flowTemplateVo = new FlowTemplateVo();
                BeanUtils.copyProperties(flowTemplate, flowTemplateVo);
                flowTemplateVoList.add(flowTemplateVo);
            }
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("temPlateList", flowTemplateVoList);
    }

    @Override
    public String loadGroupTemplate(String templateId, String loadId) {
        String username = SessionUserUtil.getCurrentUsername();
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal user, Load failed");
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(loadId);
        if (null == flowGroupById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Load failed, please try again");
        }
        FlowTemplate flowTemplate = flowTemplateDomain.getFlowTemplateById(templateId);
        if (null == flowTemplate) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Load failed, please try again");
        }
        //The XML file is read and returned according to the saved file path
        String xmlFileToStr = FileUtils.XmlFileToStrByAbsolutePath(flowTemplate.getPath());
        if (StringUtils.isBlank(xmlFileToStr)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("XML file read failed, loading template failed");
        }
        // Get the maximum value of pageId in drawing board node
        MxGraphModel mxGraphModelDb = flowGroupById.getMxGraphModel();
        Integer maxPageId = 2;
        if (null != mxGraphModelDb && StringUtils.isNotBlank(mxGraphModelDb.getId())) {
            maxPageId = mxCellDomain.getMaxPageIdByMxGraphModelId(mxGraphModelDb.getId());
        }
        maxPageId = (null != maxPageId ? maxPageId : 1);
        // Get the current flowGroup containing all flow names
        String[] flowNamesByFlowGroupId = flowDomain.getFlowAndGroupNamesByFlowGroupId(loadId);

        if (TemplateType.TASK == flowTemplate.getTemplateType()) {
            Flow flowXml = FlowXmlUtils.xmlToFlow(xmlFileToStr, maxPageId, username, false);
            if (null == flowXml) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("Conversion failure");
            }
            flowXml.setFlowGroup(flowGroupById);
            List<Flow> flowList = flowGroupById.getFlowList();
            flowList.add(flowXml);
            flowGroupById.setFlowList(flowList);

            if (null == mxGraphModelDb) {
                mxGraphModelDb = MxGraphModelUtils.mxGraphModelNewNoId(username);
                mxGraphModelDb.setFlowGroup(flowGroupById);
            }
            List<MxCell> rootDb = mxGraphModelDb.getRoot();
            if (null == rootDb || rootDb.size() <= 1) {
                List<MxCell> mxCellList = MxCellUtils.initMxCell(username, mxGraphModelDb);
                rootDb.addAll(mxCellList);
            }
            if (Arrays.asList(flowNamesByFlowGroupId).contains(flowXml.getName())) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("Duplicate FlowName");
            }

            MxCell mxCell = MxCellUtils.AddDefaultFlowMxCell(username, flowXml.getPageId(), flowXml.getName());
            mxCell.setMxGraphModel(mxGraphModelDb);
            rootDb.add(mxCell);
            mxGraphModelDb.setRoot(rootDb);
            flowGroupById.setMxGraphModel(mxGraphModelDb);

        } else if (TemplateType.GROUP == flowTemplate.getTemplateType()) {

            Map<String, Object> XmlStrToFlowGroupRtnMap = FlowXmlUtils.XmlStrToFlowGroup(xmlFileToStr, maxPageId, username, flowNamesByFlowGroupId, false);
            if (200 != (Integer) XmlStrToFlowGroupRtnMap.get("code")) {
                return JsonUtils.toJsonNoException(XmlStrToFlowGroupRtnMap);
            }
            FlowGroup flowGroupXml = (FlowGroup) XmlStrToFlowGroupRtnMap.get("flowGroup");
            if (null == flowGroupXml) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("Conversion failure");
            }
            // Added processing drawing board data

            // Fetch the drawing board data to be added
            MxGraphModel mxGraphModelXml = flowGroupXml.getMxGraphModel();
            if (null != mxGraphModelXml) {
                MxGraphModel mxGraphModel = flowGroupById.getMxGraphModel();
                if (null == mxGraphModel) {
                    mxGraphModel = MxGraphModelUtils.mxGraphModelNewNoId(username);
                    mxGraphModel.setFlowGroup(flowGroupById);
                    mxGraphModel.setId(SqlUtils.getUUID32());
                    //mxGraphModel = mxGraphModelDomain.saveOrUpdate(mxGraphModel);
                }
                List<MxCell> rootDb = mxGraphModel.getRoot();
                if (null == rootDb || rootDb.size() <= 1) {
                    List<MxCell> mxCellList = MxCellUtils.initMxCell(username, mxGraphModel);
                    rootDb.addAll(mxCellList);
                }
                List<MxCell> rootXml = mxGraphModelXml.getRoot();
                if (null != rootXml && rootXml.size() > 0) {
                    for (MxCell mxCell : rootXml) {
                        if (null == mxCell) {
                            continue;
                        }
                        //Associated sketchpad
                        mxCell.setMxGraphModel(mxGraphModel);

                    }
                    rootDb.addAll(rootXml);
                }
                mxGraphModel.setRoot(rootDb);
            }

            // Added processing flow data
            List<Flow> flowListXml = flowGroupXml.getFlowList();
            if (null != flowListXml && flowListXml.size() > 0) {
                List<Flow> flowList = flowGroupById.getFlowList();
                for (Flow flowXml : flowListXml) {
                    if (null == flowXml) {
                        continue;
                    }
                    // link
                    flowXml.setFlowGroup(flowGroupById);
                }
                flowList.addAll(flowListXml);
                flowGroupById.setFlowList(flowList);
            }

            // Added processing of flowGroupPath data
            List<FlowGroupPaths> flowGroupPathsListXml = flowGroupXml.getFlowGroupPathsList();
            if (null != flowGroupPathsListXml && flowGroupPathsListXml.size() > 0) {
                List<FlowGroupPaths> flowGroupPathsList = flowGroupById.getFlowGroupPathsList();
                for (FlowGroupPaths flowGroupPathsXml : flowGroupPathsListXml) {
                    flowGroupPathsXml.setFlowGroup(flowGroupById);
                }
                flowGroupPathsList.addAll(flowGroupPathsListXml);
                flowGroupById.setFlowGroupPathsList(flowGroupPathsList);
            }

            // Added processing of flowGroupPath data
            List<FlowGroup> flowGroupListXml = flowGroupXml.getFlowGroupList();
            if (null != flowGroupListXml && flowGroupListXml.size() > 0) {
                List<FlowGroup> flowGroupList = flowGroupById.getFlowGroupList();
                for (FlowGroup flowGroupListXml_i : flowGroupListXml) {
                    flowGroupListXml_i.setFlowGroup(flowGroupById);
                }
                flowGroupList.addAll(flowGroupListXml);
                flowGroupById.setFlowGroupList(flowGroupList);
            }

        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Template is wrong, please check the template, loading template failed");
        }

        // update
        flowGroupDomain.saveOrUpdate(flowGroupById);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("success");
    }

    @Override
    public String loadTaskTemplate(String templateId, String flowId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        if (StringUtils.isBlank(currentUsername)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal user, Load failed");
        }
        if (null == flowId) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("FlowId is empty, loading FlowTemplate failed");
        }
        if (null == templateId) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("FlowTemplateId is empty, loading FlowTemplate failed");
        }
        Flow flowById = flowDomain.getFlowById(flowId);
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Flow is empty, loading FlowTemplate failed");
        }
        FlowTemplate flowTemplate = flowTemplateDomain.getFlowTemplateById(templateId);
        if (null == flowTemplate) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("FlowTemplate is empty, loading FlowTemplate failed");
        }
        if (TemplateType.TASK != flowTemplate.getTemplateType()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Template types do not match, loading FlowTemplate failed");
        }
        //Read the xml file according to the saved file path and return
        String xmlFileToStr = FileUtils.XmlFileToStrByAbsolutePath(flowTemplate.getPath());
        if (StringUtils.isBlank(xmlFileToStr)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The xml file failed to read and the FlowTemplate failed to be loaded.");
        }
        // Get the maximum pageId in stop
        Integer maxStopPageId = stopsDomain.getMaxStopPageIdByFlowId(flowId);
        maxStopPageId = null == maxStopPageId ? 2 : maxStopPageId;
        // Get the current flow containing all stop names
        String[] stopNamesByFlowId = stopsDomain.getStopNamesByFlowId(flowId);
        Map<String, Object> flowTemplateXmlToFlowRtnMap = FlowXmlUtils.flowTemplateXmlToFlow(xmlFileToStr, currentUsername, maxStopPageId + "", null, stopNamesByFlowId);
        if (200 != (Integer) flowTemplateXmlToFlowRtnMap.get("code")) {
            return JsonUtils.toJsonNoException(flowTemplateXmlToFlowRtnMap);
        }
        Flow flowTemplateXmlToFlow = (Flow) flowTemplateXmlToFlowRtnMap.get("flow");
        if (null == flowTemplateXmlToFlow) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Conversion failure");
        }
        // Added processing drawing board data
        // Fetch the drawing board data to be added
        MxGraphModel mxGraphModelXml = flowTemplateXmlToFlow.getMxGraphModel();
        if (null != mxGraphModelXml) {
            MxGraphModel mxGraphModel = flowById.getMxGraphModel();
            if (null == mxGraphModel) {
                mxGraphModel = MxGraphModelUtils.setMxGraphModelBasicInformation(null, false, currentUsername);
            } else {
                // Update basic information
                mxGraphModel = MxGraphModelUtils.updateMxGraphModelBasicInformation(mxGraphModel, currentUsername);
            }
            // link flow
            mxGraphModel.setFlow(flowById);

            List<MxCell> mxCellList = null;
            if (null == mxGraphModel.getRoot() || mxGraphModel.getRoot().size() <= 1) {
                mxCellList = MxCellUtils.initMxCell(currentUsername, mxGraphModel);
            }
            if (null == mxCellList) {
                mxCellList = new ArrayList<>();
            }
            List<MxCell> rootXml = mxGraphModelXml.getRoot();
            if (null != rootXml && rootXml.size() > 0) {
                for (MxCell mxCell : rootXml) {
                    mxCell.setMxGraphModel(mxGraphModel);
                    mxCellList.add(mxCell);
                }
            }
            mxGraphModel.setRoot(mxCellList);
            flowById.setMxGraphModel(mxGraphModel);
        }
        // Added processing flow data
        List<Stops> stopsListXml = flowTemplateXmlToFlow.getStopsList();
        if (null != stopsListXml && stopsListXml.size() > 0) {
            List<Stops> stopsList = flowById.getStopsList();
            if (null == stopsList) {
                stopsList = new ArrayList<>();
            }
            for (Stops stops : stopsListXml) {
                stops.setFlow(flowById);
                stopsList.add(stops);
            }
            flowById.setStopsList(stopsList);
        }
        List<Paths> pathsListXml = flowTemplateXmlToFlow.getPathsList();
        if (null != pathsListXml && pathsListXml.size() > 0) {
            List<Paths> pathsList = flowById.getPathsList();
            if (null == pathsList) {
                pathsList = new ArrayList<>();
            }
            for (Paths paths : pathsListXml) {
                paths.setFlow(flowById);
                pathsList.add(paths);
            }
            flowById.setPathsList(pathsList);
        }
        // save
        flowDomain.saveOrUpdate(flowById);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Successfully loaded FlowTemplate");
    }

}
