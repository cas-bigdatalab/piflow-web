package com.nature.component.template.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.flow.model.*;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.template.model.FlowGroupTemplate;
import com.nature.component.template.service.IFlowGroupTemplateService;
import com.nature.component.template.vo.FlowGroupTemplateVo;
import com.nature.domain.flow.*;
import com.nature.domain.mxGraph.MxCellDomain;
import com.nature.domain.mxGraph.MxGeometryDomain;
import com.nature.domain.mxGraph.MxGraphModelDomain;
import com.nature.mapper.flow.FlowGroupMapper;
import com.nature.mapper.flow.FlowMapper;
import com.nature.mapper.template.FlowGroupTemplateMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class FlowGroupTemplateServiceImpl implements IFlowGroupTemplateService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private FlowGroupTemplateMapper flowGroupTemplateMapper;

    @Resource
    private FlowGroupMapper flowGroupMapper;

    @Resource
    private FlowMapper flowMapper;

    @Autowired
    private FlowGroupTemplateDomain flowGroupTemplateDomain;

    @Autowired
    private FlowGroupDomain flowGroupDomain;

    @Autowired
    private MxGraphModelDomain mxGraphModelDomain;

    @Autowired
    private MxCellDomain mxCellDomain;

    @Autowired
    private MxGeometryDomain mxGeometryDomain;

    @Autowired
    private FlowGroupPathsDomain flowGroupPathsDomain;

    @Autowired
    private FlowDomain flowDomain;

    @Autowired
    private StopsDomain stopsDomain;

    @Autowired
    private PropertyDomain propertyDomain;

    @Autowired
    private PathsDomain pathsDomain;


    /**
     * add FlowGroupTemplate
     *
     * @param name
     * @param loadId
     * @param value
     * @return
     */
    @Override
    @Transactional
    public String addFlowGroupTemplate(String name, String loadId, String value) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        MxGraphModelVo mxGraphModelVo = null;
        if (StringUtils.isAnyEmpty(name, loadId)) {
            rtnMap.put("errorMsg", "Some parameters passed in are empty");
            logger.info("Some parameters passed in are empty");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        FlowGroup flowGroupById = flowGroupMapper.getFlowGroupById(loadId);
        if (null != flowGroupById) {
            //Splicing XML according to flowById
            String flowGroupXmlStr = FlowXmlUtils.flowGroupToXmlStr(flowGroupById);
            logger.info(flowGroupXmlStr);

            FlowGroupTemplate flowGroupTemplate = new FlowGroupTemplate();
            flowGroupTemplate.setId(SqlUtils.getUUID32());
            flowGroupTemplate.setCrtDttm(new Date());
            flowGroupTemplate.setCrtUser(username);
            flowGroupTemplate.setEnableFlag(true);
            flowGroupTemplate.setLastUpdateUser(username);
            flowGroupTemplate.setLastUpdateDttm(new Date());
            flowGroupTemplate.setName(name);
            //XML to file and save to specified directory
            String path = FileUtils.createXml(flowGroupXmlStr, name, ".xml", SysParamsCache.XML_PATH);
            flowGroupTemplate.setPath(path);
            flowGroupTemplateDomain.saveOrUpdate(flowGroupTemplate);
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "save template success");
            return JsonUtils.toJsonNoException(rtnMap);
        } else {
            rtnMap.put("errorMsg", "Flow information is empty");
            logger.info("Flow information is empty,loadId：" + loadId);
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    @Override
    public String getFlowGroupTemplateListPage(Integer offset, Integer limit, String param) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        if (null != offset && null != limit) {
            Page page = PageHelper.startPage(offset, limit);
            flowGroupTemplateMapper.getFlowGroupTemplateVoListPage(param);
            rtnMap = PageHelperUtils.setDataTableParam(page, rtnMap);
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
    @Transactional
    public int deleteFlowGroupTemplate(String id) {
        int deleteTemplate = 0;
        if (StringUtils.isNoneBlank(id)) {
            deleteTemplate = flowGroupTemplateDomain.updateEnableFlagById(id, false);
        }
        return deleteTemplate;
    }

    /**
     * Download template
     *
     * @param flowGroupTemplateId
     */
    @Override
    public void templateDownload(HttpServletResponse response, String flowGroupTemplateId) {
        FlowGroupTemplateVo flowGroupTemplateVo = flowGroupTemplateMapper.getFlowGroupTemplateVoById(flowGroupTemplateId);
        if (null == flowGroupTemplateVo) {
            logger.info("Template is empty,Download template failed");
        } else {
            try {
                // Download local files
                String fileName = flowGroupTemplateVo.getName() + ".xml".toString(); // 文件的默认保存名
                // Read to the stream
                InputStream inStream = new FileInputStream(flowGroupTemplateVo.getPath());// 文件的存放路径
                // Format the output
                response.reset();
                response.setContentType("bin");
                response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                // Loop out the data in the stream
                byte[] b = new byte[100];
                int len;

                while ((len = inStream.read(b)) > 0)
                    response.getOutputStream().write(b, 0, len);
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Upload xml file and save flowGroupTemplate
     *
     * @param file
     * @return
     */
    @Override
    @Transactional
    public String uploadXmlFile(MultipartFile file) {
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
                FlowGroupTemplate flowGroupTemplate = new FlowGroupTemplate();
                flowGroupTemplate.setId(SqlUtils.getUUID32());
                flowGroupTemplate.setCrtDttm(new Date());
                flowGroupTemplate.setCrtUser(username);
                flowGroupTemplate.setEnableFlag(true);
                flowGroupTemplate.setLastUpdateUser(username);
                flowGroupTemplate.setLastUpdateDttm(new Date());
                SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmSSSS");
                Date nowDate = new Date();
                String fileName = sdf.format(nowDate);
                //File name prefix
                String prefix = name.substring(0, name.length() - 4);
                //Suffix .xml
                String Suffix = name.substring(name.length() - 4);
                //Add timestamp
                String uploadfileName = prefix + "-" + fileName;
                flowGroupTemplate.setName(uploadfileName + Suffix);
                flowGroupTemplate.setPath(path);
                //Read the XML file according to the saved file path and return the XML string
                String xmlFileToStr = FileUtils.XmlFileToStr(flowGroupTemplate.getPath());
                if (StringUtils.isBlank(xmlFileToStr)) {
                    logger.info("XML file read failed, upload template failed");
                    rtnMap.put("errorMsg", "XML file read failed, upload template failed");
                    return JsonUtils.toJsonNoException(rtnMap);
                }

                flowGroupTemplate = flowGroupTemplateDomain.saveOrUpdate(flowGroupTemplate);
                rtnMap.put("code", 200);
                rtnMap.put("errorMsg", "successful template upload");
                logger.info("Template uploaded successfully");
                return JsonUtils.toJsonNoException(rtnMap);
            }
        }
        rtnMap.put("errorMsg", "Upload failed, please try again later");
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String flowGroupTemplateAllSelect() {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        List<FlowGroupTemplateVo> findTemPlateList = flowGroupTemplateMapper.getFlowGroupTemplateVoListPage(null);
        if (null != findTemPlateList && findTemPlateList.size() > 0) {
            rtnMap.put("code", 200);
            rtnMap.put("temPlateList", findTemPlateList);
        } else {
            rtnMap.put("errorMsg", "Query result is empty");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    @Transactional
    public String loadFlowGroupTemplate(String templateId, String loadId) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            logger.info("Illegal user, Load failed");
            rtnMap.put("errorMsg", "Illegal user, Load failed");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(loadId);
        if (null == flowGroupById) {
            logger.info("Template is empty and failed to load the template");
            rtnMap.put("errorMsg", "Load failed, please try again");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        FlowGroupTemplateVo flowGroupTemplateVo = flowGroupTemplateMapper.getFlowGroupTemplateVoById(templateId);
        if (null == flowGroupTemplateVo) {
            logger.info("Template is empty and failed to load the template");
            rtnMap.put("errorMsg", "Load failed, please try again");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        //The XML file is read and returned according to the saved file path
        String xmlFileToStr = FileUtils.XmlFileToStr(flowGroupTemplateVo.getPath());
        if (StringUtils.isBlank(xmlFileToStr)) {
            logger.info("XML file read failed, loading template failed");
            rtnMap.put("errorMsg", "XML file read failed, loading template failed");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Get the maximum value of pageid in stop
        String maxStopPageIdByFlowGroupId = flowMapper.getMaxFlowPageIdByFlowGroupId(loadId);
        maxStopPageIdByFlowGroupId = StringUtils.isNotBlank(maxStopPageIdByFlowGroupId) ? maxStopPageIdByFlowGroupId : "0";
        int maxPageId = Integer.parseInt(maxStopPageIdByFlowGroupId);
        String username = currentUser.getUsername();
        FlowGroup flowGroupXml = FlowXmlUtils.XmlStrToFlowGroup(xmlFileToStr, maxPageId, username);
        if (null == flowGroupXml) {
            logger.info("Conversion failure");
            rtnMap.put("errorMsg", "Conversion failure");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Added processing artboard data
        // Fetch the artboard data to be added
        MxGraphModel mxGraphModelXml = flowGroupXml.getMxGraphModel();
        if (null != mxGraphModelXml) {
            MxGraphModel mxGraphModel = flowGroupById.getMxGraphModel();
            if (null == mxGraphModel) {
                mxGraphModel = new MxGraphModel();
                mxGraphModel.setFlowGroup(flowGroupById);
                mxGraphModel.setId(SqlUtils.getUUID32());
                mxGraphModel.setCrtDttm(new Date());
                mxGraphModel.setCrtUser(username);
                mxGraphModel.setLastUpdateDttm(new Date());
                mxGraphModel.setLastUpdateUser(username);
                mxGraphModel.setEnableFlag(true);
                mxGraphModel = mxGraphModelDomain.saveOrUpdate(mxGraphModel);
            }
            if (null == mxGraphModel.getRoot() || mxGraphModel.getRoot().size() <= 1) {
                List<MxCell> mxCellList = initMxCell(username, mxGraphModel);
                if (null != mxCellList) {
                    mxCellDomain.saveOrUpdate(mxCellList);
                }
            }
            List<MxCell> rootXml = mxGraphModelXml.getRoot();
            if (null != rootXml && rootXml.size() > 0) {
                for (MxCell mxCell : rootXml) {
                    if (null != mxCell) {
                        // get mxGeometry
                        MxGeometry mxGeometry = mxCell.getMxGeometry();
                        //Associated sketchpad
                        mxCell.setMxGraphModel(mxGraphModel);
                        mxCell.setMxGeometry(null);
                        //new
                        mxCell = mxCellDomain.saveOrUpdate(mxCell);
                        if (null != mxGeometry) {
                            mxGeometry.setMxCell(mxCell);
                            // new
                            mxGeometryDomain.saveOrUpdate(mxGeometry);
                        }

                    }
                }
            }
        }
        // Added processing flow data
        List<Flow> flowListXml = flowGroupXml.getFlowList();
        if (null != flowListXml && flowListXml.size() > 0) {
            for (Flow flowXml : flowListXml) {
                if (null != flowXml) {
                    List<Stops> stopsListXml = flowXml.getStopsList();
                    List<Paths> pathsListXml = flowXml.getPathsList();
                    MxGraphModel flowMxGraphModelXml = flowXml.getMxGraphModel();

                    flowXml.setFlowGroup(flowGroupById);
                    flowXml.setMxGraphModel(null);
                    flowXml = flowDomain.saveOrUpdate(flowXml);
                    if (null != flowMxGraphModelXml) {
                        List<MxCell> root = flowMxGraphModelXml.getRoot();
                        flowMxGraphModelXml.setRoot(null);
                        flowMxGraphModelXml.setFlow(flowXml);
                        flowMxGraphModelXml = mxGraphModelDomain.saveOrUpdate(flowMxGraphModelXml);
                        for (MxCell mxCell : root) {
                            MxGeometry flowMxGeometryXml = mxCell.getMxGeometry();
                            mxCell.setMxGeometry(null);
                            mxCell.setMxGraphModel(flowMxGraphModelXml);
                            mxCell = mxCellDomain.saveOrUpdate(mxCell);
                            if (null != flowMxGeometryXml) {
                                flowMxGeometryXml.setMxCell(mxCell);
                                mxGeometryDomain.saveOrUpdate(flowMxGeometryXml);
                            }
                        }
                    }
                    if (null != pathsListXml && pathsListXml.size() > 0) {
                        for (Paths paths : pathsListXml) {
                            paths.setFlow(flowXml);
                        }
                        pathsDomain.saveOrUpdate(pathsListXml);
                    }
                    if (null != stopsListXml && stopsListXml.size() > 0) {
                        for (Stops stops : stopsListXml) {
                            List<Property> propertyListXml = stops.getProperties();
                            stops.setProperties(null);
                            stops.setFlow(flowXml);
                            stops = stopsDomain.saveOrUpdate(stops);
                            for (Property property : propertyListXml) {
                                property.setStops(stops);
                            }
                            propertyDomain.saveOrUpdate(propertyListXml);
                        }
                    }
                }
            }
        }
        // Added processing of flowGroupPath data
        List<FlowGroupPaths> flowGroupPathsListXml = flowGroupXml.getFlowGroupPathsList();
        if (null != flowGroupPathsListXml && flowGroupPathsListXml.size() > 0) {
            for (FlowGroupPaths flowGroupPathsXml : flowGroupPathsListXml) {
                flowGroupXml.setFlowGroupPathsList(null);
                flowGroupPathsXml.setFlowGroup(flowGroupById);
            }
            flowGroupPathsDomain.saveOrUpdate(flowGroupPathsListXml);
        }
        rtnMap.put("code", 200);
        rtnMap.put("errorMsg", "success");
        return JsonUtils.toJsonNoException(rtnMap);
    }

    private List<MxCell> initMxCell(String username, MxGraphModel mxGraphModel) {
        if (StringUtils.isBlank(username) && null == mxGraphModel) {
            return null;
        }
        List<MxCell> pMxCellList = new ArrayList<>();
        MxCell mxCell0 = new MxCell();
        mxCell0.setCrtDttm(new Date());
        mxCell0.setCrtUser(username);
        mxCell0.setEnableFlag(true);
        mxCell0.setLastUpdateDttm(new Date());
        mxCell0.setLastUpdateUser(username);
        mxCell0.setVersion(0L);
        mxCell0.setPageId("0");
        mxCell0.setMxGraphModel(mxGraphModel);
        pMxCellList.add(mxCell0);
        MxCell mxCell1 = new MxCell();
        mxCell1.setCrtDttm(new Date());
        mxCell1.setCrtUser(username);
        mxCell1.setEnableFlag(true);
        mxCell1.setLastUpdateDttm(new Date());
        mxCell1.setLastUpdateUser(username);
        mxCell1.setVersion(0L);
        mxCell1.setParent("0");
        mxCell1.setPageId("1");
        mxCell1.setMxGraphModel(mxGraphModel);
        pMxCellList.add(mxCell1);
        return pMxCellList;
    }

}
