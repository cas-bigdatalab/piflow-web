package com.nature;

import com.nature.base.util.*;
import com.nature.common.Eunm.ScheduleState;
import com.nature.common.Eunm.SysRoleType;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.system.model.SysMenu;
import com.nature.component.system.model.SysSchedule;
import com.nature.domain.flow.FlowDomain;
import com.nature.domain.flow.PathsDomain;
import com.nature.domain.flow.PropertyDomain;
import com.nature.domain.flow.StopsDomain;
import com.nature.domain.mxGraph.MxCellDomain;
import com.nature.domain.mxGraph.MxGeometryDomain;
import com.nature.domain.mxGraph.MxGraphModelDomain;
import com.nature.domain.system.SysMenuDomain;
import com.nature.domain.system.SysScheduleDomain;
import com.nature.mapper.system.SysMenuMapper;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Order(value = 1)
public class StartLoader implements ApplicationRunner {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private SysScheduleDomain sysScheduleDomain;

    @Resource
    private Scheduler scheduler;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private FlowDomain flowDomain;

    @Resource
    private MxGraphModelDomain mxGraphModelDomain;

    @Resource
    private MxCellDomain mxCellDomain;

    @Resource
    private MxGeometryDomain mxGeometryDomain;

    @Resource
    private PathsDomain pathsDomain;

    @Resource
    private StopsDomain stopsDomain;

    @Resource
    private PropertyDomain propertyDomain;

    @Resource
    private SysMenuDomain sysMenuDomain;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        checkStoragePath();
        startStatusRunning();
        loadSampleNew();
    }

    private Boolean loadSample() {
        List<SysMenu> sampleMenuList = sysMenuMapper.getSampleMenuList();
        boolean loadExample1 = true;
        boolean loadExample2 = true;
        if (null != sampleMenuList && sampleMenuList.size() > 0) {
            for (SysMenu sysMenu : sampleMenuList) {
                if ("Example1".equals(sysMenu.getMenuName())) {
                    loadExample1 = false;
                } else if ("Example2".equals(sysMenu.getMenuName())) {
                    loadExample2 = false;
                }
            }
        }
        String storagePathHead = System.getProperty("user.dir") + "/src/main/resources/static/sample/";
        List<String> exampleNames = new ArrayList<>();
        if (loadExample1) {
            exampleNames.add("Example1");
        }
        if (loadExample2) {
            exampleNames.add("Example2");
        }
        for (int i = 0; i < exampleNames.size(); i++) {
            String exampleName = exampleNames.get(i);
            if (StringUtils.isBlank(exampleName)) {
                continue;
            }
            //The XML file is read and returned according to the saved file path
            String xmlFileToStr = FileUtils.XmlFileToStr(storagePathHead + exampleName + ".xml");
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
                flowXml.setIsExample(true);
                flowXml.setId(null);
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
            SysMenu sysMenu = new SysMenu();
            sysMenu.setCrtDttm(new Date());
            sysMenu.setCrtUser("system");
            sysMenu.setLastUpdateDttm(new Date());
            sysMenu.setLastUpdateUser("system");
            sysMenu.setMenuJurisdiction(SysRoleType.USER);
            sysMenu.setMenuParent("Example");
            sysMenu.setMenuName(flowXml.getName());
            sysMenu.setMenuDescription(flowXml.getName());
            sysMenu.setMenuUrl("/piflow-web/grapheditor/home?load=" + flowXml.getId());
            sysMenu.setMenuSort(500002 + i);
            sysMenuDomain.saveOrUpdate(sysMenu);
        }
        return true;
    }

    private Boolean loadSampleNew() {
        List<SysMenu> sampleMenuList = sysMenuMapper.getSampleMenuList();
        boolean loadExample1 = true;
        boolean loadExample2 = true;
        if (null != sampleMenuList && sampleMenuList.size() > 0) {
            for (SysMenu sysMenu : sampleMenuList) {
                if ("Example1".equals(sysMenu.getMenuName())) {
                    loadExample1 = false;
                } else if ("Example2".equals(sysMenu.getMenuName())) {
                    loadExample2 = false;
                }
            }
        }
        String storagePathHead = System.getProperty("user.dir") + "/src/main/resources/static/sample/";
        List<String> exampleNames = new ArrayList<>();
        if (loadExample1) {
            exampleNames.add("Example1");
        }
        if (loadExample2) {
            exampleNames.add("Example2");
        }
        for (int i = 0; i < exampleNames.size(); i++) {
            String exampleName = exampleNames.get(i);
            if (StringUtils.isBlank(exampleName)) {
                return false;
            }
            //The XML file is read and returned according to the saved file path
            String xmlFileToStr = FileUtils.XmlFileToStr(storagePathHead + exampleName + ".xml");
            if (StringUtils.isBlank(xmlFileToStr)) {
                //logger.warn("XML file read failed, loading template failed");
                return false;
            }
            Flow flowXml = FlowXmlUtils.xmlToFlow(xmlFileToStr, 2, "system");
            if (null == flowXml) {
                return false;
            }
            MxGraphModel mxGraphModel = flowXml.getMxGraphModel();
            if (null == mxGraphModel) {
                return false;
            }
            List<MxCell> root = mxGraphModel.getRoot();
            if (null == root) {
                root = new ArrayList<>();
            }
            MxCell mxCell0 = new MxCell();
            mxCell0.setMxGraphModel(mxGraphModel);
            mxCell0.setCrtDttm(new Date());
            mxCell0.setCrtUser("system");
            mxCell0.setLastUpdateDttm(new Date());
            mxCell0.setLastUpdateUser("system");
            mxCell0.setPageId("0");
            root.add(mxCell0);
            MxCell mxCell1 = new MxCell();
            mxCell1.setMxGraphModel(mxGraphModel);
            mxCell1.setCrtDttm(new Date());
            mxCell1.setCrtUser("system");
            mxCell1.setLastUpdateDttm(new Date());
            mxCell1.setLastUpdateUser("system");
            mxCell1.setPageId("1");
            mxCell1.setParent("0");
            root.add(mxCell1);
            mxGraphModel.setRoot(root);

            flowXml.setId(null);
            flowXml.setName(exampleName);
            flowXml.setIsExample(true);
            flowXml.setMxGraphModel(mxGraphModel);
            flowXml = flowDomain.saveOrUpdate(flowXml);

            SysMenu sysMenu = new SysMenu();
            sysMenu.setCrtDttm(new Date());
            sysMenu.setCrtUser("system");
            sysMenu.setLastUpdateDttm(new Date());
            sysMenu.setLastUpdateUser("system");
            sysMenu.setMenuJurisdiction(SysRoleType.USER);
            sysMenu.setMenuParent("Example");
            sysMenu.setMenuName(flowXml.getName());
            sysMenu.setMenuDescription(flowXml.getName());
            sysMenu.setMenuUrl("/piflow-web/grapheditor/home?load=" + flowXml.getId());
            sysMenu.setMenuSort(500002 + i);
            sysMenuDomain.saveOrUpdate(sysMenu);
        }
        return true;
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

    private void startStatusRunning() {
        List<SysSchedule> sysScheduleByStatusList = sysScheduleDomain.getSysScheduleByStatus(ScheduleState.RUNNING);
        if (null != sysScheduleByStatusList && sysScheduleByStatusList.size() > 0) {
            for (SysSchedule sysSchedule : sysScheduleByStatusList) {
                QuartzUtils.createScheduleJob(scheduler, sysSchedule);
            }
        }
    }
}
