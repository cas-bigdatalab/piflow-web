package com.nature;

import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ScheduleState;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.group.service.IStopGroupService;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.system.model.SysSchedule;
import com.nature.domain.flow.FlowDomain;
import com.nature.domain.flow.PathsDomain;
import com.nature.domain.flow.PropertyDomain;
import com.nature.domain.flow.StopsDomain;
import com.nature.domain.mxGraph.MxCellDomain;
import com.nature.domain.mxGraph.MxGeometryDomain;
import com.nature.domain.mxGraph.MxGraphModelDomain;
import com.nature.domain.system.SysScheduleDomain;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Order(value = 1)
public class StartLoader implements ApplicationRunner {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private SysScheduleDomain sysScheduleDomain;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private IStopGroupService stopGroupServiceImpl;

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
    public void run(ApplicationArguments args) throws Exception {
        checkStoragePath();
        loadStop();
        startStatusRunning();
    }

    private void checkStoragePath() {
        String storagePathHead = System.getProperty("user.dir");
        CheckPathUtils.isChartPathExist(storagePathHead + "/storage/image/");
        CheckPathUtils.isChartPathExist(storagePathHead + "/storage/video/");
        CheckPathUtils.isChartPathExist(storagePathHead + "/storage/xml/");
        SysParamsCache.setImagesPath(storagePathHead + "/storage/image/");
        SysParamsCache.setVideosPath(storagePathHead + "/storage/video/");
        SysParamsCache.setXmlPath(storagePathHead + "/storage/xml/");
    }

    private void loadStop() {
        if (SysParamsCache.IS_LOAD_STOP) {
            logger.warn(new Date() + ":Loading components");
            UserVo userVo = new UserVo();
            userVo.setUsername("system");
            stopGroupServiceImpl.addGroupAndStopsList(userVo);
            logger.warn(new Date() + ":Loading Component Completion");
        }

    }

    private void loadSample() {
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


    }

    private void startStatusRunning() {
        List<SysSchedule> sysScheduleByStatusList = sysScheduleDomain.getSysScheduleByStatus(ScheduleState.RUNNING);
        if (null != sysScheduleByStatusList && sysScheduleByStatusList.size() > 0) {
            for (SysSchedule sysSchedule : sysScheduleByStatusList) {
                QuartzUtils.createScheduleJob(scheduler, sysSchedule);
            }
        }
    }
}
