package com.nature.component.flow.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nature.base.util.*;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.PortType;
import com.nature.component.flow.model.*;
import com.nature.component.flow.service.IFlowService;
import com.nature.component.flow.utils.FlowInfoDbUtil;
import com.nature.component.flow.utils.MxGraphModelUtil;
import com.nature.component.flow.utils.PathsUtil;
import com.nature.component.flow.utils.StopsUtil;
import com.nature.component.flow.vo.FlowInfoDbVo;
import com.nature.component.flow.vo.FlowVo;
import com.nature.component.flow.vo.PathsVo;
import com.nature.component.flow.vo.StopsVo;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.mapper.*;
import com.nature.mapper.mxGraph.MxCellMapper;
import com.nature.mapper.mxGraph.MxGeometryMapper;
import com.nature.mapper.mxGraph.MxGraphModelMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional
public class FlowServiceImpl implements IFlowService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private FlowMapper flowMapper;

    @Resource
    private StopsTemplateMapper stopsTemplateMapper;

    @Resource
    private MxGraphModelMapper mxGraphModelMapper;

    @Resource
    private MxCellMapper mxCellMapper;

    @Resource
    private MxGeometryMapper mxGeometryMapper;

    @Resource
    private PathsMapper pathsMapper;

    @Resource
    private StopsMapper stopsMapper;

    @Resource
    private PropertyMapper propertyMapper;

    @Resource
    private FlowInfoDbMapper flowInfoDbMapper;

    /**
     * 向数据库添加flow
     *
     * @param mxGraphModelVo
     * @param flowId
     * @param operType
     * @return
     */
    @Override
    @Transient
    public StatefulRtnBase saveOrUpdateFlowAll(MxGraphModelVo mxGraphModelVo, String flowId, String operType, boolean flag) {
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        // 参数判空
        if (!StringUtils.isAnyEmpty(flowId, operType)) {
            // mxGraphModelVo参数判空
            if (null != mxGraphModelVo) {
                if ("ADD".equals(operType)) {
                    logger.info("ADD操作开始");
                    statefulRtnBase = this.addFlowStops(mxGraphModelVo, flowId, flag);
                } else if ("MOVED".equals(operType)) {
                    logger.info("MOVED操作开始");
                    statefulRtnBase = this.updateMxGraph(mxGraphModelVo, flowId);
                } else if ("REMOVED".equals(operType)) {
                    logger.info("REMOVED操作开始");
                    statefulRtnBase = this.updateFlow(mxGraphModelVo, flowId);
                } else {
                    statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("没有查询operType:" + operType + "类型");
                    logger.warn("没有查询operType:" + operType + "类型");
                }
            } else {
                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("传入参数mxGraphModelVo为空，保存失败");
                logger.warn("传入参数mxGraphModelVo为空，保存失败");
            }
        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("传入参数flowId或operType为空，保存失败");
            logger.warn("传入参数flowId或operType为空，保存失败");
        }

        return statefulRtnBase;
    }

    /**
     * 根据id查询流信息
     *
     * @param id
     * @return
     * @author Nature
     */
    @Override
    @Transient
    public Flow getFlowById(String id) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        return flowMapper.getFlowById(currentUser, id);
    }

    /**
     * 根据id查询流信息
     *
     * @param id
     * @return
     * @author Nature
     */
    @Override
    @Transient
    public FlowVo getFlowVoById(String id) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        FlowVo flowVo = null;
        Flow flowById = flowMapper.getFlowById(currentUser, id);
        if (null != flowById) {
            flowVo = new FlowVo();
            BeanUtils.copyProperties(flowById, flowVo);
            //取出mxGraphModel，并转为Vo
            MxGraphModelVo mxGraphModelVo = MxGraphModelUtil.mxGraphModelPoToVo(flowById.getMxGraphModel());
            //取出stopsList，并转为Vo
            List<StopsVo> stopsVoList = StopsUtil.stopsListPoToVo(flowById.getStopsList());
            //取出pathsList，并转为Vo
            List<PathsVo> pathsVoList = PathsUtil.pathsListPoToVo(flowById.getPathsList());
            //取出flowInfoDb，并转为Vo
            FlowInfoDbVo flowInfoDbVo = FlowInfoDbUtil.flowInfoDbToVo(flowById.getAppId());
            flowVo.setMxGraphModelVo(mxGraphModelVo);
            flowVo.setStopsVoList(stopsVoList);
            flowVo.setPathsVoList(pathsVoList);
        }
        return flowVo;
    }

    /**
     * 保存appId
     *
     * @param flowId
     * @param appId
     * @return
     */
    @SuppressWarnings("unused")
    @Override
    @Transient
    public StatefulRtnBase saveAppId(String flowId, FlowInfoDb appId) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        StatefulRtnBase satefulRtnBase = new StatefulRtnBase();
        if (StringUtils.isNotBlank(flowId)) {
            // 根据flowId查询flow
            Flow flowById = flowMapper.getFlowById(user, flowId);
            FlowInfoDb oldAppId = flowById.getAppId();
            if (null != flowById) {
                flowById.setAppId(appId);
                flowById.setLastUpdateDttm(new Date());
                flowById.setLastUpdateUser(username);
                int updateFlow = flowMapper.updateFlow(flowById);
                if (updateFlow <= 0) {
                    satefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("AppId保存失败");
                } else {
                    if (null != oldAppId) {
                        //把之前的appId置为无效
                        //  flowInfoDbMapper.deleteFlowInfoById(oldAppId.getId());
                    }
                }
            } else {
                satefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("未查询到flowId为" + flowId + "的flow，保存失败");
            }
        } else {
            satefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("flowId为空，保存失败");
        }
        return satefulRtnBase;
    }

    @Override
    @Transient
    public int addFlow(Flow flow) {
        int isSuccess = 0;
        if (null != flow) {
            int addFlow = flowMapper.addFlow(flow);
            if (addFlow > 0) {
                MxGraphModel mxGraphModel = flow.getMxGraphModel();
                if (null != mxGraphModel) {
                    mxGraphModel.setFlow(flow);
                    int addMxGraphModel = mxGraphModelMapper.addMxGraphModel(mxGraphModel);
                    if (addMxGraphModel > 0) {
                        flow.setMxGraphModel(mxGraphModel);
                        isSuccess = 1;
                    }
                }
            }
        }

        return isSuccess;
    }

    @Override
    public int updateFlow(Flow flow) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        Flow flowDb = flowMapper.getFlowById(currentUser, flow.getId());
        flow.setVersion(flowDb.getVersion());
        return flowMapper.updateFlow(flow);
    }

    @Override
    public int deleteFLowInfo(String id) {
        return flowMapper.updateEnableFlagById(id);
    }

    /**
     * add stops和画板mxCell
     *
     * @param mxGraphModelVo
     * @param flowId
     * @param flag           是否添加stop信息
     * @return
     */
    private StatefulRtnBase addFlowStops(MxGraphModelVo mxGraphModelVo, String flowId, boolean flag) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        // 根据flowId查询flow
        Flow flow = flowMapper.getFlowById(user, flowId);
        if (null != flow) {
            // 判断mxGraphModelVo和flow是否为空
            if (null != mxGraphModelVo && null != flow) {
                // 更新flow
                flow.setLastUpdateDttm(new Date()); // 最后更新时间
                flow.setLastUpdateUser(username);// 最后更新人
                flow.setEnableFlag(true);// 是否有效
                // 更新flow信息
                int updateFlowNum = flowMapper.updateFlow(flow);
                if (updateFlowNum > 0) {
                    // 取出数据库存的画板
                    MxGraphModel mxGraphModelDb = flow.getMxGraphModel();
                    // 判断数据库存的画板是否存在
                    if (null != mxGraphModelDb) {
                        // 把页面的画板信息更到数据库画板中
                        // 将mxGraphModelVo中的值copy到mxGraphModelDb中
                        BeanUtils.copyProperties(mxGraphModelVo, mxGraphModelDb);
                        mxGraphModelDb.setEnableFlag(true);
                        mxGraphModelDb.setLastUpdateUser(username);
                        mxGraphModelDb.setLastUpdateDttm(new Date());
                        // 画板的flow外键无变化，无需更新
                        // mxGraphModelDb.setFlow(flow); 添加外键
                        // 更新mxGraphModel
                        int updateMxGraphModel = mxGraphModelMapper.updateMxGraphModel(mxGraphModelDb);
                        // 判断mxGraphModelDb是否更新成功
                        if (updateMxGraphModel > 0) {
                            // 页面传过来的数据mxCellVoList
                            List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
                            // 存放页面传过来的数据的Map
                            Map<String, MxCellVo> mxCellVoMap = new HashMap<String, MxCellVo>();
                            // 页面传过来的mxCellList转map，key为pageId
                            if (null != mxCellVoList && mxCellVoList.size() > 0) {
                                // 页面传过来的mxCellList转map，key为pageId
                                for (MxCellVo mxCellVo : mxCellVoList) {
                                    if (null != mxCellVo && StringUtils.isNotBlank(mxCellVo.getPageId())) {
                                        mxCellVoMap.put(mxCellVo.getPageId(), mxCellVo);
                                    }
                                }
                            }
                            // 循环数据库数据
                            List<MxCell> mxCellDbRoot = mxGraphModelDb.getRoot();
                            for (MxCell mxCell : mxCellDbRoot) {
                                if (null != mxCell) {
                                    // 用pageId去map去取
                                    MxCellVo mxCellVo = mxCellVoMap.get(mxCell.getPageId());
                                    // 取到了说明数据库中已存在不需要新增，把取到的值在map中remove掉
                                    if (null != mxCellVo) {
                                        mxCellVoMap.remove(mxCell.getPageId());
                                    }
                                }
                            }
                            // 判断remove后的map是否还有数据，如果有进行新增处理
                            if (mxCellVoMap.size() > 0) {
                                // 把MxCellVo的map转为MxCellVoList
                                List<MxCellVo> addMxCellVoList = new ArrayList<MxCellVo>(mxCellVoMap.values());
                                if (null != addMxCellVoList && addMxCellVoList.size() > 0) {
                                    for (MxCellVo mxCellVo : addMxCellVoList) {
                                        if (null != mxCellVo) {
                                            String stopByNameAndFlowId = stopsMapper.getStopByNameAndFlowId(flow.getId(), mxCellVo.getValue());
                                            // 保存MxCell
                                            // 新建
                                            MxCell mxCell = new MxCell();
                                            // 将mxCellVo中的值copy到mxCell中
                                            BeanUtils.copyProperties(mxCellVo, mxCell);
                                            if (StringUtils.isNotBlank(stopByNameAndFlowId)) {
                                                mxCell.setValue(mxCellVo.getValue() + mxCellVo.getPageId());
                                            }
                                            // mxCell 的基本属性(创建时必填)
                                            mxCell.setId(SqlUtils.getUUID32());
                                            mxCell.setCrtDttm(new Date());
                                            mxCell.setCrtUser(username);
                                            // mxCell 的基本属性
                                            mxCell.setEnableFlag(true);
                                            mxCell.setLastUpdateUser(username);
                                            mxCell.setLastUpdateDttm(new Date());
                                            // mxGraphModel外键
                                            mxCell.setMxGraphModel(mxGraphModelDb);
                                            // 保存mxCell
                                            mxCellMapper.addMxCell(mxCell);
                                            MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
                                            if (null != mxGeometryVo) {
                                                // 保存MxGeometry
                                                // 新建
                                                MxGeometry mxGeometry = new MxGeometry();
                                                // 将mxGeometryVo中的值copy到mxGeometry中
                                                BeanUtils.copyProperties(mxGeometryVo, mxGeometry);
                                                // mxGeometry 的基本属性(创建时必填)
                                                mxGeometry.setId(SqlUtils.getUUID32());
                                                mxGeometry.setCrtDttm(new Date());
                                                mxGeometry.setCrtUser(username);
                                                // setmxGraphModel基本属性
                                                mxGeometry.setEnableFlag(true);
                                                mxGeometry.setLastUpdateUser(username);
                                                mxGeometry.setLastUpdateDttm(new Date());
                                                // mxCell外键
                                                mxGeometry.setMxCell(mxCell);
                                                // 保存mxGeometry
                                                mxGeometryMapper.addMxGeometry(mxGeometry);
                                            }

                                        }
                                    }


                                    // 把需添加addMxCellVoList中的stops和线分开
                                    Map<String, Object> stopsPathsMap = MxGraphModelUtil.distinguishStopsPaths(addMxCellVoList);

                                    // 从Map中取出mxCellVoList(stops的list)
                                    List<MxCellVo> objectStops = (ArrayList<MxCellVo>) stopsPathsMap.get("stops");

                                    // stops的list
                                    List<Stops> addStopsList = new ArrayList<Stops>();
                                    // 根据MxCellList中的内容生成stops的list
                                    addStopsList = this.mxCellVoListToStopsList(objectStops, flow);
                                    // 保存addStopsList
                                    if (flag) {
                                        this.addStopList(addStopsList);
                                    }

                                    // 从Map中取出mxCellVoList(线的list)
                                    List<MxCellVo> objectPaths = (ArrayList<MxCellVo>) stopsPathsMap.get("paths");

                                    // 线的List
                                    List<Paths> addPathsList = new ArrayList<Paths>();

                                    // 根据MxCellList中的内容生成paths的list
                                    addPathsList = MxGraphModelUtil.mxCellVoListToPathsList(objectPaths, flow);
                                    if (flag) {
                                        // 判空pathsList
                                        if (null != addPathsList && addPathsList.size() > 0) {
                                            // 保存 addPathsList
                                            int addPathsListNum = pathsMapper.addPathsList(addPathsList);
                                            if (addPathsListNum <= 0) {
                                                logger.error("addPathsList保存失败所属流：flowId(" + flow.getId() + ")", new Exception("addPathsList保存失败"));
                                            }
                                        } else {
                                            logger.info("addPathsList为空,不保存");
                                        }
                                    }
                                } else {
                                    statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("无可添加数据,添加失败");
                                }
                            } else {
                                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("无可添加数据,添加失败");
                            }
                        } else {
                            StatefulRtnBaseUtils.setFailedMsg("画板mxGraphModel更新失败,更新失败");
                            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("画板mxGraphModel更新失败,更新失败");
                            logger.warn("flow更新失败，添加失败");
                        }
                    } else {
                        statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("数据库无画板，添加失败");
                        logger.warn("数据库无画板，添加失败");
                    }
                } else {
                    StatefulRtnBaseUtils.setFailedMsg("flow更新失败,更新失败");
                    logger.warn("flow更新失败,更新失败");
                }
            } else {
                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("传入参数mxGraphModelVo为空或flow不存在，添加失败");
                logger.warn("传入参数mxGraphModelVo为空或flow不存在，ADD失败");
            }
        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("没有查询到flowId为:" + flowId + "的flow信息");
            logger.warn("没有查询到flowId为:" + flowId + "的flow信息");
        }
        return statefulRtnBase;

    }

    /**
     * 对画板的修改
     *
     * @param mxGraphModelVo
     * @param flowId
     * @return
     */
    private StatefulRtnBase updateMxGraph(MxGraphModelVo mxGraphModelVo, String flowId) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        MxGraphModel mxGraphModel = mxGraphModelMapper.getMxGraphModelByFlowId(flowId);
        if (null != mxGraphModel) {

        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("没有查询到flowId为:" + flowId + "的flow信息");
            logger.warn("没有查询到flowId为:" + flowId + "的flow信息");
        }
        // 判断传入的数据是否为空
        if (null != mxGraphModelVo) {
            // 判断要修改的数据库数据是否为空
            if (null != mxGraphModel) {
                // 将mxGraphModelVo中的值copy到mxGraphModelDb中
                BeanUtils.copyProperties(mxGraphModelVo, mxGraphModel);
                // setmxGraphModel基本属性
                mxGraphModel.setLastUpdateUser(username);// 最后更新人
                mxGraphModel.setLastUpdateDttm(new Date());// 最后更新时间
                mxGraphModel.setEnableFlag(true);// 是否有效
                // 保存MxGraphModel
                int updateMxGraphModel = mxGraphModelMapper.updateMxGraphModel(mxGraphModel);
                // 判断mxGraphModel是否保存成功
                if (updateMxGraphModel > 0) {
                    // 页面传过来的数据MxCellVo
                    List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
                    // 取出数据库查询出来的MxCellList信息
                    List<MxCell> mxCellList = mxGraphModel.getRoot();
                    // 保存并处理mxCellList
                    statefulRtnBase = this.updateMxCellList(mxCellVoList, mxCellList);
                }
            } else {
                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("没有查询到flowId为:" + flowId + "的mxGraphModel信息为空，修改失败");
            }
        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("mxGraphModelVo为空，修改失败");
        }
        return statefulRtnBase;
    }

    /**
     * 修改Flow
     *
     * @param mxGraphModelVo 页面传出的信息
     * @param flowId         要修改的数据
     * @return
     */
    private StatefulRtnBase updateFlow(MxGraphModelVo mxGraphModelVo, String flowId) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        Flow flow = flowMapper.getFlowById(user, flowId);
        if (null != flow) {
            if (null != mxGraphModelVo) {
                // 最后更新时间
                flow.setLastUpdateDttm(new Date());
                // 最后更新人
                flow.setLastUpdateUser(username);

                // 保存flow
                int updateFlow = flowMapper.updateFlow(flow);
                // 判断是否保存成功
                if (updateFlow > 0) {
                    // 取出画板信息
                    MxGraphModel mxGraphModel = flow.getMxGraphModel();
                    // 保存修改画板信息
                    StatefulRtnBase updateMxGraphRtn = this.updateMxGraph(mxGraphModelVo, flowId);
                    // 判断mxGraphModel是否保存成功
                    if (null != updateMxGraphRtn && updateMxGraphRtn.isReqRtnStatus()) {
                        // 页面的MxCellVo的list
                        List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();

                        // 把mxCellVoList中的stops和线分开
                        Map<String, Object> stopsPathsMap = MxGraphModelUtil.distinguishStopsPaths(mxCellVoList);

                        if (null != stopsPathsMap) {
                            // 逻辑删除的线
                            // key为from和to(就是stop的pageId) value为inport和outport
                            Map<String, String> pathsDelInfoMap = new HashMap<String, String>();

                            // get出数据库中存的PathsList
                            List<Paths> pathsList = flow.getPathsList();
                            // 判断数据库中的线的list是否为空，不为空继续下边的判断操作，否则直接添加
                            if (null != pathsList && pathsList.size() > 0) {
                                // 取线的mxCellVoList
                                List<MxCellVo> objectPaths = (ArrayList<MxCellVo>) stopsPathsMap.get("paths");
                                // key为PathsVo的PageId(画板中所属id),value为Paths
                                Map<String, MxCellVo> objectPathsMap = new HashMap<String, MxCellVo>();

                                // 需修改的pathsList
                                List<Paths> updatePaths = new ArrayList<Paths>();

                                //判断页面传过来的objectStops是否为空
                                if (null != objectPaths) {
                                    for (MxCellVo mxCellVo : objectPaths) {
                                        if (null != mxCellVo) {
                                            objectPathsMap.put(mxCellVo.getPageId(), mxCellVo);
                                        }
                                    }
                                }

                                // 循环数据库的数据pathsList,用stops中的pageId去转换为map后的页面传的值的map中去取，
                                for (Paths paths : pathsList) {
                                    if (null != paths) {
                                        String pageId = paths.getPageId();
                                        MxCellVo mxCellVo = objectPathsMap.get(pageId);
                                        // 取到了则是需要修改的，否则是要逻辑删除的
                                        if (null != mxCellVo) {
                                            //操作画板时暂无修改stop信息的操作，可以修改属性，但立刻保存了，这里无需操作
                                        } else {
                                            paths.setEnableFlag(false);
                                            paths.setLastUpdateUser(username);
                                            paths.setLastUpdateDttm(new Date());
                                            paths.setFlow(flow);
                                            updatePaths.add(paths);
                                            // 把需要逻辑删除的端口信息放入pathsDelInfoMap的map中
                                            pathsDelInfoMap.put("in" + paths.getTo(), paths.getInport());
                                            pathsDelInfoMap.put("out" + paths.getFrom(), paths.getOutport());
                                        }
                                    }
                                }
                                // 修改保存updateStops
                                for (Paths paths : updatePaths) {
                                    if (null != paths) {
                                        pathsMapper.updatePaths(paths);
                                    }
                                }
                            } else {
                                //数据库中的stopsList为空，修改失败
                                logger.info("数据库中的pathsList为空");
                            }


                            // get出数据库中存的Stopslist
                            List<Stops> stopsList = flow.getStopsList();

                            // 如果数据库中的Stops的list是否为空，则修改失败，因为此方法只处理修改的，不负责新增
                            if (null != stopsList && stopsList.size() > 0) {
                                // 取stops的mxCellVoList
                                List<MxCellVo> objectStops = (ArrayList<MxCellVo>) stopsPathsMap.get("stops");
                                // key为mxCellVo的PageId(画板中所属id),value为mxCellVo
                                Map<String, MxCellVo> objectStopsMap = new HashMap<String, MxCellVo>();

                                // 需修改的stopsList
                                List<Stops> updateStops = new ArrayList<Stops>();

                                // 判断页面传过来的objectStops是否为空
                                if (null != objectStops) {
                                    // 循环 把objectStops(页面数据)转为objectStopsMap
                                    for (MxCellVo mxCellVo : objectStops) {
                                        if (null != mxCellVo) {
                                            objectStopsMap.put(mxCellVo.getPageId(), mxCellVo);
                                        }
                                    }
                                }
                                // 循环数据库的数据stopsList,用stops中的pageId去转换为map后的页面传的值的map中去取，
                                for (Stops stops : stopsList) {
                                    if (null != stops) {
                                        String pageId = stops.getPageId();
                                        MxCellVo mxCellVo = objectStopsMap.get(pageId);
                                        // 取到了则是需要修改的，否则是要逻辑删除的
                                        if (null != mxCellVo) {
                                            //操作画板时,当删除线时，需要判断线的两端是否有Any类型的stop，如果有要把属性中对应的端口信息删除
                                            // 判断stops是否有Any端口
                                            if (stops.getInPortType() == PortType.ANY || stops.getOutPortType() == PortType.ANY) {
                                                // 根据pageId在pathsDelInfoMap中取值
                                                String inprot = pathsDelInfoMap.get("in" + stops.getPageId());
                                                String outprot = pathsDelInfoMap.get("out" + stops.getPageId());
                                                // 如果inprot或者outprot有值，则循环属性找到对应存放端口的属性，进行修改
                                                if (StringUtils.isNotBlank(inprot) || StringUtils.isNotBlank(outprot)) {
                                                    // 取出属性list
                                                    List<Property> properties = stops.getProperties();
                                                    // 判空
                                                    if (null != properties && properties.size() > 0) {
                                                        Property inportProperty = null;
                                                        Property outportProperty = null;
                                                        List<Property> propertiesNew = new ArrayList<Property>();
                                                        // 循环属性
                                                        for (Property property : properties) {
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
                                                            inportProperty = replaceProtValue(inprot, inportProperty);
                                                            propertiesNew.add(inportProperty);
                                                            isUpdate = true;
                                                        }
                                                        if (null != outportProperty && StringUtils.isNotBlank(outprot)) {
                                                            outportProperty = replaceProtValue(outprot, outportProperty);
                                                            propertiesNew.add(outportProperty);
                                                            isUpdate = true;
                                                        }
                                                        stops.setProperties(propertiesNew);
                                                        if (isUpdate) {
                                                            stops.setLastUpdateDttm(new Date());//最后跟新时间
                                                            stops.setLastUpdateUser(username);//最后更新人
                                                            stops.setProperties(properties);//属性list
                                                            stops.setFlow(flow);
                                                            updateStops.add(stops);
                                                        }
                                                    }
                                                }
                                            }

                                        } else {
                                            stops.setEnableFlag(false);//逻辑删除标识
                                            stops.setLastUpdateDttm(new Date());//最后跟新时间
                                            stops.setLastUpdateUser(username);//最后更新人
                                            //stops的属性
                                            List<Property> properties = stops.getProperties();
                                            // 判空
                                            if (null != properties) {
                                                List<Property> propertyList = new ArrayList<Property>();
                                                // 循环进行逻辑删除属性
                                                for (Property property : properties) {
                                                    if (null != property) {
                                                        property.setEnableFlag(false);//逻辑删除标识
                                                        property.setLastUpdateDttm(new Date());//最后跟新时间
                                                        property.setLastUpdateUser(username);//最后更新人
                                                        propertyList.add(property);
                                                    }
                                                }
                                                stops.setProperties(propertyList);
                                            }
                                            updateStops.add(stops);
                                        }
                                    }
                                }
                                // 修改保存updateStops
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
                                // 数据库中的stops数据为空
                                logger.info("数据库中的stops数据为空");
                            }
                        }
                    } else {
                        statefulRtnBase = updateMxGraphRtn;
                    }
                } else {
                    statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("修改保存失败flow");
                }
            } else {
                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("mxGraphModelVo为空，修改失败");
            }
        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("flowId查不到对应的flow，修改失败");
        }
        return statefulRtnBase;
    }

    /**
     * 替换端口属性
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
     * 根据MxCellVoList中的内容生成stops的list
     *
     * @param objectStops
     * @param flow
     * @return
     */
    private List<Stops> mxCellVoListToStopsList(List<MxCellVo> objectStops, Flow flow) {
        List<Stops> stopsList = null;
        if (null != objectStops && objectStops.size() > 0) {
            stopsList = new ArrayList<Stops>();
            // 循环objectStops
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
     * mxCellVo转stops
     *
     * @param mxCellVo
     * @return
     */
    private Stops stopsTemplateToStops(MxCellVo mxCellVo) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        Stops stops = null;
        if (null != mxCellVo) {
            // 取出style属性(属性中有stops的name)
            // stops的style属性例子
            // （image;html=1;labelBackgroundColor=#ffffff;image=/grapheditor/stencils/clipart/test_stops_1_128x128.png）
            // 我们需要的是"test_stops_1"
            String style = mxCellVo.getStyle();
            if (StringUtils.isNotBlank(style)) {
                // 通过截取关键字来判断是否为stops
                String[] split = style.split("_128x128.");
                // 当有且仅有一个关键字("_128x128.")时则认为是stops
                if (null != split && split.length == 2) {
                    // 取数组第一位继续截取
                    String string = split[0];
                    String[] split2 = string.split("/");
                    // 判空，取数组最后一位（stops的name）
                    if (null != split2 && split2.length > 0) {
                        // 取到stops的名字
                        String stopsName = split2[split2.length - 1];
                        // 根据stops的Name查询stops模板
                        StopsTemplate stopsTemplate = this.getStopsTemplate(stopsName);
                        // 模板判空
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
                                    //表明是select
                                    if (propertyTemplate.getAllowableValues().contains(",") && propertyTemplate.getAllowableValues().length() > 4) {
                                        property.setIsSelect(true);
                                        //判断select中是否存在默认值
                                        if (!propertyTemplate.getAllowableValues().contains(propertyTemplate.getDefaultValue())) {
                                            //如果不存在则情况默认值
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
     * 根据stopsName查询stops
     *
     * @param stopsName
     * @return
     */
    private StopsTemplate getStopsTemplate(String stopsName) {
        StopsTemplate stopsTemplate = null;
        // 根据stops的Name查询stops模板
        List<StopsTemplate> stopsTemplateList = stopsTemplateMapper.getStopsTemplateByName(stopsName);
        if (null != stopsTemplateList && stopsTemplateList.size() > 0) {
            stopsTemplate = stopsTemplateList.get(0);
        }
        return stopsTemplate;
    }

    /**
     * 保存并处理mxCellList
     *
     * @param mxCellVoList
     * @param mxCellList
     * @return
     */
    private StatefulRtnBase updateMxCellList(List<MxCellVo> mxCellVoList, List<MxCell> mxCellList) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();

        // 如果mxCellList为空，则修改失败，因为此方法只处理修改的，不负责新增
        if (null != mxCellList && mxCellList.size() > 0) {

            // 包括修改的和逻辑删除的
            List<MxCell> updateMxCellList = new ArrayList<MxCell>();
            // 将页面传过来的List转为map 键为pageId
            Map<String, MxCellVo> mxCellVoMap = new HashMap<String, MxCellVo>();
            // 判空
            if (null != mxCellVoList) {
                for (MxCellVo mxCell : mxCellVoList) {
                    mxCellVoMap.put(mxCell.getPageId(), mxCell);
                }
            }
            // 循环页面数据 进行分类(需要修改和逻辑删除的)
            for (MxCell mxCell : mxCellList) {
                if (null != mxCell) {
                    // 画板上的图形ID(pageId)
                    String pageId = mxCell.getPageId();
                    // 根据pageId去map取，
                    // 取到说明数据库中有页面也有，做修改操作，
                    // 否则说明数据库有页面没有，做逻辑删除
                    MxCellVo mxCellVo = mxCellVoMap.get(pageId);
                    if (null != mxCellVo) {
                        // 将mxCellVo中的值copy到mxCell中
                        BeanUtils.copyProperties(mxCellVo, mxCell);
                        // mxCell 的基本属性
                        mxCell.setEnableFlag(true);// 是否有效
                        mxCell.setLastUpdateUser(username);// 最后更新人
                        mxCell.setLastUpdateDttm(new Date());// 最后更新时间

                        // 修改时不用处理外键，除非取消或修改外键
                        // mxGraphModel外键
                        // mxCell.setMxGraphModel(mxGraphModel);
                        MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
                        MxGeometry mxGeometry = mxCell.getMxGeometry();
                        if (null != mxGeometry) {
                            if (null != mxGeometryVo) {
                                // 将 mxGeometryVo 中的值copy到 mxGeometry 中
                                BeanUtils.copyProperties(mxGeometryVo, mxGeometry);

                                // setmxGraphModel基本属性
                                mxGeometry.setLastUpdateUser(username);// 最后更新人
                                mxGeometry.setLastUpdateDttm(new Date());// 最后更新时间
                                mxGeometry.setEnableFlag(true);// 是否有效
                                mxGeometry.setMxCell(mxCell);
                            }
                        }
                    } else {
                        //逻辑删
                        mxCell.setEnableFlag(false);
                        mxCell.setLastUpdateDttm(new Date());
                        mxCell.setLastUpdateUser(username);
                    }
                    // 填入修改list
                    updateMxCellList.add(mxCell);
                }
            }
            if (null != updateMxCellList && updateMxCellList.size() > 0) {
                for (MxCell mxCell : updateMxCellList) {
                    if (null != mxCell) {
                        // 保存mxCell
                        int updateMxCell = mxCellMapper.updateMxCell(mxCell);
                        if (updateMxCell > 0) {
                            MxGeometry mxGeometry = mxCell.getMxGeometry();
                            if (null != mxGeometry) {
                                // 取修改保存mxGeometry
                                int updateMxGeometry = mxGeometryMapper.updateMxGeometry(mxGeometry);
                                if (updateMxGeometry > 0) {
                                    logger.info("mxGeometry保存成功ID:" + mxGeometry.getId());
                                } else {
                                    logger.error("mxGeometryVo保存失败：对应的mxCell的pageId(" + mxCell.getPageId() + "),mxCellValue(" + mxCell.getValue() + ")", new Exception("mxGeometryVo保存失败"));
                                }
                            }
                        } else {
                            logger.error("mxCellVo保存失败：pageId(" + mxCell.getPageId() + "),name(" + mxCell.getValue() + ")", new Exception("mxCellVo保存失败"));
                        }
                    }
                }
            }
        } else {
            statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("数据库mxCellList为空，修改失败");
        }
        return statefulRtnBase;
    }

    /**
     * 保存addStopsList到数据库
     *
     * @param addStopsList
     */
    private void addStopList(List<Stops> addStopsList) {
        // stopsList判空
        if (null != addStopsList && addStopsList.size() > 0) {
            // 保存stopsList
            int addStopsListNum = stopsMapper.addStopsList(addStopsList);
            if (addStopsListNum > 0) {
                for (Stops stops : addStopsList) {
                    // 取出propertyList
                    List<Property> propertyList = stops.getProperties();
                    // 判空propertyList
                    if (null != propertyList && propertyList.size() > 0) {
                        // 保存propertyList
                        int addPropertyList = propertyMapper.addPropertyList(propertyList);
                        if (addPropertyList <= 0) {
                            logger.error("Property保存失败：stopsId(" + stops.getId() + "),stopsName(" + stops.getName() + ")", new Exception("Property保存失败"));
                        }
                    } else {
                        logger.info("addPropertyList为空,不保存");
                    }
                }
            } else {
                logger.error("新建保存失败addStopsList", new Exception("新建保存失败addStopsList"));
            }
        } else {
            logger.info("addStopsList为空,不保存");
        }
    }

    @Override
    public String getMaxStopPageId(String flowId) {
        return flowMapper.getMaxStopPageId(flowId);
    }

    @Override
    public List<FlowVo> getFlowList() {
        List<FlowVo> flowVoList = new ArrayList<>();
        List<Flow> flowList = flowMapper.getFlowList();
        if (null != flowList && flowList.size() > 0) {
            for (Flow flow : flowList) {
                if (null != flow) {
                    FlowVo flowVo = new FlowVo();
                    BeanUtils.copyProperties(flow, flowVo);
                    flowVo.setCrtDttm(flow.getCrtDttm());
                    flowVoList.add(flowVo);
                }
            }
        }
        return flowVoList;
    }

    @Override
    public String getFlowListPage(Integer offset, Integer limit, String param) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null != offset && null != limit) {
            Page page = PageHelper.startPage(offset, limit);
            flowMapper.getFlowListParma(currentUser, param);
            rtnMap = PageHelperUtils.setDataTableParam(page, rtnMap);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String getFlowExampleList() {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 0);
        List<FlowVo> flowVoList = new ArrayList<>();
        List<Flow> flowExampleList = flowMapper.getFlowExampleList();
        // list判空
        if (CollectionUtils.isNotEmpty(flowExampleList)) {
            flowExampleList.forEach(flow -> {
                FlowVo flowVo = new FlowVo();
                flowVo.setId(flow.getId());
                flowVo.setName(flow.getName());
                flowVoList.add(flowVo);
            });
            rtnMap.put("code", 1);
            rtnMap.put("flowExampleList", flowVoList);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }
}
