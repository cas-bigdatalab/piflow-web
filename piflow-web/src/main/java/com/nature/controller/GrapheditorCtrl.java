package com.nature.controller;

import com.nature.base.config.vo.UserVo;
import com.nature.base.util.*;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.common.Eunm.PortType;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.service.*;
import com.nature.component.workFlow.vo.PathsVo;
import com.nature.component.workFlow.vo.StopGroupVo;
import com.nature.component.workFlow.vo.StopsPropertyVo;
import com.nature.component.workFlow.vo.StopsVo;
import com.nature.third.service.GetGroupsAndStops;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 画板的ctrl
 */
@Controller
@RequestMapping("/grapheditor")
public class GrapheditorCtrl {

    /**
     * 引入日志，注意都是"org.slf4j"包下
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IFlowService flowServiceImpl;

    @Autowired
    private IStopGroupService stopGroupServiceImpl;

    @Autowired
    private IStopsService stopsServiceImpl;

    @Autowired
    private IPathsService pathsServiceImpl;

    @Autowired
    private IPropertyService propertyServiceImpl;

    @Autowired
    private GetGroupsAndStops getGroupsAndStops;

    /**
     * 进入画板首页
     *
     * @param model
     * @param load
     * @return
     */
    @RequestMapping("/home")
    public String kitchenSink(Model model, String load) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        // 判断是否存在Flow的id(load),如果存在則加载，否則生成UUID返回返回页面
        if (StringUtils.isNotBlank(load)) {
            // 根据加载id进行查询
            Flow flowById = flowServiceImpl.getFlowById(load);
            if (null != flowById) {
                // 左侧的组和stops
                List<StopGroupVo> groupsVoList = stopGroupServiceImpl.getStopGroupAll();
                model.addAttribute("groupsVoList", groupsVoList);
                String maxStopPageId = flowServiceImpl.getMaxStopPageId(load);
                //maxStopPageId如果为空则默认给2,否则maxStopPageId+1
                maxStopPageId = StringUtils.isBlank(maxStopPageId) ? "2" : (Integer.parseInt(maxStopPageId) + 1) + "";
                model.addAttribute("maxStopPageId", maxStopPageId);
                MxGraphModelVo mxGraphModelVo = null;
                MxGraphModel mxGraphModel = flowById.getMxGraphModel();
                mxGraphModelVo = FlowXmlUtils.mxGraphModelPoToVo(mxGraphModel);
                // 把查询出來的mxGraphModelVo转为XML
                String loadXml = FlowXmlUtils.mxGraphModelToXml(mxGraphModelVo);
                model.addAttribute("xmlDate", loadXml);
                model.addAttribute("load", load);
                return "grapheditor/index";
            } else {
                return "errorPage";
            }
        } else {
            // 生成32位UUID
            load = Utils.getUUID32();
            Flow flow = new Flow();
            flow.setId(load);
            flow.setCrtDttm(new Date());
            flow.setCrtUser(username);
            flow.setLastUpdateDttm(new Date());
            flow.setLastUpdateUser(username);
            flow.setEnableFlag(true);
            flow.setName("default");
            MxGraphModel mxGraphModel = new MxGraphModel();
            mxGraphModel.setId(Utils.getUUID32());
            mxGraphModel.setCrtDttm(new Date());
            mxGraphModel.setCrtUser(username);
            mxGraphModel.setLastUpdateDttm(new Date());
            mxGraphModel.setLastUpdateUser(username);
            mxGraphModel.setEnableFlag(true);
            flow.setMxGraphModel(mxGraphModel);
            int addFlow = flowServiceImpl.addFlow(flow);
            if (addFlow > 0) {
                return "redirect:/grapheditor/home?load=" + load;
            } else {
                return "errorPage";
            }
        }
    }

    /**
     * 保存数据
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/saveData")
    @ResponseBody
    public String saveData(HttpServletRequest request, Model model) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("code", "0");
        String imageXML = request.getParameter("imageXML");
        String loadId = request.getParameter("load");
        String operType = request.getParameter("operType");
        if (StringUtils.isAnyEmpty(imageXML, loadId, operType)) {
            rtnMap.put("errMsg", "传入参数有空的");
            logger.info("传入参数有空的");
            return JsonUtils.toJsonNoException(rtnMap);
        } else {
            // 把页面传來的XML转为mxGraphModel
            MxGraphModelVo xmlToMxGraphModel = FlowXmlUtils.xmlToMxGraphModel(imageXML);
            StatefulRtnBase addFlow = flowServiceImpl.saveOrUpdateFlowAll(xmlToMxGraphModel, loadId, operType, true);
            // addFlow不为空且ReqRtnStatus的值为true,则保存成功
            if (null != addFlow && addFlow.isReqRtnStatus()) {
                rtnMap.put("code", "1");
                rtnMap.put("errMsg", "保存成功");
                logger.info("保存成功");
            } else {
                rtnMap.put("errMsg", "保存失败");
                logger.info("保存失败");
            }
            return JsonUtils.toJsonNoException(rtnMap);
        }

    }

    /**
     * reload左侧的stops和groups
     *
     * @param load
     * @return
     */
    @RequestMapping("/reloadStops")
    @ResponseBody
    public String reloadStops(String load) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("code", "0");
        getGroupsAndStops.addGroupAndStopsList();
        rtnMap.put("code", "1");
        rtnMap.put("load", load);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * 获取当前连线端口的使用情况
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/getStopsPort")
    @ResponseBody
    public String getStopsPort(HttpServletRequest request, Model model) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", "0");
        //取参数
        //flowId
        String flowId = request.getParameter("flowId");
        //output 的stop的PageId
        String sourceId = request.getParameter("sourceId");
        //input 的stop的PageId
        String targetId = request.getParameter("targetId");
        // 线的id
        String pathLineId = request.getParameter("pathLineId");
        // 参数判空,判断每个参数是否有空，如果有则直接返回
        // （isAnyEmpty只有有一个值为空就返回true）
        if (StringUtils.isAnyEmpty(flowId, sourceId, targetId, pathLineId)) {
            rtnMap.put("errMsg", "传入参数有空的");
            logger.info("传入参数有空的");
            return JsonUtils.toJsonNoException(rtnMap);
        } else {
            // 查 input和output的stop
            List<StopsVo> queryInfoList = stopsServiceImpl.getStopsByFlowIdAndPageIds(flowId, new String[]{sourceId, targetId});
            // 如果queryInfoList为空，或者queryInfoList的size小于2则直接返回
            if (null == queryInfoList || queryInfoList.size() < 2) {
                rtnMap.put("errMsg", "没有查询到source或target");
                return JsonUtils.toJsonNoException(rtnMap);
            } else {
                StopsVo sourceStop = null;
                StopsVo targetStop = null;
                //循环取出sourceStop和targetStop
                for (StopsVo stopVo : queryInfoList) {
                    if (null != stopVo) {
                        if (sourceId.equals(stopVo.getPageId())) {
                            sourceStop = stopVo;
                        } else if (targetId.equals(stopVo.getPageId())) {
                            targetStop = stopVo;
                        }
                    }
                }
                // 如果source或target为空则直接返回
                if (null == sourceStop || null == targetStop) {
                    rtnMap.put("errMsg", "查询到的source或target为空");
                    return JsonUtils.toJsonNoException(rtnMap);
                } else {
                    // 可能的十种情况：
                    // 第一种
                    // sourceStop的sourceStopOutports或者targetStop的targetStopInports为None不能输出
                    // 第二种
                    // sourceStop的sourceStopOutports为Default，去查询默认端口是否占用，如果占用直接返回并删除线，否则不处理
                    // targetStop的targetStopInports 为Default，去查询默认端口是否占用，如果占用直接返回并删除线，否则不处理
                    // 第三种
                    // sourceStop的sourceStopOutports为Default，去查询默认端口是否占用，如果占用直接返回并删除线，否则不处理
                    // targetStop的targetStopInports 为UserDefault，去查询端口占用情况，如果无可用端口返回并删除线，否则返回端口使用情况
                    // 第四种
                    // sourceStop的sourceStopOutports为Default，去查询默认端口是否占用，如果占用直接返回并删除线，否则不处理
                    // targetStop的targetStopInports 为Any，去查询端口占用情况，返回端口使用情况
                    // 第五种
                    // sourceStop的sourceStopOutports为UserDefault，去查询端口占用情况，如果无可用端口返回并删除线，否则返回端口使用情况
                    // targetStop的targetStopInports 为Default，去查询默认端口是否占用，如果占用直接返回并删除线，否则不处理
                    // 第六种
                    // sourceStop的sourceStopOutports为UserDefault，去查询端口占用情况，如果无可用端口返回并删除线，否则返回端口使用情况
                    // targetStop的targetStopInports 为UserDefault，去查询端口占用情况，如果无可用端口返回并删除线，否则返回端口使用情况
                    // 第七种
                    // sourceStop的sourceStopOutports为UserDefault，去查询端口占用情况，如果无可用端口返回并删除线，否则返回端口使用情况
                    // targetStop的targetStopInports 为Any，去查询端口占用情况，返回端口使用情况
                    // 第八种
                    // sourceStop的sourceStopOutports为Any，去查询端口占用情况，返回端口使用情况
                    // targetStop的targetStopInports 为Default，去查询默认端口是否占用，如果占用直接返回并删除线，否则不处理
                    // 第九种
                    // sourceStop的sourceStopOutports为Any，去查询端口占用情况，返回端口使用情况
                    // targetStop的targetStopInports 为UserDefault，去查询端口占用情况，如果无可用端口返回并删除线，否则返回端口使用情况
                    // 第十种
                    // sourceStop的sourceStopOutports为Any，去查询端口占用情况，返回端口使用情况
                    // targetStop的targetStopInports 为Any，去查询端口占用情况，返回端口使用情况
                    // 处理查询端口使用情况 并封装返回的map
                    rtnMap = stopPortUsed(rtnMap, sourceStop, targetStop, flowId, pathLineId);
                    return JsonUtils.toJsonNoException(rtnMap);
                }
            }
        }
    }

    /**
     * 处理查询端口使用情况 并封装返回的map
     *
     * @param rtnMap
     * @param sourceStop
     * @param targetStop
     * @param flowId
     * @param pathLineId
     * @return
     */
    private Map stopPortUsed(Map<String, Object> rtnMap, StopsVo sourceStop, StopsVo targetStop, String flowId, String pathLineId) {
        // 获取sourceStop和targetStop的端口类型
        PortType sourcePortType = sourceStop.getOutPortType();
        PortType targetPortType = targetStop.getInPortType();
        if (null != sourcePortType && null != targetPortType) {
            if (PortType.NONE != sourcePortType && PortType.NONE != sourcePortType) {
                // 调用stopPortUsage方法查询可用接口，返回的Map 的key如下
                // 1、stop的端口类型枚举的text(value为可使用端口数量Integer)
                // 2、isSourceStop(value为是否为source)
                // 3、portUsageMap(value为端口详细信息map(使用情况)， map的key为端口名，value为是否可用boolean)
                // 查询sourceStop接口的占用情况
                Map sourcePortUsageMap = stopPortUsage(true, flowId, sourceStop, pathLineId);
                if (null != sourcePortUsageMap) {
                    // 取出可用端口数量
                    Integer sourceCounts = (Integer) sourcePortUsageMap.get(sourcePortType.getText());
                    // 判断可用端口数量是否大于0
                    if (null != sourceCounts && sourceCounts > 0) {
                        // 查询targetStop接口的占用情况
                        Map targetPortUsageMap = stopPortUsage(false, flowId, targetStop, pathLineId);
                        if (null != targetPortUsageMap) {
                            // 取出可用端口数量
                            Integer targetCounts = (Integer) targetPortUsageMap.get(targetPortType.getText());
                            // 判断可用端口数量是否大于0
                            if (null != targetCounts && targetCounts > 0) {
                                rtnMap.put("code", "1");
                                // 将可用端口数量放入返回的map
                                rtnMap.put("sourceCounts", sourceCounts);
                                rtnMap.put("targetCounts", targetCounts);
                                // 将端口的详细占用情况取出，放入返回的map
                                rtnMap.put("sourcePortUsageMap", (Map<String, Boolean>) sourcePortUsageMap.get("portUsageMap"));
                                rtnMap.put("targetPortUsageMap", (Map<String, Boolean>) targetPortUsageMap.get("portUsageMap"));
                                // stop的端口类型放入返回的map
                                rtnMap.put("sourceType", sourcePortType);
                                rtnMap.put("targetType", targetPortType);
                                // stop的name放入返回的map
                                rtnMap.put("sourceName", sourceStop.getName());
                                rtnMap.put("targetName", targetStop.getName());
                            } else {
                                rtnMap.put("code", "0");
                                rtnMap.put("errMsg", "查询targetStop暂无可用端口");
                                logger.info("查询targetStop暂无可用端口");
                            }
                        } else {
                            rtnMap.put("code", "0");
                            rtnMap.put("errMsg", "查询targetStop接口的占用情况出错");
                            logger.info("查询targetStop接口的占用情况出错");
                        }
                    } else {
                        rtnMap.put("code", "0");
                        rtnMap.put("errMsg", "查询sourceStop暂无可用端口");
                        logger.info("查询sourceStop暂无可用端口");
                    }
                } else {
                    rtnMap.put("code", "0");
                    rtnMap.put("errMsg", "查询sourceStop接口的占用情况出错");
                    logger.info("查询sourceStop接口的占用情况出错");
                }
            } else {
                rtnMap.put("code", "0");
                rtnMap.put("errMsg", "sourceStop的sourceStopOutports或者targetStop的targetStopInports为None不能输出");
                logger.info("sourceStop的sourceStopOutports或者targetStop的targetStopInports为None不能输出");
            }
        } else {
            rtnMap.put("code", "0");
            rtnMap.put("errMsg", "sourceStop的sourceStopOutports或者targetStop的targetStopInports为Null不能输出");
            logger.info("sourceStop的sourceStopOutports或者targetStop的targetStopInports为Null不能输出");
        }
        return rtnMap;
    }

    /**
     * 查询端口使用情况
     *
     * @param isSourceStop
     * @param flowId
     * @param stopVo
     * @param pathLineId
     * @return map key有
     * stop的端口类型枚举的text(value为可使用端口数量Integer)，
     * isSourceStop(value为是否为source)，
     * stopPortUsage(value为端口详细信息map， map的key为端口名，value为是否可用boolean)
     */
    private Map stopPortUsage(boolean isSourceStop, String flowId, StopsVo stopVo, String pathLineId) {
        // 用于返回的map,
        // key有
        // 1、stop的端口类型枚举的text(value为可使用端口数量Integer)
        // 2、isSourceStop(value为是否为source)
        // 3、portUsageMap(value为端口详细信息map(使用情况)， map的key为端口名，value为是否可用boolean)
        Map<String, Object> stopPortUsageMap = null;
        if (null != stopVo) {
            stopPortUsageMap = new HashMap<String, Object>();
            // 已用端口数量
            Integer usedPathsCounts = 0;
            // 可用端口数量
            Integer availablePathsCounts = 0;
            // 已用接口的list信息
            List<PathsVo> usedPathsList = null;

            // 端口详细信息map（使用情况）
            Map<String, Boolean> portUsageMap = new HashMap<String, Boolean>();

            // 获取stopVo的端口类型
            PortType stopPortType = (isSourceStop ? stopVo.getOutPortType() : stopVo.getInPortType());
            switch (stopPortType) {
                case DEFAULT:

                    // DEFAULT的默认可用端口数为1
                    availablePathsCounts = 1;

                    // 判断stopVo是否为source
                    if (isSourceStop) {
                        // put是否为source
                        stopPortUsageMap.put("isSourceStop", true);
                        // 查询stopVo接口的占用情况
                        usedPathsList = pathsServiceImpl.getPaths(flowId, stopVo.getPageId(), null);
                    } else {
                        // put是否为source
                        stopPortUsageMap.put("isSourceStop", false);

                        // 查询stopVo接口的占用情况
                        usedPathsList = pathsServiceImpl.getPaths(flowId, null, stopVo.getPageId());
                    }

                    // 判断查询stopVo接口的占用情况是否为空
                    if (null != usedPathsList) {
                        String currentPathsId = "";
                        PathsVo currentPaths = pathsServiceImpl.getPathsByFlowIdAndPageId(flowId, pathLineId);
                        if (null != currentPaths) {
                            currentPathsId = currentPaths.getId();
                        }
                        // 循环和当前线进行对比，排除当前线
                        for (PathsVo pathsVo : usedPathsList) {
                            if (null != pathsVo) {
                                if (currentPathsId.equals(pathsVo.getId())) {
                                    continue;
                                }
                                availablePathsCounts = availablePathsCounts - 1;
                            }
                        }
                    }
                    // 判断可用接口数量
                    if (null != availablePathsCounts && availablePathsCounts < 0) {
                        logger.warn("可用端口数为负数，默认重置为0");
                        availablePathsCounts = 0;
                    }
                    // put可用接口数量
                    stopPortUsageMap.put(PortType.DEFAULT.getText(), availablePathsCounts);

                    break;
                case USER_DEFAULT:

                    // stopVo所有端口
                    String stopVoPorts = "";
                    // 判断stopVo是否为source
                    if (isSourceStop) {
                        // put是否为source
                        stopPortUsageMap.put("isSourceStop", true);

                        // 查询stopVo接口的占用情况
                        usedPathsList = pathsServiceImpl.getPaths(flowId, stopVo.getPageId(), null);

                        // 把查到已使用的端口put入portUsageMap中
                        portUsageMap = this.portStrToMap(portUsageMap, true, usedPathsList, flowId, pathLineId, null);

                        // 取到stopVo所有端口
                        stopVoPorts = stopVo.getOutports();

                    } else {
                        // put是否为source
                        stopPortUsageMap.put("isSourceStop", false);

                        // 查询stopVo接口的占用情况
                        usedPathsList = pathsServiceImpl.getPaths(flowId, null, stopVo.getPageId());

                        // 把查到已使用的端口put入portUsageMap中
                        portUsageMap = this.portStrToMap(portUsageMap, false, usedPathsList, flowId, pathLineId, null);

                        // 取到stopVo所有端口
                        stopVoPorts = stopVo.getInports();
                    }
                    // 目前portUsageMap中只放入了已使用的端口，所有取他的长的就是已用端口数量
                    usedPathsCounts = portUsageMap.size();
                    // 将全部端口信息放入portUsageMap中
                    portUsageMap = this.portStrToMap(portUsageMap, null, null, null, null, stopVoPorts);
                    // portUsageMap 刚刚放入全部端口，所以用portUsageMap的size减去已用端口数量就是可用端口数量
                    availablePathsCounts = portUsageMap.size() - usedPathsCounts;
                    // put可用接口数量
                    stopPortUsageMap.put(PortType.USER_DEFAULT.getText(), availablePathsCounts);

                    // 端口使用情况
                    stopPortUsageMap.put("portUsageMap", portUsageMap);

                    break;
                case ANY:

                    // stopVo所有端口
                    String stopVoPortsAny = "";

                    // 要取得属性名，因为Any类型的stop端口是存储在属性中的
                    String propertyVoName = "";

                    // 可用接口数量无限多，暂用99999表示
                    stopPortUsageMap.put(PortType.ANY.getText(), 99999);

                    // 判断stopVo是否为source
                    if (isSourceStop) {
                        // put是否为source
                        stopPortUsageMap.put("isSourceStop", true);

                        // 查询stopVo接口的占用情况
                        usedPathsList = pathsServiceImpl.getPaths(flowId, stopVo.getPageId(), null);

                        // 把查到已使用的端口put入portUsageMap中
                        portUsageMap = this.portStrToMap(portUsageMap, true, usedPathsList, flowId, pathLineId, null);

                        // 给定属性名
                        propertyVoName = "outports";
                    } else {
                        // put是否为source
                        stopPortUsageMap.put("isSourceStop", false);

                        // 查询stopVo接口的占用情况
                        usedPathsList = pathsServiceImpl.getPaths(flowId, null, stopVo.getPageId());

                        // 把查到已使用的端口put入portUsageMap中
                        portUsageMap = this.portStrToMap(portUsageMap, false, usedPathsList, flowId, pathLineId, null);

                        // 给定属性名
                        propertyVoName = "outports";

                    }
                    // 获取stopVo的属性List
                    List<StopsPropertyVo> propertyVoList = stopVo.getPropertiesVo();
                    // 循环属性，获取存放端口的属性
                    for (StopsPropertyVo stopsPropertyVo : propertyVoList) {
                        if (null != stopsPropertyVo) {
                            // 判断是否为存放端口的属性
                            if (propertyVoName.equals(stopsPropertyVo.getName())) {
                                // 取到stopVo所有端口
                                stopVoPortsAny = stopsPropertyVo.getCustomValue();
                                break;
                            }
                        }
                    }
                    // 将全部端口信息放入portUsageMap中
                    portUsageMap = this.portStrToMap(portUsageMap, null, null, null, null, stopVoPortsAny);
                    // 端口使用情况
                    stopPortUsageMap.put("portUsageMap", portUsageMap);

                    break;
                case NONE:
                    // NONE表示无可用端口
                    stopPortUsageMap.put(PortType.NONE.getText(), 0);
                    logger.info("stopPortType为'" + stopPortType + "'，无操作，返回null");
                    break;
                default:
                    stopPortUsageMap = null;
                    logger.info("stopPortType为'" + stopPortType + "'，无操作，返回null");
                    break;
            }
        }
        return stopPortUsageMap;
    }

    /**
     * 将端口数据放入portUsageMap
     *
     * @param portUsageMap  全部的端口及其使用情况
     * @param isSourceStop  是否为sourceStop
     * @param usedPathsList 数据库查询到的线的信息，代表已经用掉的端口(排除当前线所用端口)
     * @param flowId        当前flow的id
     * @param pathLineId    当前线的pageId
     * @param stopVoPorts   stop中的端口信息，全部的端口
     * @return
     */
    private Map portStrToMap(Map<String, Boolean> portUsageMap, Boolean isSourceStop, List<PathsVo> usedPathsList, String flowId, String pathLineId, String stopVoPorts) {
        if (null != portUsageMap) {
            if (null != usedPathsList && usedPathsList.size() > 0) {
                String currentPathsId = "";
                PathsVo currentPaths = pathsServiceImpl.getPathsByFlowIdAndPageId(flowId, pathLineId);
                if (null != currentPaths) {
                    currentPathsId = currentPaths.getId();
                }
                if (null != isSourceStop && isSourceStop) {
                    // 把查到已使用的端口put进map
                    for (PathsVo pathsVo : usedPathsList) {
                        if (null != pathsVo && StringUtils.isNotBlank(pathsVo.getOutport())) {
                            if (currentPathsId.equals(pathsVo.getId())) {
                                portUsageMap.put(pathsVo.getOutport(), true);
                            } else {
                                portUsageMap.put(pathsVo.getOutport(), false);
                            }
                        }
                    }
                } else {
                    // 把查到已使用的端口put进map
                    for (PathsVo pathsVo : usedPathsList) {
                        if (null != pathsVo && StringUtils.isNotBlank(pathsVo.getInport())) {
                            if (currentPathsId.equals(pathsVo.getId())) {
                                portUsageMap.put(pathsVo.getInport(), true);
                            } else {
                                portUsageMap.put(pathsVo.getInport(), false);
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotBlank(stopVoPorts)) {
                // stopVo所有端口的字符串转为String数组
                String[] stopVoPortsSplit = stopVoPorts.split(",");
                // 判断是否转换成功并判空
                if (null != stopVoPortsSplit && stopVoPortsSplit.length > 0) {
                    // 循环数组
                    for (String portName : stopVoPortsSplit) {
                        if (StringUtils.isNotBlank(portName)) {
                            // 在portUsageMap中根据端口名进行取值，取到则说明 已放如不用继续放
                            Boolean isUsed = portUsageMap.get(portName);
                            // 之前把所有的已经使用的端口都放入的map中，取不到则说明还没放入，也没有使用
                            if (null == isUsed) {
                                portUsageMap.put(portName, true);
                            }
                        }
                    }
                }
            }
        }
        return portUsageMap;
    }

    /**
     * 保存用户选择的端口
     *
     * @param request
     * @return
     */
    @RequestMapping("/savePathsPort")
    @ResponseBody
    public String savePathsPort(HttpServletRequest request) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("code", "0");
        //取参数
        //flowId
        String flowId = request.getParameter("flowId");
        // 线的id
        String pathLineId = request.getParameter("pathLineId");
        // sourceOut
        String sourcePortVal = request.getParameter("sourcePortVal");
        //output 的stop的PageId
        String sourceId = request.getParameter("sourceId");
        //input 的stop的PageId
        String targetId = request.getParameter("targetId");
        // targetIn
        String targetPortVal = request.getParameter("targetPortVal");
        if (StringUtils.isAnyEmpty(flowId, pathLineId, sourceId, targetId)) {
            rtnMap.put("errMsg", "传入参数有空的");
            logger.info("传入参数有空的");
            return JsonUtils.toJsonNoException(rtnMap);
        } else {
            StopsVo sourceStop = null;
            StopsVo targetStop = null;
            List<StopsVo> queryInfoList = stopsServiceImpl.getStopsByFlowIdAndPageIds(flowId, new String[]{sourceId, targetId});
            // 如果queryInfoList为空，或者queryInfoList的size小于2则直接返回
            if (null == queryInfoList || queryInfoList.size() < 2) {
                rtnMap.put("errMsg", "没有查询到source或target");
                return JsonUtils.toJsonNoException(rtnMap);
            } else {
                //循环取出sourceStop和targetStop
                for (StopsVo stopVo : queryInfoList) {
                    if (null != stopVo) {
                        if (sourceId.equals(stopVo.getPageId())) {
                            sourceStop = stopVo;
                        } else if (targetId.equals(stopVo.getPageId())) {
                            targetStop = stopVo;
                        }
                    }
                }
            }
            PathsVo currentPaths = pathsServiceImpl.getPathsByFlowIdAndPageId(flowId, pathLineId);
            if (StringUtils.isNotBlank(sourcePortVal)) {
                currentPaths.setOutport(sourcePortVal);
                updatePropertyBypaths(sourcePortVal, sourceStop, "outports");
            }
            if (StringUtils.isNotBlank(targetPortVal)) {
                currentPaths.setInport(targetPortVal);
                updatePropertyBypaths(targetPortVal, targetStop, "inports");
            }
            int i = pathsServiceImpl.upDatePathsVo(currentPaths);
            if (i <= 0) {
                rtnMap.put("code", "0");
                rtnMap.put("errMsg", "保存失败");
            } else {
                rtnMap.put("code", "1");
                rtnMap.put("errMsg", "保存成功");
            }
            return JsonUtils.toJsonNoException(rtnMap);
        }


    }

    /**
     * 根据paths的端口信息，修改端口类型为any的端口属性值
     *
     * @param sourcePortVal
     * @param stopsVo
     * @param propertyName
     */
    private void updatePropertyBypaths(String sourcePortVal, StopsVo stopsVo, String propertyName) {
        if (null != stopsVo) {
            if (PortType.ANY == stopsVo.getInPortType() || PortType.ANY == stopsVo.getOutPortType()) {
                List<StopsPropertyVo> propertyVoList = stopsVo.getPropertiesVo();
                if (null != propertyVoList && propertyVoList.size() > 0) {
                    String ports = null;
                    StopsPropertyVo propertyVoSave = null;
                    for (StopsPropertyVo propertyVo : propertyVoList) {
                        if (propertyName.equals(propertyVo.getName())) {
                            propertyVoSave = propertyVo;
                            break;
                        }
                    }
                    if (null != propertyVoSave) {
                        if (null == propertyVoSave.getCustomValue()) {
                            ports = "";
                        } else {
                            ports = propertyVoSave.getCustomValue();
                        }
                        if (StringUtils.isNotBlank(ports)) {
                            ports = ports + ",";
                        }
                        propertyServiceImpl.updateProperty((ports + sourcePortVal), propertyVoSave.getId());
                    }
                }
            }
        }
    }
}
