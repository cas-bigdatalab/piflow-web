package com.nature.component.mxGraph.utils;

import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowGroup;
import com.nature.component.flow.model.FlowGroupPaths;
import com.nature.component.flow.model.Paths;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;

public class MxGraphModelUtil {

    public static MxGraphModel setMxGraphModelBasicInformation(MxGraphModel mxGraphModel, boolean isSetId, String username) {
        if (null == mxGraphModel) {
            mxGraphModel = new MxGraphModel();
        }
        if (isSetId) {
            mxGraphModel.setId(SqlUtils.getUUID32());
        }
        //set MxGraphModel basic information
        mxGraphModel.setCrtDttm(new Date());
        mxGraphModel.setCrtUser(username);
        mxGraphModel.setVersion(0L);
        return updateMxGraphModelBasicInformation(mxGraphModel, username);
    }

    public static MxGraphModel updateMxGraphModelBasicInformation(MxGraphModel mxGraphModel, String username) {
        if (null == mxGraphModel) {
            return null;
        }
        mxGraphModel.setLastUpdateDttm(new Date());
        mxGraphModel.setLastUpdateUser(username);
        return mxGraphModel;
    }

    /**
     * mxGraphModel entity to Vo
     *
     * @param mxGraphModel
     * @return
     */
    public static MxGraphModelVo mxGraphModelPoToVo(MxGraphModel mxGraphModel) {
        MxGraphModelVo mxGraphModelVo = null;
        if (null != mxGraphModel) {
            mxGraphModelVo = new MxGraphModelVo();
            BeanUtils.copyProperties(mxGraphModel, mxGraphModelVo);
            List<MxCell> root = mxGraphModel.getRoot();
            mxGraphModelVo.setRootVo(mxCellVoListPoToVo(root));
        }
        return mxGraphModelVo;
    }

    /**
     * mxCellList entity to Vo
     *
     * @param mxCellList
     * @return
     */
    public static List<MxCellVo> mxCellVoListPoToVo(List<MxCell> mxCellList) {
        List<MxCellVo> mxCellVoList = null;
        if (null != mxCellList && mxCellList.size() > 0) {
            mxCellVoList = new ArrayList<MxCellVo>();
            for (MxCell mxCell : mxCellList) {
                MxCellVo mxCellVo = mxCellPoToVo(mxCell);
                if (null != mxCell) {
                    mxCellVoList.add(mxCellVo);
                }
            }
        }
        return mxCellVoList;
    }

    /**
     * mxCell entity to Vo
     *
     * @param mxCell
     * @return
     */
    public static MxCellVo mxCellPoToVo(MxCell mxCell) {
        MxCellVo mxCellVo = null;
        if (null != mxCell) {
            mxCellVo = new MxCellVo();
            BeanUtils.copyProperties(mxCell, mxCellVo);
            MxGeometry mxGeometry = mxCell.getMxGeometry();
            if (null != mxGeometry) {
                MxGeometryVo mxGeometryVo = new MxGeometryVo();
                BeanUtils.copyProperties(mxGeometry, mxGeometryVo);
                mxCellVo.setMxGeometryVo(mxGeometryVo);
            }
        }
        return mxCellVo;
    }

    /**
     * Distinguish between 'element' and 'path'
     *
     * @param root
     * @return Returns a list of Mxcell types with elements and paths in the map (keys: paths and elements)
     */
    public static Map<String, List<MxCellVo>> distinguishElementsPaths(List<MxCellVo> root) {
        Map<String, List<MxCellVo>> map = null;
        if (null != root && root.size() > 0) {
            map = new HashMap<>();
            List<MxCellVo> pathsList = new ArrayList<MxCellVo>();
            List<MxCellVo> elementsList = new ArrayList<MxCellVo>();
            // Loop root
            for (MxCellVo mxCellVo : root) {
                if (null != mxCellVo) {
                    // Take out the style attribute
                    String style = mxCellVo.getStyle();
                    // Judge whether it is empty
                    if (StringUtils.isNotBlank(style)) {
                        // Take out the line-specific attributes to determine if it is empty.
                        String edge = mxCellVo.getEdge();
                        if (StringUtils.isNotBlank(edge)) {
                            pathsList.add(mxCellVo);
                        } else {
                            elementsList.add(mxCellVo);
                        }
                    }
                }
            }
            map.put("elements", elementsList);
            map.put("paths", pathsList);
        }
        return map;
    }

