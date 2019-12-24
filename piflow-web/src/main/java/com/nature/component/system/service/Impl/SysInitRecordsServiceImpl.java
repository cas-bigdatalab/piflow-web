package com.nature.component.system.service.Impl;

import com.nature.base.util.*;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.system.model.SysInitRecords;
import com.nature.component.system.service.ISysInitRecordsService;
import com.nature.domain.flow.FlowDomain;
import com.nature.domain.flow.PathsDomain;
import com.nature.domain.flow.PropertyDomain;
import com.nature.domain.flow.StopsDomain;
import com.nature.domain.mxGraph.MxCellDomain;
import com.nature.domain.mxGraph.MxGeometryDomain;
import com.nature.domain.mxGraph.MxGraphModelDomain;
import com.nature.domain.system.SysInitRecordsDomain;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysInitRecordsServiceImpl implements ISysInitRecordsService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private SysInitRecordsDomain sysInitRecordsDomain;

    @Autowired
    private FlowDomain flowDomain;

    @Autowired
    private MxGraphModelDomain mxGraphModelDomain;

    @Autowired
    private MxCellDomain mxCellDomain;

    @Autowired
    private MxGeometryDomain mxGeometryDomain;

    @Autowired
    private PathsDomain pathsDomain;

    @Autowired
    private StopsDomain stopsDomain;

    @Autowired
    private PropertyDomain propertyDomain;

    @Override
    public String addSysInitRecords() {
        Map<String, Object> rtnMap = new HashMap<>();
        SysInitRecords sysInitRecords = new SysInitRecords();
        sysInitRecords.setId(SqlUtils.getUUID32());
        sysInitRecords.setInitDate(new Date());
        sysInitRecords.setIsSucceed(true);
        sysInitRecordsDomain.saveOrUpdate(sysInitRecords);
        SysParamsCache.setIsBootComplete(true);
        rtnMap.put("code", 200);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String initSample() {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        String storagePathHead = System.getProperty("user.dir") + "/src/main/resources/static/sample/";
        String[] exampleNames = new String[]{"Example1.xml", "Example2.xml"};
        for (String exampleName : exampleNames) {
            //The XML file is read and returned according to the saved file path
            String xmlFileToStr = FileUtils.XmlFileToStr(storagePathHead + exampleName);
            if (StringUtils.isBlank(xmlFileToStr)) {
                //logger.warn("XML file read failed, loading template failed");
                continue;
            }
            Flow flowXml = FlowXmlUtils.xmlToFlow(xmlFileToStr, 2, "system");
            if (null != flowXml) {
                List<Stops> stopsListXml = flowXml.getStopsList();
                List<Paths> pathsListXml = flowXml.getPathsList();
                MxGraphModel flowMxGraphModelXml = flowXml.getMxGraphModel();
                flowXml.setName(exampleName);
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
        rtnMap.put("code", 200);
        return JsonUtils.toJsonNoException(rtnMap);
    }

}
