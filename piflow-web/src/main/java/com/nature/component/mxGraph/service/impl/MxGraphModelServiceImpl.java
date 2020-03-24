package com.nature.component.mxGraph.service.impl;

import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.PortType;
import com.nature.component.flow.model.*;
import com.nature.component.group.model.PropertyTemplate;
import com.nature.component.group.model.StopsTemplate;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.service.IMxGraphModelService;
import com.nature.component.mxGraph.utils.MxGraphModelUtil;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.domain.flow.FlowDomain;
import com.nature.domain.flow.FlowGroupDomain;
import com.nature.domain.flow.FlowGroupPathsDomain;
import com.nature.domain.mxGraph.MxCellDomain;
import com.nature.domain.mxGraph.MxGeometryDomain;
import com.nature.domain.mxGraph.MxGraphModelDomain;
import com.nature.mapper.flow.*;
import com.nature.mapper.mxGraph.MxCellMapper;
import com.nature.mapper.mxGraph.MxGeometryMapper;
import com.nature.mapper.mxGraph.MxGraphModelMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class MxGraphModelServiceImpl implements IMxGraphModelService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private MxGraphModelMapper mxGraphModelMapper;

    @Resource
    private MxCellMapper mxCellMapper;

    @Resource
    private MxGeometryMapper mxGeometryMapper;

    @Resource
    private StopsMapper stopsMapper;

    @Resource
    private FlowMapper flowMapper;

    @Resource
    private StopsTemplateMapper stopsTemplateMapper;

    @Resource
    private PathsMapper pathsMapper;

    @Resource
    private PropertyMapper propertyMapper;

    @Resource
    private FlowDomain flowDomain;

    @Resource
    private FlowGroupDomain flowGroupDomain;

    @Resource
    private MxGraphModelDomain mxGraphModelDomain;

    @Resource
    private MxCellDomain mxCellDomain;

    @Resource
    private MxGeometryDomain mxGeometryDomain;

    @Resource
    private FlowGroupPathsDomain flowGroupPathsDomain;

    @Override
    @Transactional
    public String saveDataForTask(String imageXML, String loadId, String operType) {
        if (StringUtils.isAnyEmpty(imageXML, loadId, operType)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The incoming parameters are empty");
        }
        // Change the `XML'from the page to `mxGraphModel'
        MxGraphModelVo xmlToMxGraphModel = FlowXmlUtils.xmlToMxGraphModel(imageXML);
        // xmlToMxGraphModel Parameter null
        if (null == xmlToMxGraphModel) {
            logger.warn("The passed parameter xmlToMxGraphModel is empty and the save failed.");
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The passed parameter xmlToMxGraphModel is empty and the save failed.");
        }
        if ("ADD".equals(operType)) {
            logger.info("ADD Operation begins");
            return JsonUtils.toJsonNoException(this.addFlowStops(xmlToMxGraphModel, loadId, true));
        } else if ("MOVED".equals(operType)) {
            logger.info("MOVED Operation begins");
            return JsonUtils.toJsonNoException(this.updateFlowMxGraph(xmlToMxGraphModel, loadId));
        } else if ("REMOVED".equals(operType)) {
            logger.info("REMOVED Operation begins");
            return JsonUtils.toJsonNoException(this.updateFlow(xmlToMxGraphModel, loadId));
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Can't find operType:" + operType + " type ");
        }
        //return ReturnMapUtils.setSucceededMsgRtnJsonStr("Successful Preservation");
    }

    /**
     * Add stops and artboard mxCell
     *
     * @param mxGraphModelVo
     * @param flowId
     * @param flag           Whether to add stop information
     * @return
     */
    private Map<String, Object> addFlowStops(MxGraphModelVo mxGraphModelVo, String flowId, boolean flag) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            return ReturnMapUtils.setFailedMsg("illegal user");
        }
        // Query flow by flowId
        Flow flow = flowMapper.getFlowById(flowId);
        if (null == flow) {
            return ReturnMapUtils.setFailedMsg("Flow information with flowId: " + flowId + " is not queried");
        }
        // Determine if 'mxGraphModelVo' and 'flow' are empty
        if (null == mxGraphModelVo || null == flow) {
            return ReturnMapUtils.setFailedMsg("The passed parameter mxGraphModelVo is empty or the flow does not exist and the addition fails.");
        }
        String username = currentUser.getUsername();
        // update flow
        flow.setLastUpdateDttm(new Date()); // last update time
        flow.setLastUpdateUser(username);// last update user
        flow.setEnableFlag(true);// is it effective
        // update flow info
        int updateFlowNum = flowMapper.updateFlow(flow);
        if (updateFlowNum <= 0) {
            return ReturnMapUtils.setFailedMsg("flow update failed, update failed");
        }
        //Take out the drawing board of the data inventory
        MxGraphModel mxGraphModelDb = flow.getMxGraphModel();
        //Determine if the artboard of the data inventory exists
        if (null == mxGraphModelDb) {
            return ReturnMapUtils.setFailedMsg("Database without artboard, adding failed");
        }
        // Put the page's artboard information into the database canvas
        //Copy the value from 'mxGraphModelVo' to 'mxGraphModelDb'
        BeanUtils.copyProperties(mxGraphModelVo, mxGraphModelDb);
        mxGraphModelDb.setEnableFlag(true);
        mxGraphModelDb.setLastUpdateUser(username);
        mxGraphModelDb.setLastUpdateDttm(new Date());
        mxGraphModelDb.setFlow(flow);
        //The flow's flow foreign key has no change, no need to update
        //mxGraphModelDb.setFlow(flow); Add a foreign key
        // update mxGraphModel
        int updateMxGraphModel = mxGraphModelMapper.updateMxGraphModel(mxGraphModelDb);
        //Determine if mxGraphModelDb is updated successfully
        if (updateMxGraphModel <= 0) {
            return ReturnMapUtils.setFailedMsg("mxGraphModel update failed, update failed");
        }
        //The data passed from the page mxCellVoList
        List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
        // Map of the data sent from the page
        Map<String, MxCellVo> mxCellVoMap = new HashMap<String, MxCellVo>();
        // The mxCellList passed to the page is transferred to the map, and the key is pageId.
        if (null != mxCellVoList && mxCellVoList.size() > 0) {
            // The mxCellList passed to the page is transferred to the map, and the key is pageId.
            for (MxCellVo mxCellVo : mxCellVoList) {
                if (null != mxCellVo && StringUtils.isNotBlank(mxCellVo.getPageId())) {
                    mxCellVoMap.put(mxCellVo.getPageId(), mxCellVo);
                }
            }
        }
        // Loop database data
        List<MxCell> mxCellDbRoot = mxGraphModelDb.getRoot();
        for (MxCell mxCell : mxCellDbRoot) {
            if (null != mxCell) {
                // Use pageId to go to map
                MxCellVo mxCellVo = mxCellVoMap.get(mxCell.getPageId());
                // Get the description database already exists, do not need to add, remove the value removed in the map
                if (null != mxCellVo) {
                    mxCellVoMap.remove(mxCell.getPageId());
                }
            }
        }
        // Determine whether there is data in the map after remove, if there is any new processing
        if (mxCellVoMap.size() <= 0) {
            return ReturnMapUtils.setSucceededMsg("No data can be added, the addition failed");
        }
        // Convert MxCellVo map to MxCellVoList
        List<MxCellVo> addMxCellVoList = new ArrayList<MxCellVo>(mxCellVoMap.values());
        if (null == addMxCellVoList || addMxCellVoList.size() <= 0) {
            return ReturnMapUtils.setSucceededMsg("No data can be added, the addition failed");
        }
        for (MxCellVo mxCellVo : addMxCellVoList) {
            if (null != mxCellVo) {
                String stopByNameAndFlowId = stopsMapper.getStopByNameAndFlowId(flow.getId(), mxCellVo.getValue());
                // save MxCell
                // new
                MxCell mxCell = new MxCell();
                // Copy the value in mxCellVo to mxCell
                BeanUtils.copyProperties(mxCellVo, mxCell);
                if (StringUtils.isNotBlank(stopByNameAndFlowId)) {
                    mxCell.setValue(mxCellVo.getValue() + mxCellVo.getPageId());
                }
                // 'mxCell' basic properties (required when creating)
                mxCell.setId(SqlUtils.getUUID32());
                mxCell.setCrtDttm(new Date());
                mxCell.setCrtUser(username);
                // mxCell basic properties
                mxCell.setEnableFlag(true);
                mxCell.setLastUpdateUser(username);
                mxCell.setLastUpdateDttm(new Date());
                // mxGraphModel foreign key
                mxCell.setMxGraphModel(mxGraphModelDb);
                // save mxCell
                mxCellMapper.addMxCell(mxCell);
                MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
                if (null != mxGeometryVo) {
                    // save MxGeometry
                    // new
                    MxGeometry mxGeometry = new MxGeometry();
                    // Copy the value in mxGeometryVo to mxGeometry
                    BeanUtils.copyProperties(mxGeometryVo, mxGeometry);
                    // mxGeometry basic properties(required when creating)
                    mxGeometry.setId(SqlUtils.getUUID32());
                    mxGeometry.setCrtDttm(new Date());
                    mxGeometry.setCrtUser(username);
                    // setmxGraphModel basic properties
                    mxGeometry.setEnableFlag(true);
                    mxGeometry.setLastUpdateUser(username);
                    mxGeometry.setLastUpdateDttm(new Date());
                    // mxCell foreign key
                    mxGeometry.setMxCell(mxCell);
                    // save mxGeometry
                    mxGeometryMapper.addMxGeometry(mxGeometry);
                }

            }
        }

        //Separate 'stops' from 'path' in addMxCellVoList
        Map<String, List<MxCellVo>> stopsPathsMap = MxGraphModelUtil.distinguishElementsPaths(addMxCellVoList);

        // Take mxCellVoList(stopsList) from the Map
        List<MxCellVo> objectStops = stopsPathsMap.get("elements");

        // stops list
        List<Stops> addStopsList = new ArrayList<Stops>();
        // Generate a stopList based on the contents of the MxCellList
        addStopsList = this.mxCellVoListToStopsList(objectStops, flow);
        // save addStopsList
        if (flag) {
            this.addStopList(addStopsList);
        }

        // Take mxCellVoList(pathsList) from the Map
        List<MxCellVo> objectPaths = stopsPathsMap.get("paths");

        // path list
        List<Paths> addPathsList = new ArrayList<Paths>();

        // Generate a pathsList based on the contents of the MxCellList
        addPathsList = MxGraphModelUtil.mxCellVoListToPathsList(objectPaths, flow);
        if (flag) {
            // Judge empty
            if (null != addPathsList && addPathsList.size() > 0) {
                // save addPathsList
                int addPathsListNum = pathsMapper.addPathsList(addPathsList);
                if (addPathsListNum <= 0) {
                    logger.error("addPathsList Save failed flow：flowId(" + flow.getId() + ")", new Exception("addPathsList save failed"));
                }
            } else {
                logger.info("addPathsList is null,do not save");
            }
        }
        return ReturnMapUtils.setSucceededMsg("Successful");

    }

    /**
     * Modification of the artboard
     *
     * @param mxGraphModelVo
     * @param flowId
     * @return
     */
    private Map<String, Object> updateFlowMxGraph(MxGraphModelVo mxGraphModelVo, String flowId) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            logger.warn("illegal user");
            return ReturnMapUtils.setFailedMsg("illegal user");
        }
        MxGraphModel mxGraphModel = mxGraphModelMapper.getMxGraphModelByFlowId(flowId);
        // Determine if the incoming data is empty
        if (null == mxGraphModelVo) {
            return ReturnMapUtils.setFailedMsg("mxGraphModelVo is null, fail to edit");
        }
        // Determine if the database data to be modified is empty
        if (null == mxGraphModel) {
            return ReturnMapUtils.setFailedMsg("The mxGraphModel information with flowId is: " + flowId + " is not found, fail to edit");
        }
        String username = currentUser.getUsername();
        // Copy the value from mxGraphModelVo to mxGraphModelDb
        BeanUtils.copyProperties(mxGraphModelVo, mxGraphModel);
        // setmxGraphModel basic attribute
        mxGraphModel.setLastUpdateUser(username);// last update user
        mxGraphModel.setLastUpdateDttm(new Date());// last update time
        mxGraphModel.setEnableFlag(true);// is it effective
        // save MxGraphModel
        int updateMxGraphModel = mxGraphModelMapper.updateMxGraphModel(mxGraphModel);
        // Determine if mxGraphModel is saved successfully
        if (updateMxGraphModel <= 0) {
            return ReturnMapUtils.setFailedMsg("save failed");
        }
        // The data passed from the page MxCellVo
        List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
        // Take out the MxCellList information queried by the database.
        List<MxCell> mxCellList = mxGraphModel.getRoot();
        // Save and process mxCellList
        return this.updateMxCellList(mxCellVoList, mxCellList);
    }

    /**
     * Modify Flow
     *
     * @param mxGraphModelVo Information from the page
     * @param flowId         The data to be modified
     * @return
     */
    private Map<String, Object> updateFlow(MxGraphModelVo mxGraphModelVo, String flowId) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        Flow flow = flowMapper.getFlowById(flowId);
        if (null == flow) {
            return ReturnMapUtils.setFailedMsg("The flowId cannot find the corresponding flow, and the modification fails.");
        }
        if (null == mxGraphModelVo) {
            return ReturnMapUtils.setFailedMsg("mxGraphModelVo is empty, modification failed");
        }
        // last update time
        flow.setLastUpdateDttm(new Date());
        // last update user
        flow.setLastUpdateUser(username);

        // save flow
        int updateFlow = flowMapper.updateFlow(flow);
        // Determine whether the save is successful
        if (updateFlow <= 0) {
            return ReturnMapUtils.setFailedMsg("Modify save failed flow");
        }

        // Save and modify the artboard information
        Map<String, Object> updateMxGraphRtnMap = this.updateFlowMxGraph(mxGraphModelVo, flowId);
        // Determine if mxGraphModel is saved successfully
        if (null == updateMxGraphRtnMap || 200 != (int) updateMxGraphRtnMap.get(ReturnMapUtils.KEY_CODE)) {
            return updateMxGraphRtnMap;
        }

        // Page of MxCellVo's list
        List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();

        // Separate the stops and path in mxCellVoList
        Map<String, List<MxCellVo>> stopsPathsMap = MxGraphModelUtil.distinguishElementsPaths(mxCellVoList);

        if (null == stopsPathsMap) {
            return ReturnMapUtils.setSucceededMsg("");
        }

        // Need to delete the path
        // Key is from and to (that is, the pageId of stop) value is inport and outport
        Map<String, String> pathsDelInfoMap = new HashMap<String, String>();

        // Get out the PathsList stored in the database
        List<Paths> pathsList = flow.getPathsList();
        // Determine whether the list of lines in the database is empty, do not empty to continue the judgment operation below, or directly add
        if (null != pathsList && pathsList.size() > 0) {
            // Take the line of mxCellVoList
            List<MxCellVo> objectPaths = stopsPathsMap.get("paths");
            // Key is the PageId of PathsVo (the id in the artboard), and the value is Paths
            Map<String, MxCellVo> objectPathsMap = new HashMap<String, MxCellVo>();

            // The pathsList to be modified
            List<Paths> updatePaths = new ArrayList<Paths>();

            //Determine if the objectStops passed from the page is empty.
            if (null != objectPaths) {
                for (MxCellVo mxCellVo : objectPaths) {
                    if (null != mxCellVo) {
                        objectPathsMap.put(mxCellVo.getPageId(), mxCellVo);
                    }
                }
            }

            // The data pathsList of the loop database is retrieved by using the pageId in the stops to convert the map to the value of the page passed by the map.
            for (Paths paths : pathsList) {
                if (null != paths) {
                    String pageId = paths.getPageId();
                    MxCellVo mxCellVo = objectPathsMap.get(pageId);
                    // If you get it, you need to modify it. Otherwise, it is to be deleted.
                    if (null != mxCellVo) {
                        //There is no operation to modify the stop information when you operate the artboard. You can modify the properties, but save it immediately. No operation is required here.
                    } else {
                        paths.setEnableFlag(false);
                        paths.setLastUpdateUser(username);
                        paths.setLastUpdateDttm(new Date());
                        paths.setFlow(flow);
                        updatePaths.add(paths);
                        // Put the port information that needs to be logically deleted into the map of pathsDelInfoMap
                        pathsDelInfoMap.put("in" + paths.getTo(), paths.getInport());
                        pathsDelInfoMap.put("out" + paths.getFrom(), paths.getOutport());
                    }
                }
            }
            // Modify save updateStops
            for (Paths paths : updatePaths) {
                if (null != paths) {
                    pathsMapper.updatePaths(paths);
                }
            }
        } else {
            //The stopsList in the database is empty and the modification failed.
            logger.info("The pathsList in the database is empty");
        }


        // Get out the Stopslist stored in the database
        List<Stops> stopsList = flow.getStopsList();

        // If the list of Stops in the database is empty, the modification fails because this method only processes the modification and is not responsible for adding
        if (null != stopsList && stopsList.size() > 0) {
            // Take the mxCellVoList of stops
            List<MxCellVo> objectStops = stopsPathsMap.get("elements");
            // Key is the PageId of mxCellVo (the id in the artboard), and the value is mxCellVo
            Map<String, MxCellVo> objectStopsMap = new HashMap<String, MxCellVo>();

            // Stopslist to be modified
            List<Stops> updateStops = new ArrayList<Stops>();

            // Determine if the objectStops passed from the page is empty.
            if (null != objectStops) {
                // Loop converts objectStops (page data) to objectStopsMap
                for (MxCellVo mxCellVo : objectStops) {
                    if (null != mxCellVo) {
                        objectStopsMap.put(mxCellVo.getPageId(), mxCellVo);
                    }
                }
            }
            // The data stopsList of the loop database, using the pageId in the stops to convert to the map of the value of the page after the map is fetched,
            for (Stops stops : stopsList) {
                if (null != stops) {
                    String pageId = stops.getPageId();
                    MxCellVo mxCellVo = objectStopsMap.get(pageId);
                    // If you get it, you need to modify it. Otherwise, it is to be deleted.
                    if (null != mxCellVo) {
                        //When you operate the artboard, when you delete the line, you need to determine whether there is an Any type stop at both ends of the line. If there is any corresponding port information in the attribute, delete the corresponding port information.
                        // Determine if the stops have an Any port
                        if (stops.getInPortType() == PortType.ANY || stops.getOutPortType() == PortType.ANY) {
                            // Value in the pathsDelInfoMap according to the pageId
                            String inprot = pathsDelInfoMap.get("in" + stops.getPageId());
                            String outprot = pathsDelInfoMap.get("out" + stops.getPageId());
                            // If inprot or outprot has a value, the loop attribute finds the attribute of the corresponding storage port and modifies it.
                            if (StringUtils.isNotBlank(inprot) || StringUtils.isNotBlank(outprot)) {
                                // Take out the attribute list
                                List<Property> properties = stops.getProperties();
                                // Judge
                                if (null != properties && properties.size() > 0) {
                                    Property inportProperty = null;
                                    Property outportProperty = null;
                                    List<Property> propertiesNew = new ArrayList<Property>();
                                    // Loop attribute
                                    for (Property property : properties) {
                                        property.setStops(stops);
                                        if ("inports".equals(property.getName())) {
                                            inportProperty = property;
                                            continue;
                                        } else if ("outports".equals(property.getName())) {
                                            outportProperty = property;
                                            continue;
                                        }
                                        propertiesNew.add(property);
                                    }
                                    boolean isUpdate = false;
                                    if (null != inportProperty && StringUtils.isNotBlank(inprot)) {
                                        inportProperty = this.replaceProtValue(inprot, inportProperty);
                                        propertiesNew.add(inportProperty);
                                        isUpdate = true;
                                    }
                                    if (null != outportProperty && StringUtils.isNotBlank(outprot)) {
                                        outportProperty = this.replaceProtValue(outprot, outportProperty);
                                        propertiesNew.add(outportProperty);
                                        isUpdate = true;
                                    }
                                    stops.setProperties(propertiesNew);
                                    if (isUpdate) {
                                        stops.setLastUpdateDttm(new Date());//Last update time
                                        stops.setLastUpdateUser(username);//Last update user
                                        stops.setProperties(propertiesNew);//Attribute list
                                        stops.setFlow(flow);
                                        updateStops.add(stops);
                                    }
                                }
                            }
                        }

                    } else {
                        stops.setEnableFlag(false);//Tombstone ID
                        stops.setLastUpdateDttm(new Date());//
                        stops.setLastUpdateUser(username);//Last update user
                        //stops property
                        List<Property> properties = stops.getProperties();
                        // Whether the judgment is empty
                        if (null != properties) {
                            List<Property> propertyList = new ArrayList<Property>();
                            // Loop tombstone properties
                            for (Property property : properties) {
                                if (null != property) {
                                    property.setEnableFlag(false);//Tombstone ID
                                    property.setLastUpdateDttm(new Date());//Last update time
                                    property.setLastUpdateUser(username);//Last update user
                                    propertyList.add(property);
                                }
                            }
                            stops.setProperties(propertyList);
                        }
                        updateStops.add(stops);
                    }
                }
            }
            // Modify save updateStops
            for (Stops stops : updateStops) {
                if (null != stops) {
                    int updateStopsNum = stopsMapper.updateStops(stops);
                    if (updateStopsNum > 0) {
                        List<Property> properties = stops.getProperties();
                        for (Property property : properties) {
                            propertyMapper.updateStopsProperty(property);
                        }
                    }
                }
            }
        } else {
            // The stops data in the database is empty.
            logger.info("The stops data in the database is empty.");
        }
        return ReturnMapUtils.setSucceededMsg("Successful");
    }

    /**
     * Save addStopsList to the database
     *
     * @param addStopsList
     */
    private void addStopList(List<Stops> addStopsList) {
        // Determine if the stopsList is empty
        if (null == addStopsList || addStopsList.size() <= 0) {
            logger.info("addStopsList is empty,do not save");
            return;
        }
        // save stopsList
        int addStopsListNum = stopsMapper.addStopsList(addStopsList);
        if (addStopsListNum <= 0) {
            logger.error("New save failed addStopsList", new Exception("New save failed addStopsList"));
            return;
        }
        for (Stops stops : addStopsList) {
            // Determine if the propertyList is empty
            List<Property> propertyList = stops.getProperties();
            // Determine if the propertyList is empty
            if (null == propertyList || propertyList.size() <= 0) {
                logger.info("addPropertyList is empty,do not save");
                continue;
            }
            // save propertyList
            int addPropertyList = propertyMapper.addPropertyList(propertyList);
            if (addPropertyList <= 0) {
                logger.error("Property save failed：stopsId(" + stops.getId() + "),stopsName(" + stops.getName() + ")", new Exception("Property save failed"));
            }
        }
    }

    /**
     * Generate a list of 'stops' based on the contents of 'MxCellVoList'
     *
     * @param objectStops
     * @param flow
     * @return
     */
    private List<Stops> mxCellVoListToStopsList(List<MxCellVo> objectStops, Flow flow) {
        List<Stops> stopsList = null;
        if (null != objectStops && objectStops.size() > 0) {
            stopsList = new ArrayList<Stops>();
            // loop objectStops
            for (MxCellVo mxCellVo : objectStops) {
                Stops stops = this.stopsTemplateToStops(mxCellVo);
                if (null != stops) {
                    String stopByNameAndFlowId = stopsMapper.getStopByNameAndFlowId(flow.getId(), stops.getName());
                    if (StringUtils.isNotBlank(stopByNameAndFlowId)) {
                        stops.setName(stops.getName() + stops.getPageId());
                    }
                    stops.setFlow(flow);
                    stopsList.add(stops);
                }
            }
        }
        return stopsList;
    }

    /**
     * mxCellVo to stops
     *
     * @param mxCellVo
     * @return
     */
    private Stops stopsTemplateToStops(MxCellVo mxCellVo) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        Stops stops = null;
        if (null != mxCellVo) {
            // Take out the style attribute (the name of the stop in the attribute)
            // Example of the style attribute of stops
            // (image;html=1;labelBackgroundColor=#ffffff;image=/grapheditor/stencils/clipart/test_stops_1_128x128.png)
            // What we need is "test_stops_1"
            String style = mxCellVo.getStyle();
            if (StringUtils.isNotBlank(style)) {
                // Determine whether it is a stop by intercepting keywords
                String[] split = style.split("_128x128.");
                // Think of stops when there is one and only one keyword ("_128x128.")
                if (null != split && split.length == 2) {
                    // Take the first bit of the array and continue to intercept
                    String string = split[0];
                    String[] split2 = string.split("/");
                    // Empty, take the last bit of the array (the name of the stops)
                    if (null != split2 && split2.length > 0) {
                        // Get the name of the stops
                        String stopsName = split2[split2.length - 1];
                        // Query the stops template according to the name of the stops
                        StopsTemplate stopsTemplate = this.getStopsTemplate(stopsName);
                        // Whether to judge whether the template is empty
                        if (null != stopsTemplate) {
                            stops = new Stops();
                            BeanUtils.copyProperties(stopsTemplate, stops);
                            stops.setCrtDttm(new Date());
                            stops.setCrtUser(username);
                            stops.setLastUpdateDttm(new Date());
                            stops.setLastUpdateUser(username);
                            stops.setEnableFlag(true);
                            stops.setId(SqlUtils.getUUID32());
                            stops.setPageId(mxCellVo.getPageId());
                            List<Property> propertiesList = null;
                            List<PropertyTemplate> propertiesTemplateList = stopsTemplate.getProperties();
                            if (null != propertiesTemplateList && propertiesTemplateList.size() > 0) {
                                propertiesList = new ArrayList<Property>();
                                for (PropertyTemplate propertyTemplate : propertiesTemplateList) {
                                    Property property = new Property();
                                    BeanUtils.copyProperties(propertyTemplate, property);
                                    property.setId(SqlUtils.getUUID32());
                                    property.setCrtDttm(new Date());
                                    property.setCrtUser(username);
                                    property.setLastUpdateDttm(new Date());
                                    property.setLastUpdateUser(username);
                                    property.setEnableFlag(true);
                                    property.setStops(stops);
                                    property.setCustomValue(propertyTemplate.getDefaultValue());
                                    //Indicates "select"
                                    if (propertyTemplate.getAllowableValues().contains(",") && propertyTemplate.getAllowableValues().length() > 4) {
                                        property.setIsSelect(true);
                                        //Determine if there is a default value in "select"
                                        if (!propertyTemplate.getAllowableValues().contains(propertyTemplate.getDefaultValue())) {
                                            //Default value if not present
                                            property.setCustomValue("");
                                        }
                                    } else {
                                        property.setIsSelect(false);
                                    }
                                    propertiesList.add(property);
                                }
                            }
                            stops.setProperties(propertiesList);
                        }
                    }
                }
            }
        }
        return stops;
    }

    /**
     * Query 'stops' according to 'stopsName'
     *
     * @param stopsName
     * @return
     */
    private StopsTemplate getStopsTemplate(String stopsName) {
        StopsTemplate stopsTemplate = null;
        // Query the stops template according to the name of the stops
        List<StopsTemplate> stopsTemplateList = stopsTemplateMapper.getStopsTemplateByName(stopsName);
        if (null != stopsTemplateList && stopsTemplateList.size() > 0) {
            stopsTemplate = stopsTemplateList.get(0);
        }
        return stopsTemplate;
    }

    /**
     * Save and process mxCellList
     *
     * @param mxCellVoList
     * @param mxCellList
     * @return
     */
    private Map<String, Object> updateMxCellList(List<MxCellVo> mxCellVoList, List<MxCell> mxCellList) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";

        // If the mxCellList is empty, the modification fails because this method only processes the modifications and is not responsible for adding
        if (null == mxCellList || mxCellList.size() <= 0) {
            return ReturnMapUtils.setFailedMsg("The database mxCellList is empty and the modification failed.");
        }
        // Including modified and tombstoned
        List<MxCell> updateMxCellList = new ArrayList<MxCell>();
        // Convert the list passed to the page to map key for pageId
        Map<String, MxCellVo> mxCellVoMap = new HashMap<String, MxCellVo>();
        // Determine if it is empty
        if (null != mxCellVoList) {
            for (MxCellVo mxCell : mxCellVoList) {
                mxCellVoMap.put(mxCell.getPageId(), mxCell);
            }
        }
        // Loop page data for classification (requires modification and tombstone)
        for (MxCell mxCell : mxCellList) {
            if (null != mxCell) {
                // Graphic ID (pageId) on the artboard
                String pageId = mxCell.getPageId();
                // According to the pageId to go to map,
                // Get the description database has a page, do the modification operation,
                // Otherwise, the database has no pages, and the logical deletion is performed.
                MxCellVo mxCellVo = mxCellVoMap.get(pageId);
                if (null != mxCellVo) {
                    // Copy the value in mxCellVo to mxCell
                    BeanUtils.copyProperties(mxCellVo, mxCell);
                    // mxCell basic properties
                    mxCell.setEnableFlag(true);// Tombstone ID
                    mxCell.setLastUpdateUser(username);// last update user
                    mxCell.setLastUpdateDttm(new Date());// last update time

                    // Do not handle foreign keys when modifying, unless you cancel or modify the foreign key
                    // mxGraphModel foreign key
                    // mxCell.setMxGraphModel(mxGraphModel);
                    MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
                    MxGeometry mxGeometry = mxCell.getMxGeometry();
                    if (null != mxGeometry) {
                        if (null != mxGeometryVo) {
                            // Copy the value from mxGeometryVo into mxGeometry
                            BeanUtils.copyProperties(mxGeometryVo, mxGeometry);

                            // setmxGraphModel basic properties
                            mxGeometry.setLastUpdateUser(username);// last update user
                            mxGeometry.setLastUpdateDttm(new Date());// last update time
                            mxGeometry.setEnableFlag(true);// Tombstone ID
                            mxGeometry.setMxCell(mxCell);
                        }
                    }
                } else {
                    //Logical deletion
                    mxCell.setEnableFlag(false);
                    mxCell.setLastUpdateDttm(new Date());
                    mxCell.setLastUpdateUser(username);
                }
                // Fill in the modified list
                updateMxCellList.add(mxCell);
            }
        }
        if (null != updateMxCellList && updateMxCellList.size() > 0) {
            for (MxCell mxCell : updateMxCellList) {
                if (null == mxCell) {
                    continue;
                }
                // save mxCell
                int updateMxCell = mxCellMapper.updateMxCell(mxCell);
                if (updateMxCell <= 0) {
                    logger.warn("mxGeometryVo save failed：pageId(" + mxCell.getPageId() + "),name(" + mxCell.getValue() + ")", new Exception("mxGeometryVo save failed"));
                    continue;
                }
                MxGeometry mxGeometry = mxCell.getMxGeometry();
                if (null == mxGeometry) {
                    continue;
                }
                // Take the modified save mxGeometry
                int updateMxGeometry = mxGeometryMapper.updateMxGeometry(mxGeometry);
                if (updateMxGeometry > 0) {
                    logger.info("mxGeometry saves the success ID:" + mxGeometry.getId());
                } else {
                    logger.error("mxGeometryVo save failed：The corresponding mxCell's pageId(" + mxCell.getPageId() + "),mxCellValue(" + mxCell.getValue() + ")", new Exception("\"mxGeometryVo save failed"));
                }
            }
        }

        return ReturnMapUtils.setSucceededMsg("Successful");
    }

    /**
     * Replace port properties
     *
     * @param prot
     * @param property
     * @return
     */
    private Property replaceProtValue(String prot, Property property) {
        String customValue = property.getCustomValue();
        if (null != customValue) {
            if (customValue.contains(prot + ",")) {
                customValue = customValue.replace(prot + ",", "");
            } else if (customValue.contains("," + prot)) {
                customValue = customValue.replace("," + prot, "");
            } else if (customValue.contains(prot)) {
                customValue = customValue.replace(prot, "");
            }
            property.setCustomValue(customValue);
        }
        return property;
    }

    /**
     * save or add flowGroup
     *
     * @param imageXML
     * @param loadId
     * @param operType
     * @param flag
     * @return
     */
    @Override
    @Transactional
    public String saveDataForGroup(String imageXML, String loadId, String operType, boolean flag) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal operation");
        }
        if (StringUtils.isAnyEmpty(imageXML, loadId, operType)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The incoming parameters are empty");
        }
        // Change the `XML'from the page to `mxGraphModel'
        MxGraphModelVo mxGraphModelVo = FlowXmlUtils.xmlToMxGraphModel(imageXML);
        // Parameter null
        if (StringUtils.isAnyEmpty(loadId, operType)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The incoming parameter flowId or operType is empty and the save failed.");
        }
        // mxGraphModelVo Parameter null
        if (null == mxGraphModelVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The passed parameter mxGraphModelVo is empty and the save failed.");
        }
        if ("ADD".equals(operType)) {
            logger.info("ADD Operation begins");
            return JsonUtils.toJsonNoException(this.addGroupFlows(mxGraphModelVo, loadId, currentUser));
        } else if ("MOVED".equals(operType)) {
            logger.info("MOVED Operation begins");
            return JsonUtils.toJsonNoException(this.updateGroupMxGraph(mxGraphModelVo, loadId, currentUser));
        } else if ("REMOVED".equals(operType)) {
            logger.info("REMOVED Operation begins");
            return JsonUtils.toJsonNoException(this.updateFlowGroup(mxGraphModelVo, loadId));
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No operType:" + operType + "type");
        }
    }

    /**
     * add flows and drawing board mxCell
     *
     * @param mxGraphModelVo
     * @param flowGroupId
     * @return
     */
    private Map<String, Object> addGroupFlows(MxGraphModelVo mxGraphModelVo, String flowGroupId, UserVo currentUser) {
        if (null == currentUser) {
            return ReturnMapUtils.setFailedMsg("Illegal operation");
        }
        // Query 'flowGroup' according to 'flowGroupId'
        FlowGroup flowGroup = flowGroupDomain.getFlowGroupById(flowGroupId);
        if (null == flowGroup) {
            return ReturnMapUtils.setFailedMsg("No query to flowId is: " + flowGroupId + " Flow information");
        }
        // Determine if 'mxGraphModelVo' and 'flowGroup' are empty
        if (null == mxGraphModelVo) {
            return ReturnMapUtils.setFailedMsg("The passed parameter mxGraphModelVo is empty or the flow does not exist and the addition fails.");
        }
        // update flow
        flowGroup.setLastUpdateDttm(new Date()); // last update date time
        flowGroup.setLastUpdateUser(currentUser.getUsername());// last update user
        flowGroup.setEnableFlag(true);// is it effective

        // Take out the drawing board of the data inventory
        MxGraphModel mxGraphModel = flowGroup.getMxGraphModel();
        // Determine if the artboard of the data inventory exists
        if (null == mxGraphModel) {
            return ReturnMapUtils.setFailedMsg("Database without artboard, adding failed");
        }
        // Put the page's artboard information into the database canvas
        // Copy the value from 'mxGraphModelVo' to 'mxGraphModelDb'
        BeanUtils.copyProperties(mxGraphModelVo, mxGraphModel);
        mxGraphModel.setEnableFlag(true);
        mxGraphModel.setLastUpdateUser(currentUser.getUsername());
        mxGraphModel.setLastUpdateDttm(new Date());
        mxGraphModel.setFlowGroup(flowGroup);

        List<MxCell> mxCellDbRoot = mxGraphModel.getRoot();
        // Convert MxCellVo map to MxCellVoList
        List<MxCellVo> addMxCellVoList = this.filterNewMxCell(mxGraphModelVo.getRootVo(), mxCellDbRoot);
        ;
        if (null != addMxCellVoList && addMxCellVoList.size() <= 0) {
            return ReturnMapUtils.setFailedMsg("No data can be added, the addition failed");
        }

        for (MxCellVo mxCellVo : addMxCellVoList) {
            if (null != mxCellVo) {
                // save MxCell
                // new
                MxCell mxCell = new MxCell();
                // Copy the value in mxCellVo to mxCell
                BeanUtils.copyProperties(mxCellVo, mxCell);
                if (null != mxCellVo.getValue()) {
                    mxCell.setValue(mxCellVo.getValue() + mxCellVo.getPageId());
                }
                // Basic properties of mxCell (Required when creating)
                mxCell.setCrtDttm(new Date());
                mxCell.setCrtUser(currentUser.getUsername());
                // Basic properties of mxCell
                mxCell.setEnableFlag(true);
                mxCell.setLastUpdateUser(currentUser.getUsername());
                mxCell.setLastUpdateDttm(new Date());
                // mxGraphModel Foreign key
                mxCell.setMxGraphModel(mxGraphModel);

                MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
                if (null != mxGeometryVo) {
                    // save MxGeometry
                    // new
                    MxGeometry mxGeometry = new MxGeometry();
                    // Copy the value from mxGeometryVo to mxGeometry
                    BeanUtils.copyProperties(mxGeometryVo, mxGeometry);
                    // Basic properties of mxGeometry (required when creating)
                    mxGeometry.setCrtDttm(new Date());
                    mxGeometry.setCrtUser(currentUser.getUsername());
                    // Set mxGraphModel basic properties
                    mxGeometry.setEnableFlag(true);
                    mxGeometry.setLastUpdateUser(currentUser.getUsername());
                    mxGeometry.setLastUpdateDttm(new Date());
                    // mxCell Foreign key
                    mxGeometry.setMxCell(mxCell);

                    mxCell.setMxGeometry(mxGeometry);
                }
                mxCellDbRoot.add(mxCell);
            }
        }
        mxGraphModel.setRoot(mxCellDbRoot);
        mxGraphModel = mxGraphModelDomain.saveOrUpdate(mxGraphModel);
        flowGroup.setMxGraphModel(mxGraphModel);

        // Separate the flows and lines that need to be added in addMxCellVoList
        Map<String, List<MxCellVo>> flowsPathsMap = MxGraphModelUtil.distinguishElementsPaths(addMxCellVoList);

        // Take mxCellVoList (list of elements) from Map
        List<MxCellVo> objectElements = flowsPathsMap.get("elements");

        // Generate a list of elements based on the contents of the MxCellList
        Map<String, List> addFlowAndFlowGroupsMap = MxGraphModelUtil.mxCellVoListToFlowAndFlowGroups(objectElements, flowGroup, currentUser.getUsername());
        List<Flow> addFlowsList = addFlowAndFlowGroupsMap.get("flows");

        List<Flow> flowList = flowGroup.getFlowList();
        if (null != addFlowsList && addFlowsList.size() > 0) {
            if (null == flowList) {
                flowList = new ArrayList<>();
            }
            for (Flow flow : addFlowsList) {
                flow.setFlowGroup(flowGroup);
                flowList.add(flow);
            }
            //flowList = flowDomain.saveOrUpdate(flowList);
            flowGroup.setFlowList(flowList);
        }

        List<FlowGroup> addFlowGroupList = addFlowAndFlowGroupsMap.get("flowGroups");

        List<FlowGroup> flowGroupList = flowGroup.getFlowGroupList();
        if (null != addFlowGroupList && addFlowGroupList.size() > 0) {
            if (null == flowGroupList) {
                flowGroupList = new ArrayList<>();
            }
            for (FlowGroup addFlowGroup : addFlowGroupList) {
                addFlowGroup.setFlowGroup(flowGroup);
                flowGroupList.add(addFlowGroup);
            }
            flowGroup.setFlowGroupList(flowGroupList);
        }

        // Take "mxCellVoList" from the "Map" (array of lines)
        List<MxCellVo> objectPaths = flowsPathsMap.get("paths");

        // Generate a list of paths based on the contents of the MxCellList
        List<FlowGroupPaths> addFlowGroupPathsList = MxGraphModelUtil.mxCellVoListToFlowGroupPathsList(objectPaths, flowGroup);
        // Judge empty pathsList
        if (null != addFlowGroupPathsList && addFlowGroupPathsList.size() > 0) {
            List<FlowGroupPaths> flowGroupPathsList = flowGroup.getFlowGroupPathsList();
            for (FlowGroupPaths addFlowGroupPaths : addFlowGroupPathsList) {
                addFlowGroupPaths.setFlowGroup(flowGroup);
                flowGroupPathsList.add(addFlowGroupPaths);
            }
        }

        // Update flow information
        flowGroupDomain.saveOrUpdate(flowGroup);
        return ReturnMapUtils.setSucceededMsg("Succeeded");
    }

    /**
     * Modification of the artboard
     *
     * @param mxGraphModelVo
     * @param flowGroupId
     * @return
     */
    private Map<String, Object> updateGroupMxGraph(MxGraphModelVo mxGraphModelVo, String flowGroupId, UserVo currentUser) {
        if (null == currentUser) {
            return ReturnMapUtils.setFailedMsg("Illegal operation");
        }
        // Determine if the incoming data is empty
        if (null == mxGraphModelVo) {
            return ReturnMapUtils.setFailedMsg("mxGraphModelVo is empty, modification failed");
        }
        MxGraphModel mxGraphModel = mxGraphModelDomain.getMxGraphModelByFlowId(flowGroupId);
        // Determine if the database data to be modified is empty
        if (null == mxGraphModel) {
            return ReturnMapUtils.setFailedMsg("No query to flowGroupId is: “" + flowGroupId + "”mxGraphModel information is empty, modification failed");
        }
        // Copy the value from mxGraphModelVo to mxGraphModelDb
        BeanUtils.copyProperties(mxGraphModelVo, mxGraphModel);
        // setmxGraphModel basic properties
        mxGraphModel.setLastUpdateUser(currentUser.getUsername());// Last updater
        mxGraphModel.setLastUpdateDttm(new Date());// Last update time
        mxGraphModel.setEnableFlag(true);// is it effective
        // save MxGraphModel
        mxGraphModelDomain.saveOrUpdate(mxGraphModel);
        // The data passed from the page MxCellVo
        List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
        // Take out the MxCellList information queried by the database.
        List<MxCell> mxCellList = mxGraphModel.getRoot();
        // Save and process mxCellList
        return this.updateGroupMxCellList(mxCellVoList, mxCellList, currentUser.getUsername());
    }

    /**
     * Modify Flow
     *
     * @param mxGraphModelVo Information from the page
     * @param flowGroupId    The data to be modified
     * @return
     */
    private Map<String, Object> updateFlowGroup(MxGraphModelVo mxGraphModelVo, String flowGroupId) {
        UserVo user = SessionUserUtil.getCurrentUser();
        if (null == user) {
            return ReturnMapUtils.setFailedMsg("Illegal operation");
        }
        FlowGroup flowGroup = flowGroupDomain.getFlowGroupById(flowGroupId);
        if (null == flowGroup) {
            return ReturnMapUtils.setFailedMsg("The flowGroupId cannot find the corresponding flowGroup, and the modification fails.");
        }
        if (null == mxGraphModelVo) {
            return ReturnMapUtils.setFailedMsg("mxGraphModelVo is empty, modification failed");
        }
        // Last update time
        flowGroup.setLastUpdateDttm(new Date());
        // Last updater
        flowGroup.setLastUpdateUser(user.getUsername());

        // save flowGroup
        flowGroup = flowGroupDomain.saveOrUpdate(flowGroup);
        // Save and modify the artboard information
        Map<String, Object> map = this.updateGroupMxGraph(mxGraphModelVo, flowGroupId, user);
        // Determine if mxGraphModel is saved successfully
        if (null != map) {
            Object code = map.get("code");
            if (null == code || 200 != (int) code) {
                return map;
            }
        } else {
            return ReturnMapUtils.setFailedMsg("failed, Near 'updateMxGraph' ");
        }

        // MxCellVo's list from the page
        List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();

        // Separate the flow and lines in the mxCellVoList
        Map<String, List<MxCellVo>> stopsPathsMap = MxGraphModelUtil.distinguishElementsPaths(mxCellVoList);

        if (null != stopsPathsMap) {
            // Get out the PathsList stored in the database
            List<FlowGroupPaths> flowGroupPathsList = flowGroup.getFlowGroupPathsList();
            // Determine whether the list of lines in the database is empty, do not empty to continue the judgment operation below, or directly add
            if (null != flowGroupPathsList && flowGroupPathsList.size() > 0) {
                // Take the line of mxCellVoList
                List<MxCellVo> objectPaths = stopsPathsMap.get("paths");
                // Key is the PageId of PathsVo (the id in the artboard), and the value is Paths
                Map<String, MxCellVo> objectPathsMap = new HashMap<>();

                // The pathsList to be modified
                List<FlowGroupPaths> updatePaths = new ArrayList<>();
                //Determine if the objectStops passed from the page is empty.
                if (null != objectPaths) {
                    for (MxCellVo mxCellVo : objectPaths) {
                        if (null != mxCellVo) {
                            objectPathsMap.put(mxCellVo.getPageId(), mxCellVo);
                        }
                    }
                }

                // The data pathsList of the loop database is retrieved by using the pageId in the stops to convert the map to the value of the page passed by the map.
                for (FlowGroupPaths flowGroupPaths : flowGroupPathsList) {
                    if (null != flowGroupPaths) {
                        String pageId = flowGroupPaths.getPageId();
                        MxCellVo mxCellVo = objectPathsMap.get(pageId);
                        // If you get it, you need to modify it. Otherwise, it is to be deleted.
                        if (null == mxCellVo) {
                            flowGroupPaths.setEnableFlag(false);
                            flowGroupPaths.setLastUpdateUser(user.getUsername());
                            flowGroupPaths.setLastUpdateDttm(new Date());
                            flowGroupPaths.setFlowGroup(flowGroup);
                            updatePaths.add(flowGroupPaths);
                        }
                    }
                }
                // save update updateStops
                if (updatePaths.size() > 0) {
                    flowGroupPathsDomain.saveOrUpdate(updatePaths);
                }
            } else {
                //The stopsList in the database is empty and the modification failed.
                logger.info("The pathsList in the database is empty");
            }


            // Get out the flowList stored in the database
            List<Flow> flowList = flowGroup.getFlowList();

            // If the flowList in the database is empty, the modification fails because this method only processes the modifications, and is not responsible for adding
            if (null != flowList && flowList.size() > 0) {
                //Take the flow of mxCellVoList
                List<MxCellVo> objectStops = stopsPathsMap.get("elements");
                // Key is the PageId of mxCellVo (the id in the artboard), and the value is mxCellVo
                Map<String, MxCellVo> objectStopsMap = new HashMap<>();

                // FlowList to be modified
                List<Flow> updateFlowList = new ArrayList<>();

                // Determine if the objectStops passed from the page is empty.
                if (null != objectStops) {
                    // Loop Convert objectStops (page data) to objectStopsMap
                    for (MxCellVo mxCellVo : objectStops) {
                        if (null != mxCellVo) {
                            objectStopsMap.put(mxCellVo.getPageId(), mxCellVo);
                        }
                    }
                }
                // The data flowList of the loop database is converted to the map of the value passed by the page after the map is converted by the pageId in the flow.
                for (Flow flow : flowList) {
                    if (null != flow) {
                        String pageId = flow.getPageId();
                        MxCellVo mxCellVo = objectStopsMap.get(pageId);
                        // If you get it, you need to modify it. Otherwise, it is to be deleted.
                        if (null == mxCellVo) {
                            flow.setEnableFlag(false);//logically delete
                            flow.setLastUpdateDttm(new Date());//Last update time
                            flow.setLastUpdateUser(user.getUsername());//Last updater
                            updateFlowList.add(flow);
                        }
                    }
                }
                if (updateFlowList.size() > 0) {
                    // update save updateFlowList
                    flowDomain.saveOrUpdate(updateFlowList);
                }
            } else {
                // The stops data in the database is empty.
                logger.info("The stops data in the database is empty.");
            }
        }
        return ReturnMapUtils.setSucceededMsg("Succeeded");
    }

    /**
     * Save and process mxCellList
     *
     * @param mxCellVoList
     * @param mxCellList
     * @return
     */
    private Map<String, Object> updateGroupMxCellList(List<MxCellVo> mxCellVoList, List<MxCell> mxCellList, String username) {
        // If the mxCellList is empty, the modification fails because this method only processes the modifications and is not responsible for adding
        if (null == mxCellList || mxCellList.size() <= 0) {
            return ReturnMapUtils.setFailedMsg("The database mxCellList is empty and the modification failed.");
        }
        // Including modified and tombstoned
        List<MxCell> updateMxCellList = new ArrayList<>();
        // Convert the list passed to the page to map key for pageId
        Map<String, MxCellVo> mxCellVoMap = new HashMap<>();
        // Judge
        if (null != mxCellVoList) {
            for (MxCellVo mxCell : mxCellVoList) {
                mxCellVoMap.put(mxCell.getPageId(), mxCell);
            }
        }
        // Loop page data for classification (requires modification and tombstone)
        for (MxCell mxCell : mxCellList) {
            if (null != mxCell) {
                // Graphic ID (pageId) on the artboard
                String pageId = mxCell.getPageId();
                // According to the pageId to go to map,
                // Get the description database has a page, do the modification operation,
                // Otherwise, the database has no pages, and the logical deletion is performed.
                MxCellVo mxCellVo = mxCellVoMap.get(pageId);
                if (null != mxCellVo) {
                    // Copy the value in mxCellVo to mxCell
                    BeanUtils.copyProperties(mxCellVo, mxCell);
                    //  Basic properties of mxCell
                    mxCell.setEnableFlag(true);// is it effective
                    mxCell.setLastUpdateUser(username);// Last updater
                    mxCell.setLastUpdateDttm(new Date());// Last update time

                    // Do not handle foreign keys when modifying, unless you cancel or modify the foreign key
                    // mxGraphModel foreign key
                    // mxCell.setMxGraphModel(mxGraphModel);
                    MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
                    MxGeometry mxGeometry = mxCell.getMxGeometry();
                    if (null != mxGeometry) {
                        if (null != mxGeometryVo) {
                            // Copy the value from mxGeometryVo into mxGeometry
                            BeanUtils.copyProperties(mxGeometryVo, mxGeometry);

                            // setmxGraphModel basic properties
                            mxGeometry.setLastUpdateUser(username);// Last updater
                            mxGeometry.setLastUpdateDttm(new Date());// Last update time
                            mxGeometry.setEnableFlag(true);// is it effective
                            mxGeometry.setMxCell(mxCell);
                        }
                    }
                } else {
                    //Logical deletion
                    mxCell.setEnableFlag(false);
                    mxCell.setLastUpdateDttm(new Date());
                    mxCell.setLastUpdateUser(username);
                }
                // Fill in the modified list
                updateMxCellList.add(mxCell);
            }
        }
        if (updateMxCellList.size() > 0) {
            for (MxCell mxCell : updateMxCellList) {
                if (null != mxCell) {
                    MxGeometry mxGeometry = mxCell.getMxGeometry();
                    // save mxCell
                    mxCell = mxCellDomain.saveOrUpdate(mxCell);
                    if (null != mxGeometry) {
                        mxGeometry.setMxCell(mxCell);
                        // Take the modified save mxGeometry
                        mxGeometryDomain.saveOrUpdate(mxGeometry);
                    }
                }
            }
        }
        return ReturnMapUtils.setSucceededMsg("Succeeded");
    }

    /**
     * @param mxCellVoList Data passed from page
     * @param mxCellDbRoot database data
     * @return
     */
    private List<MxCellVo> filterNewMxCell(List<MxCellVo> mxCellVoList, List<MxCell> mxCellDbRoot) {
        List<MxCellVo> rtnMxCellVoList = null;
        // Map of the data sent from the page
        Map<String, MxCellVo> mxCellVoMap = new HashMap<>();
        // The mxCellList passed to the page is transferred to the map, and the key is pageId.
        if (null != mxCellVoList && mxCellVoList.size() > 0) {
            // The mxCellList passed to the page is transferred to the map, and the key is pageId.
            for (MxCellVo mxCellVo : mxCellVoList) {
                if (null != mxCellVo && StringUtils.isNotBlank(mxCellVo.getPageId())) {
                    mxCellVoMap.put(mxCellVo.getPageId(), mxCellVo);
                }
            }
        }
        for (MxCell mxCell : mxCellDbRoot) {
            if (null != mxCell) {
                // Use pageId to go to map
                MxCellVo mxCellVo = mxCellVoMap.get(mxCell.getPageId());
                // Get the description database already exists, do not need to add, remove the value removed in the map
                if (null != mxCellVo) {
                    mxCellVoMap.remove(mxCell.getPageId());
                }
            }
        }
        // Determine whether there is data in the map after remove, if there is any new processing
        if (mxCellVoMap.size() > 0) {
            rtnMxCellVoList = new ArrayList<>(mxCellVoMap.values());
        }
        return rtnMxCellVoList;
    }

}