    /**
     * Distinguish between stop and path
     *
     * @param root
     * @return Returns a list of MxCell types with stops and paths in the map (keys: paths and stops)
     */
    public static Map<String, Object> mxCellDistinguishStopsPaths(List<MxCell> root) {
        Map<String, Object> map = null;
        if (null != root && root.size() > 0) {
            map = new HashMap<String, Object>();
            List<MxCell> pathsList = new ArrayList<MxCell>();
            List<MxCell> stopsList = new ArrayList<MxCell>();
            // Loop root
            for (MxCell mxCell : root) {
                if (null != mxCell) {
                    // Take out the line-specific attributes to determine if it is empty.
                    String edge = mxCell.getEdge();
                    if (StringUtils.isNotBlank(edge)) {
                        pathsList.add(mxCell);
                    } else {
                        stopsList.add(mxCell);
                    }
                }
            }
            map.put("stops", stopsList);
            map.put("paths", pathsList);
        }
        return map;
    }

    /**
     * Generate a list of paths based on the contents of the MxCellList
     *
     * @param objectPaths
     * @param flow
     * @return
     */
    public static List<Paths> mxCellVoListToPathsList(List<MxCellVo> objectPaths, Flow flow) {
        List<Paths> pathsList = null;
        if (null != objectPaths && objectPaths.size() > 0) {
            pathsList = new ArrayList<Paths>();
            // Loop objectPaths
            for (MxCellVo mxCellVo : objectPaths) {
                Paths paths = mxCellToPaths(mxCellVo);
                if (null != paths) {
                    paths.setFlow(flow);
                    pathsList.add(paths);
                }
            }
        }
        return pathsList;
    }

    /**
     * Generate a list of paths based on the contents of the MxCellList
     *
     * @param objectPaths
     * @param flowGroup
     * @return
     */
    public static List<FlowGroupPaths> mxCellVoListToFlowGroupPathsList(List<MxCellVo> objectPaths, FlowGroup flowGroup) {
        List<FlowGroupPaths> flowGroupPathsList = null;
        if (null != objectPaths && objectPaths.size() > 0) {
            flowGroupPathsList = new ArrayList<>();
            // Loop objectPaths
            for (MxCellVo mxCellVo : objectPaths) {
                FlowGroupPaths flowGroupPaths = mxCellToFlowGroupPaths(mxCellVo);
                if (null != flowGroupPaths) {
                    flowGroupPaths.setFlowGroup(flowGroup);
                    flowGroupPathsList.add(flowGroupPaths);
                }
            }
        }
        return flowGroupPathsList;
    }

