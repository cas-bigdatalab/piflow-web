package com.nature.component.workFlow.utils;

import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.Utils;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Paths;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;

import java.util.*;

public class MxGraphModelUtil {
    /**
     * mxGraphModel实体转Vo
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
     * mxCellList实体转Vo
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
     * mxCell实体转Vo
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
     * 区分stop和path
     *
     * @param root
     * @return 返回map中有stops和paths的Mxcell型的List(键为 ： paths和stops)
     */
    public static Map<String, Object> distinguishStopsPaths(List<MxCellVo> root) {
        Map<String, Object> map = null;
        if (null != root && root.size() > 0) {
            map = new HashMap<String, Object>();
            List<MxCellVo> pathsList = new ArrayList<MxCellVo>();
            List<MxCellVo> stopsList = new ArrayList<MxCellVo>();
            // 循环root
            for (MxCellVo mxCellVo : root) {
                if (null != mxCellVo) {
                    // 取出style属性
                    String style = mxCellVo.getStyle();
                    // 判空
                    if (StringUtils.isNotBlank(style)) {
                        // 取出线特有的属性判断是否为空
                        String edge = mxCellVo.getEdge();
                        if (StringUtils.isNotBlank(edge)) {
                            pathsList.add(mxCellVo);
                        } else {
                            stopsList.add(mxCellVo);
                        }
                    }
                }
            }
            map.put("stops", stopsList);
            map.put("paths", pathsList);
        }
        return map;
    }
    /**
     * 区分stop和path
     *
     * @param root
     * @return 返回map中有stops和paths的MxCell型的List(键为 ： paths和stops)
     */
    public static Map<String, Object> mxCellDistinguishStopsPaths(List<MxCell> root) {
        Map<String, Object> map = null;
        if (null != root && root.size() > 0) {
            map = new HashMap<String, Object>();
            List<MxCell> pathsList = new ArrayList<MxCell>();
            List<MxCell> stopsList = new ArrayList<MxCell>();
            // 循环root
            for (MxCell mxCell : root) {
                if (null != mxCell) {
                    // 取出线特有的属性判断是否为空
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
     * 根据MxCellList中的内容生成paths的list
     *
     * @param objectPaths
     * @param flow
     * @return
     */
    public static List<Paths> mxCellVoListToPathsList(List<MxCellVo> objectPaths, Flow flow) {
        List<Paths> pathsList = null;
        if (null != objectPaths && objectPaths.size() > 0) {
            pathsList = new ArrayList<Paths>();
            // 循环objectPaths
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
     * mxCellVo转Paths
     *
     * @param mxCellVo
     * @return
     */
    public static Paths mxCellToPaths(MxCellVo mxCellVo) {
        User user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        Paths paths = null;
        if (null != mxCellVo) {
            paths = new Paths();
            paths.setId(Utils.getUUID32());
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
}
