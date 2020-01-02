package com.nature.component.system.service.Impl;

import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.SysRoleType;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.group.model.PropertyTemplate;
import com.nature.component.group.model.StopGroup;
import com.nature.component.group.model.StopsTemplate;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.system.model.SysInitRecords;
import com.nature.component.system.model.SysMenu;
import com.nature.component.system.service.ISysInitRecordsService;
import com.nature.domain.flow.FlowDomain;
import com.nature.domain.flow.PathsDomain;
import com.nature.domain.flow.PropertyDomain;
import com.nature.domain.flow.StopsDomain;
import com.nature.domain.mxGraph.MxCellDomain;
import com.nature.domain.mxGraph.MxGeometryDomain;
import com.nature.domain.mxGraph.MxGraphModelDomain;
import com.nature.domain.system.SysInitRecordsDomain;
import com.nature.domain.system.SysMenuDomain;
import com.nature.mapper.flow.PropertyTemplateMapper;
import com.nature.mapper.flow.StopGroupMapper;
import com.nature.mapper.flow.StopsTemplateMapper;
import com.nature.third.service.IStop;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class SysInitRecordsServiceImpl implements ISysInitRecordsService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private SysInitRecordsDomain sysInitRecordsDomain;

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

    @Resource
    private IStop stopImpl;

    @Resource
    StopGroupMapper stopGroupMapper;

    @Resource
    private StopsTemplateMapper stopsTemplateMapper;

    @Resource
    private PropertyTemplateMapper propertyTemplateMapper;

    @Override
    public String initComponents() {
        Map<String, Object> rtnMap = new HashMap<>();
        ExecutorService es = new ThreadPoolExecutor(1, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(100000));
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        es.execute(() -> {
            //loadSample();
        });
        es.execute(() -> {
            loadStopGroup(currentUser.getUsername());
        });
        String[] stopNameList = stopImpl.getAllStops();
        // The call is successful, empty the "Stop" message and insert
        stopGroupMapper.deleteStopsPropertyInfo();
        int deleteStopsInfo = stopGroupMapper.deleteStopsInfo();
        logger.info("Successful deletion StopsInfo" + deleteStopsInfo + "piece of data!!!");
        if (null != stopNameList && stopNameList.length > 0) {
            for (String stopListInfos : stopNameList) {
                es.execute(() -> {
                    Boolean aBoolean1 = loadStop(stopListInfos);
                    if (!aBoolean1) {
                        logger.warn("stop load failed, bundel : " + stopListInfos);
                    }
                });
            }
            rtnMap.put("code", 200);
        }
        es.execute(() -> {
            //addSysInitRecordsAndSave();
        });
        SysParamsCache.THREAD_POOL_EXECUTOR = ((ThreadPoolExecutor) es);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String threadMonitoring() {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        //Total number of threads
        long taskCount = SysParamsCache.THREAD_POOL_EXECUTOR.getTaskCount();
        //Number of execution completion threads
        long completedTaskCount = SysParamsCache.THREAD_POOL_EXECUTOR.getCompletedTaskCount();
        rtnMap.put("progress", (completedTaskCount / taskCount) * 100);
        rtnMap.put("code", 200);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    private Boolean loadSample() {
        String storagePathHead = System.getProperty("user.dir") + "/src/main/resources/static/sample/";
        String[] exampleNames = new String[]{"Example1.xml", "Example2.xml"};
        for (int i = 0; i < exampleNames.length; i++) {
            String exampleName = exampleNames[i];
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
                flowXml.setIsExample(true);
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
            sysMenu.setMenuSort(50002 + i);
            sysMenuDomain.saveOrUpdate(sysMenu);
        }
        return true;
    }

    private Boolean loadStopGroup(String currentUser) {
        String[] group = stopImpl.getAllGroup();
        if (null != group && group.length > 0) {
            // The call is successful, the group table information is cleared and then inserted.
            stopGroupMapper.deleteGroupCorrelation();
            int deleteGroup = stopGroupMapper.deleteGroup();
            logger.debug("Successful deletion Group" + deleteGroup + "piece of data!!!");
            int a = 0;
            for (String string : group) {
                if (string.length() > 0) {
                    StopGroup stopGroup = new StopGroup();
                    stopGroup.setId(SqlUtils.getUUID32());
                    stopGroup.setCrtDttm(new Date());
                    stopGroup.setCrtUser(currentUser);
                    stopGroup.setLastUpdateUser(currentUser);
                    stopGroup.setEnableFlag(true);
                    stopGroup.setLastUpdateDttm(new Date());
                    stopGroup.setGroupName(string);
                    int insertStopGroup = stopGroupMapper.insertStopGroup(stopGroup);
                    a += insertStopGroup;
                }
            }
            logger.debug("Successful insert Group" + a + "piece of data!!!");
        }
        return true;
    }

    private Boolean loadStop(String stopListInfos) {
        logger.info("Now the call is：" + stopListInfos);
        StopsTemplate stopsTemplate = stopImpl.getStopInfo(stopListInfos);
        if (null != stopsTemplate) {
            List<StopsTemplate> listStopsTemplate = new ArrayList<>();
            listStopsTemplate.add(stopsTemplate);
            int insertStopsTemplate = stopsTemplateMapper.insertStopsTemplate(listStopsTemplate);
            logger.info("flow_stops_template affects the number of rows : " + insertStopsTemplate);
            logger.info("=============================association_groups_stops_template=====start==================");
            List<StopGroup> stopGroupList = stopsTemplate.getStopGroupList();
            for (StopGroup stopGroup : stopGroupList) {
                String stopGroupId = stopGroup.getId();
                String stopsTemplateId = stopsTemplate.getId();
                int insertAssociationGroupsStopsTemplate = stopGroupMapper.insertAssociationGroupsStopsTemplate(stopGroupId, stopsTemplateId);
                logger.info("association_groups_stops_template Association table insertion affects the number of rows : " + insertAssociationGroupsStopsTemplate);
            }
            List<PropertyTemplate> properties = stopsTemplate.getProperties();
            int insertPropertyTemplate = propertyTemplateMapper.insertPropertyTemplate(properties);
            logger.info("flow_stops_property_template affects the number of rows : " + insertPropertyTemplate);
        }
        return true;
    }

    private Boolean addSysInitRecordsAndSave() {
        SysInitRecords sysInitRecords = new SysInitRecords();
        sysInitRecords.setId(SqlUtils.getUUID32());
        sysInitRecords.setInitDate(new Date());
        sysInitRecords.setIsSucceed(true);
        //sysInitRecordsDomain.saveOrUpdate(sysInitRecords);
        SysParamsCache.setIsBootComplete(true);
        return true;
    }


}
