package cn.cnic.component.flow.utils;

import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.flow.entity.*;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.component.flow.vo.PathsVo;
import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGeometry;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlowUtil {

    public static Flow setFlowBasicInformation(Flow flow, boolean isSetId, String username) {
        if (null == flow) {
            flow = new Flow();
        }
        if (isSetId) {
            flow.setId(UUIDUtils.getUUID32());
        }
        //set MxGraphModel basic information
        flow.setCrtDttm(new Date());
        flow.setCrtUser(username);
        flow.setLastUpdateDttm(new Date());
        flow.setLastUpdateUser(username);
        flow.setVersion(0L);
        return flow;
    }

    /**
     * stopsList Po To Vo
     *
     * @param flowList
     * @return
     */
    public static List<FlowVo> flowListPoToVo(List<Flow> flowList) {
        List<FlowVo> flowVoList = null;
        if (null != flowList && flowList.size() > 0) {
            flowVoList = new ArrayList<>();
            for (Flow flow : flowList) {
                FlowVo flowVo = flowPoToVo(flow);
                if (null != flowVo) {
                    flowVoList.add(flowVo);
                }
            }
        }
        return flowVoList;
    }

    /**
     * stop Po To Vo
     *
     * @param flow
     * @return
     */
    public static FlowVo flowPoToVo(Flow flow) {
        FlowVo flowVo = null;
        if (null != flow) {
            flowVo = new FlowVo();
            BeanUtils.copyProperties(flow, flowVo);
            List<StopsVo> stopsVoList = StopsUtils.stopsListPoToVo(flow.getStopsList());
            List<PathsVo> pathsVoList = PathsUtil.pathsListPoToVo(flow.getPathsList());
            flowVo.setStopsVoList(stopsVoList);
            flowVo.setPathsVoList(pathsVoList);
        }
        return flowVo;
    }

    public static Flow copyCreateFlow(Flow flow, String username) {
        Flow flowNew = new Flow();
        //set MxGraphModel basic information
        flowNew.setCrtDttm(new Date());
        flowNew.setCrtUser(username);
        flowNew.setLastUpdateDttm(new Date());
        flowNew.setLastUpdateUser(username);
        // copy flow
        flowNew.setName(flow.getName());
        flowNew.setDriverMemory(flow.getDriverMemory());
        flowNew.setExecutorNumber(flow.getExecutorNumber());
        flowNew.setExecutorMemory(flow.getExecutorMemory());
        flowNew.setExecutorCores(flow.getExecutorCores());
        flowNew.setDescription(flow.getDescription());
        flowNew.setPageId(flow.getPageId());
        flowNew.setIsExample(flow.getIsExample());

        // get MxGraphModel
        MxGraphModel mxGraphModel = flow.getMxGraphModel();
        if (null != mxGraphModel) {
            MxGraphModel mxGraphModelNew = new MxGraphModel();
            // link Flow new
            mxGraphModelNew.setFlow(flowNew);
            //set MxGraphModel basic information
            mxGraphModelNew.setCrtDttm(new Date());
            mxGraphModelNew.setCrtUser(username);
            mxGraphModelNew.setLastUpdateDttm(new Date());
            mxGraphModelNew.setLastUpdateUser(username);
            //copy MxGraphModel
            mxGraphModelNew.setDx(mxGraphModel.getDx());
            mxGraphModelNew.setDy(mxGraphModel.getDy());
            mxGraphModelNew.setGrid(mxGraphModel.getGrid());
            mxGraphModelNew.setGridSize(mxGraphModel.getGridSize());
            mxGraphModelNew.setGuides(mxGraphModel.getGuides());
            mxGraphModelNew.setTooltips(mxGraphModel.getTooltips());
            mxGraphModelNew.setConnect(mxGraphModel.getConnect());
            mxGraphModelNew.setArrows(mxGraphModel.getArrows());
            mxGraphModelNew.setFold(mxGraphModel.getFold());
            mxGraphModelNew.setPage(mxGraphModel.getPage());
            mxGraphModelNew.setPageScale(mxGraphModel.getPageScale());
            mxGraphModelNew.setPageWidth(mxGraphModel.getPageWidth());
            mxGraphModelNew.setPageHeight(mxGraphModel.getPageHeight());
            mxGraphModelNew.setBackground(mxGraphModel.getBackground());

            //get MxCell array
            List<MxCell> root = mxGraphModel.getRoot();
            // Determine if the MxCell array is empty
            if (CollectionUtils.isNotEmpty(root)) {
                List<MxCell> rootNew = new ArrayList<>();
                // loop
                for (MxCell mxCell : root) {
                    //Determine if it is empty
                    if (null != mxCell) {
                        MxCell mxCellNew = new MxCell();
                        // link MxGraphModel new
                        mxCellNew.setMxGraphModel(mxGraphModelNew);
                        //set MxCell basic information
                        mxCellNew.setCrtDttm(new Date());
                        mxCellNew.setCrtUser(username);
                        mxCellNew.setLastUpdateDttm(new Date());
                        mxCellNew.setLastUpdateUser(username);
                        // copy MxCell
                        mxCellNew.setPageId(mxCell.getPageId());
                        mxCellNew.setParent(mxCell.getParent());
                        mxCellNew.setStyle(mxCell.getStyle());
                        mxCellNew.setEdge(mxCell.getEdge()); // Line has
                        mxCellNew.setSource(mxCell.getSource()); // Line has
                        mxCellNew.setTarget(mxCell.getTarget()); // Line has
                        mxCellNew.setValue(mxCell.getValue());
                        mxCellNew.setVertex(mxCell.getVertex());
                        // get MxGeometry
                        MxGeometry mxGeometry = mxCell.getMxGeometry();
                        // Determine if MxGeometry is empty
                        if (null != mxGeometry) {
                            MxGeometry mxGeometryNew = new MxGeometry();
                            // link MxCell new
                            mxGeometryNew.setMxCell(mxCellNew);
                            //set MxCell basic information
                            mxGeometryNew.setCrtDttm(new Date());
                            mxGeometryNew.setCrtUser(username);
                            mxGeometryNew.setLastUpdateDttm(new Date());
                            mxGeometryNew.setLastUpdateUser(username);
                            // copy MxGeometry
                            mxGeometryNew.setRelative(mxGeometry.getRelative());
                            mxGeometryNew.setAs(mxGeometry.getAs());
                            mxGeometryNew.setX(mxGeometry.getX());
                            mxGeometryNew.setY(mxGeometry.getY());
                            mxGeometryNew.setWidth(mxGeometry.getWidth());
                            mxGeometryNew.setHeight(mxGeometry.getHeight());

                            // set MxGeometry new
                            mxCellNew.setMxGeometry(mxGeometryNew);
                        }
                        // add MxCell new
                        rootNew.add(mxCellNew);
                    }
                }
                // set MxCell array
                mxGraphModelNew.setRoot(rootNew);
            }
            // set MxGraphModel array
            flowNew.setMxGraphModel(mxGraphModelNew);
        }

        // get StopsList
        List<Stops> stopsList = flow.getStopsList();
        // Determine if Stops array is empty
        if (CollectionUtils.isNotEmpty(stopsList)) {
            List<Stops> stopsListNew = new ArrayList<>();
            // loop
            for (Stops stops : stopsList) {
                //Determine if it is empty
                if (null != stops) {
                    Stops stopsNew = new Stops();
                    // link Flow new
                    stopsNew.setFlow(flowNew);
                    //set Stops basic information
                    stopsNew.setCrtDttm(new Date());
                    stopsNew.setCrtUser(username);
                    stopsNew.setLastUpdateDttm(new Date());
                    stopsNew.setLastUpdateUser(username);
                    // copy Stops new
                    stopsNew.setName(stops.getName());
                    stopsNew.setBundel(stops.getBundel());
                    stopsNew.setGroups(stops.getGroups());
                    stopsNew.setOwner(stops.getOwner());
                    stopsNew.setDescription(stops.getDescription());
                    stopsNew.setInports(stops.getInports());
                    stopsNew.setInPortType(stops.getInPortType());
                    stopsNew.setOutports(stops.getOutports());
                    stopsNew.setOutPortType(stops.getOutPortType());
                    stopsNew.setPageId(stops.getPageId());
                    stopsNew.setState(stops.getState());
                    stopsNew.setStartTime(stops.getStartTime());
                    stopsNew.setStopTime(stops.getStopTime());
                    stopsNew.setIsCheckpoint(stops.getIsCheckpoint());
                    stopsNew.setIsCustomized(stops.getIsCustomized());
                    stopsNew.setDataSource(stops.getDataSource());

                    // get Properties
                    List<Property> properties = stops.getProperties();
                    // Determine if Property array is empty
                    if (CollectionUtils.isNotEmpty(properties)) {
                        List<Property> propertiesNew = new ArrayList<>();
                        // loop
                        for (Property property : properties) {
                            //Determine if it is empty
                            if (null != property) {
                                Property propertyNew = new Property();
                                //link Stops new
                                propertyNew.setStops(stopsNew);
                                //set Property basic information
                                propertyNew.setCrtDttm(new Date());
                                propertyNew.setCrtUser(username);
                                propertyNew.setLastUpdateDttm(new Date());
                                propertyNew.setLastUpdateUser(username);
                                // copy Property
                                propertyNew.setName(property.getName());
                                propertyNew.setDisplayName(property.getDisplayName());
                                propertyNew.setDescription(property.getDescription());
                                propertyNew.setCustomValue(property.getCustomValue());
                                propertyNew.setAllowableValues(property.getAllowableValues());
                                propertyNew.setRequired(property.getRequired());
                                propertyNew.setSensitive(property.getSensitive());
                                propertyNew.setIsSelect(property.getIsSelect());
                                propertyNew.setIsLocked(property.getIsLocked());
                                propertyNew.setPropertySort(property.getPropertySort());
                                // add Property new
                                propertiesNew.add(propertyNew);
                            }
                        }
                        // set Properties
                        stopsNew.setProperties(propertiesNew);
                    }
                    List<CustomizedProperty> customizedPropertyList = stops.getCustomizedPropertyList();
                    // Determine if CustomizedProperty array is empty
                    if (CollectionUtils.isNotEmpty(customizedPropertyList)) {
                        List<CustomizedProperty> customizedPropertyListNew = new ArrayList<>();
                        // loop
                        for (CustomizedProperty customizedProperty : customizedPropertyList) {
                            //Determine if it is empty
                            if (null != customizedProperty) {
                                CustomizedProperty customizedPropertyNew = new CustomizedProperty();
                                //link Stops new
                                customizedPropertyNew.setStops(stopsNew);
                                //set CustomizedProperty basic information
                                customizedPropertyNew.setCrtDttm(new Date());
                                customizedPropertyNew.setCrtUser(username);
                                customizedPropertyNew.setLastUpdateDttm(new Date());
                                customizedPropertyNew.setLastUpdateUser(username);
                                // copy Property
                                customizedPropertyNew.setName(customizedProperty.getName());
                                customizedPropertyNew.setCustomValue(customizedProperty.getCustomValue());
                                customizedPropertyNew.setDescription(customizedProperty.getDescription());
                                // add CustomizedProperty new
                                customizedPropertyListNew.add(customizedPropertyNew);
                            }
                        }
                        // set CustomizedPropertyList
                        stopsNew.setCustomizedPropertyList(customizedPropertyListNew);
                    }
                    // add Stops new
                    stopsListNew.add(stopsNew);
                }
            }
            // set StopsList
            flowNew.setStopsList(stopsListNew);
        }
        List<Paths> pathsList = flow.getPathsList();
        if (CollectionUtils.isNotEmpty(pathsList)) {
            List<Paths> pathsListNew = new ArrayList<>();
            for (Paths paths : pathsList) {
                if (null != paths) {
                    Paths pathsNew = new Paths();
                    //link Flow new
                    pathsNew.setFlow(flowNew);
                    //set Paths basic information
                    pathsNew.setCrtDttm(new Date());
                    pathsNew.setCrtUser(username);
                    pathsNew.setLastUpdateDttm(new Date());
                    pathsNew.setLastUpdateUser(username);
                    // copy Path
                    pathsNew.setFrom(paths.getFrom());
                    pathsNew.setOutport(paths.getOutport());
                    pathsNew.setInport(paths.getInport());
                    pathsNew.setTo(paths.getTo());
                    pathsNew.setPageId(paths.getPageId());
                    pathsNew.setFilterCondition(paths.getFilterCondition());
                    // add Paths new
                    pathsListNew.add(pathsNew);
                }
            }
            // set PathsList
            flowNew.setPathsList(pathsListNew);
        }
        return flowNew;
    }
}