    /**
     * mxCellVo to Paths
     *
     * @param mxCellVo
     * @return
     */
    public static Paths mxCellToPaths(MxCellVo mxCellVo) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        Paths paths = null;
        if (null != mxCellVo) {
            paths = new Paths();
            paths.setId(SqlUtils.getUUID32());
            paths.setCrtDttm(new Date());
            paths.setCrtUser(username);
            paths.setLastUpdateDttm(new Date());
            paths.setLastUpdateUser(username);
            paths.setEnableFlag(true);
            paths.setFrom(mxCellVo.getSource());
            paths.setTo(mxCellVo.getTarget());
            paths.setPageId(mxCellVo.getPageId());
        }
        return paths;
    }

    /**
     * mxCellVo to Paths
     *
     * @param mxCellVo
     * @return
     */
    public static FlowGroupPaths mxCellToFlowGroupPaths(MxCellVo mxCellVo) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        FlowGroupPaths flowGroupPaths = null;
        if (null != mxCellVo) {
            flowGroupPaths = new FlowGroupPaths();
            flowGroupPaths.setCrtDttm(new Date());
            flowGroupPaths.setCrtUser(username);
            flowGroupPaths.setLastUpdateDttm(new Date());
            flowGroupPaths.setLastUpdateUser(username);
            flowGroupPaths.setEnableFlag(true);
            flowGroupPaths.setFrom(mxCellVo.getSource());
            flowGroupPaths.setTo(mxCellVo.getTarget());
            flowGroupPaths.setPageId(mxCellVo.getPageId());
        }
        return flowGroupPaths;
    }

    /**
     * Generate a list of flows based on the contents of MxCellVoList
     *
     * @param mxCellVoList
     * @param flowGroup
     * @param username
     * @return
     */
    public static Map<String, List> mxCellVoListToFlowAndFlowGroups(List<MxCellVo> mxCellVoList, FlowGroup flowGroup, String username) {
        Map<String, List> rtnMapData = new HashMap<>();
        List<Flow> flowList = new ArrayList<>();
        List<FlowGroup> flowGroupList = new ArrayList<>();
        if (null != mxCellVoList && mxCellVoList.size() > 0) {
            // Loop mxCellVoList
            for (MxCellVo mxCellVo : mxCellVoList) {
                if (null == mxCellVo) {
                    continue;
                }
                String mxCellVoStyle = mxCellVo.getStyle();
                String[] mxCellVoStyle_arrays = mxCellVoStyle.split("/");
                if (mxCellVoStyle_arrays.length <= 0) {
                    continue;
                }
                String imageName = mxCellVoStyle_arrays[mxCellVoStyle_arrays.length - 1];
                String typeName = imageName.split("_")[0];
                if ("flow".equals(typeName)) {
                    Flow flowNew = mxCellVoToFlow(mxCellVo, flowGroup, username);
                    if (null != flowNew) {
                        flowList.add(flowNew);
                    }
                } else if ("group".equals(typeName)) {
                    FlowGroup flowGroupNew = mxCellVoToGroup(mxCellVo, flowGroup, username);
                    if (null != flowGroupNew) {
                        flowGroupList.add(flowGroupNew);
                    }
                }
            }
        }
        rtnMapData.put("flows", flowList);
        rtnMapData.put("flowGroups", flowGroupList);
        return rtnMapData;
    }

    /**
     * Content generation Flow based on mxCellVo
     *
     * @param mxCellVo
     * @param parentsFlowGroup
     * @param username
     * @return
     */
    public static Flow mxCellVoToFlow(MxCellVo mxCellVo, FlowGroup parentsFlowGroup, String username) {
        Flow flow = null;
        if (null != mxCellVo) {
            flow = new Flow();
            flow.setCrtDttm(new Date());
            flow.setCrtUser(username);
            flow.setLastUpdateDttm(new Date());
            flow.setLastUpdateUser(username);
            flow.setEnableFlag(true);
            flow.setPageId(mxCellVo.getPageId());
            flow.setName("flow" + mxCellVo.getPageId());
            MxGraphModel mxGraphModel = new MxGraphModel();
            mxGraphModel.setFlow(flow);
            mxGraphModel.setCrtDttm(new Date());
            mxGraphModel.setCrtUser(username);
            mxGraphModel.setLastUpdateDttm(new Date());
            mxGraphModel.setLastUpdateUser(username);
            mxGraphModel.setEnableFlag(true);
            flow.setMxGraphModel(mxGraphModel);
            flow.setFlowGroup(parentsFlowGroup);
        }
        return flow;
    }

    /**
     * Content generation FlowGroup based on mxCellVo
     *
     * @param mxCellVo
     * @param parentsFlowGroup
     * @param username
     * @return
     */
    public static FlowGroup mxCellVoToGroup(MxCellVo mxCellVo, FlowGroup parentsFlowGroup, String username) {
        FlowGroup flowGroup = null;
        if (null != mxCellVo) {
            flowGroup = new FlowGroup();
            flowGroup.setCrtDttm(new Date());
            flowGroup.setCrtUser(username);
            flowGroup.setLastUpdateDttm(new Date());
            flowGroup.setLastUpdateUser(username);
            flowGroup.setEnableFlag(true);
            flowGroup.setPageId(mxCellVo.getPageId());
            flowGroup.setName("group" + mxCellVo.getPageId());
            MxGraphModel mxGraphModel = new MxGraphModel();
            mxGraphModel.setFlowGroup(flowGroup);
            mxGraphModel.setCrtDttm(new Date());
            mxGraphModel.setCrtUser(username);
            mxGraphModel.setLastUpdateDttm(new Date());
            mxGraphModel.setLastUpdateUser(username);
            mxGraphModel.setEnableFlag(true);
            flowGroup.setMxGraphModel(mxGraphModel);
            flowGroup.setFlowGroup(parentsFlowGroup);
        }
        return flowGroup;
    }

}
