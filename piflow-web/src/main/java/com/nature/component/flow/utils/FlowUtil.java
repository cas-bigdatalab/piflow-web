package com.nature.component.flow.utils;

import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.flow.vo.*;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlowUtil {
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
            List<StopsVo> stopsVoList = StopsUtil.stopsListPoToVo(flow.getStopsList());
            List<PathsVo> pathsVoList = PathsUtil.pathsListPoToVo(flow.getPathsList());
            flowVo.setStopsVoList(stopsVoList);
            flowVo.setPathsVoList(pathsVoList);
        }
        return flowVo;
    }

    public static Flow copyCreateFlow(Flow flow, String username) {
        Flow flowNew = new Flow();
        //copy
        BeanUtils.copyProperties(flow, flowNew);
        //set flow basic information
        flowNew.setId(null);
        flowNew.setCrtDttm(new Date());
        flowNew.setCrtUser(username);
        flowNew.setLastUpdateDttm(new Date());
        flowNew.setLastUpdateUser(username);
        flowNew.setUuid(null);
        // get MxGraphModel
        MxGraphModel mxGraphModel = flowNew.getMxGraphModel();
        //Determine if it is empty
        if (null != mxGraphModel) {
            //set MxGraphModel basic information
            mxGraphModel.setId(null);
            mxGraphModel.setCrtDttm(new Date());
            mxGraphModel.setCrtUser(username);
            mxGraphModel.setLastUpdateDttm(new Date());
            mxGraphModel.setLastUpdateUser(username);
            // link Flow
            mxGraphModel.setFlow(flowNew);
            //get artboard MxCell properties
            List<MxCell> root = mxGraphModel.getRoot();
            // Determine if the MxCell array is empty
            if (CollectionUtils.isNotEmpty(root)) {
                // loop
                for (MxCell mxCell : root) {
                    //Determine if it is empty
                    if (null != mxCell) {
                        //set MxCell basic information
                        mxCell.setId(null);
                        mxCell.setCrtDttm(new Date());
                        mxCell.setCrtUser(username);
                        mxCell.setLastUpdateDttm(new Date());
                        mxCell.setLastUpdateUser(username);
                        // link MxGraphModel
                        mxCell.setMxGraphModel(mxGraphModel);
                        // get MxGeometry
                        MxGeometry mxGeometry = mxCell.getMxGeometry();
                        // Determine if MxGeometry is empty
                        if (null != mxGeometry) {
                            //set MxCell basic information
                            mxGeometry.setId(null);
                            mxGeometry.setCrtDttm(new Date());
                            mxGeometry.setCrtUser(username);
                            mxGeometry.setLastUpdateDttm(new Date());
                            mxGeometry.setLastUpdateUser(username);
                            // link MxCell
                            mxGeometry.setMxCell(mxCell);
                            // set MxGeometry
                            mxCell.setMxGeometry(mxGeometry);
                        }
                    }
                }
                // set root
                mxGraphModel.setRoot(root);
            }
            flowNew.setMxGraphModel(mxGraphModel);
        }
        // get StopsList
        List<Stops> stopsList = flowNew.getStopsList();
        // Determine if Stops array is empty
        if (CollectionUtils.isNotEmpty(stopsList)) {
            // loop
            for (Stops stops : stopsList) {
                //Determine if it is empty
                if (null != stops) {
                    //set Stops basic information
                    stops.setId(null);
                    stops.setCrtDttm(new Date());
                    stops.setCrtUser(username);
                    stops.setLastUpdateDttm(new Date());
                    stops.setLastUpdateUser(username);
                    // link Flow
                    stops.setFlow(flowNew);
                    // get Properties
                    List<Property> properties = stops.getProperties();
                    // Determine if Property array is empty
                    if (CollectionUtils.isNotEmpty(properties)) {
                        // loop
                        for (Property property : properties) {
                            //Determine if it is empty
                            if (null != property) {
                                //set Property basic information
                                property.setId(null);
                                property.setCrtDttm(new Date());
                                property.setCrtUser(username);
                                property.setLastUpdateDttm(new Date());
                                property.setLastUpdateUser(username);
                                //link Stops
                                property.setStops(stops);
                            }
                        }
                        // set Properties
                        stops.setProperties(properties);
                    }
                }
            }
            // set StopsList
            flowNew.setStopsList(stopsList);
        }
        List<Paths> pathsList = flowNew.getPathsList();
        if (CollectionUtils.isNotEmpty(pathsList)) {
            for (Paths paths : pathsList) {
                //set Paths basic information
                paths.setId(null);
                paths.setCrtDttm(new Date());
                paths.setCrtUser(username);
                paths.setLastUpdateDttm(new Date());
                paths.setLastUpdateUser(username);
                //link Stops
                paths.setFlow(flow);
            }
            // set PathsList
            flowNew.setPathsList(pathsList);
        }
        return flowNew;
    }
}
