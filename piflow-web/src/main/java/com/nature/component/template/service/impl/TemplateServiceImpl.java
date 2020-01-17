package com.nature.component.template.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nature.base.util.*;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Stops;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.utils.MxCellUtils;
import com.nature.component.mxGraph.utils.MxGraphModelUtil;
import com.nature.component.template.model.Template;
import com.nature.component.template.service.ITemplateService;
import com.nature.domain.flow.FlowDomain;
import com.nature.mapper.flow.FlowMapper;
import com.nature.mapper.template.TemplateMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TemplateServiceImpl implements ITemplateService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private TemplateMapper templateMapper;

    @Resource
    private FlowMapper flowMapper;

    @Resource
    private FlowDomain flowDomain;

    @Override
    public int addTemplate(Template template) {
        return templateMapper.addFlow(template);
    }

    @Override
    public List<Template> findTemPlateList() {
        return templateMapper.findTemPlateList();
    }

    @Override
    public int deleteTemplate(String id) {
        return templateMapper.updateEnableFlagById(id);
    }

    @Override
    public Template queryTemplate(String id) {
        return templateMapper.queryTemplate(id);
    }

    @Override
    public String getTemplateListPage(Integer offset, Integer limit, String param) {
        Map<String, Object> rtnMap = new HashMap<>();
        if (null != offset && null != limit) {
            Page<Template> page = PageHelper.startPage(offset, limit);
            templateMapper.findTemPlateListPage(param);
            rtnMap = PageHelperUtils.setDataTableParam(page, rtnMap);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String loadTemplateToFlow(String flowId, String templateId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        if (StringUtils.isBlank(currentUsername)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal user, Load failed");
        }
        if (null == flowId) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("FlowId is empty, loading template failed");
        }
        if (null == templateId) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("TemplateId is empty, loading template failed");
        }
        Flow flowById = flowDomain.getFlowById(flowId);
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Flow is empty, loading template failed");
        }
        Template template = templateMapper.queryTemplate(templateId);
        if (null == template) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Template is empty, loading template failed");
        }
        //Read the xml file according to the saved file path and return
        String xmlFileToStr = FileUtils.XmlFileToStr(template.getPath());
        if (StringUtils.isBlank(xmlFileToStr)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The xml file failed to read and the template failed to be loaded.");
        }
        // Get the maximum pageId in stop
        String maxStopPageId = flowMapper.getMaxStopPageId(flowId);
        Flow templateXmlToFlow = FlowXmlUtils.templateXmlToFlow(xmlFileToStr, currentUsername, maxStopPageId, null);
        if (null == templateXmlToFlow) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Conversion failure");
        }
        // Added processing artboard data
        // Fetch the artboard data to be added
        MxGraphModel mxGraphModelXml = templateXmlToFlow.getMxGraphModel();
        if (null != mxGraphModelXml) {
            MxGraphModel mxGraphModel = flowById.getMxGraphModel();
            if (null == mxGraphModel) {
                mxGraphModel = MxGraphModelUtil.setMxGraphModelBasicInformation(null, false, currentUsername);
            } else {
                // Update basic information
                mxGraphModel = MxGraphModelUtil.updateMxGraphModelBasicInformation(mxGraphModel, currentUsername);
            }
            mxGraphModel.setFlow(flowById);
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
        List<Stops> stopsListXml = templateXmlToFlow.getStopsList();
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
        List<Paths> pathsListXml = templateXmlToFlow.getPathsList();
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
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Successfully loaded template");
    }

}
