package com.nature.component.flow.service.impl;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.CustomizedProperty;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Stops;
import com.nature.component.flow.service.ICustomizedPropertyService;
import com.nature.component.flow.vo.PathsVo;
import com.nature.component.flow.vo.StopsCustomizedPropertyVo;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.mapper.flow.CustomizedPropertyMapper;
import com.nature.mapper.flow.PathsMapper;
import com.nature.mapper.flow.StopsMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CustomizedPropertyServiceImpl implements ICustomizedPropertyService {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private CustomizedPropertyMapper customizedPropertyMapper;

    @Autowired
    private StopsMapper stopsMapper;

    @Autowired
    private PathsMapper pathsMapper;

    @Override
    @Transactional
    public String addStopCustomizedProperty(StopsCustomizedPropertyVo stopsCustomizedPropertyVo) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        int optDataCount = 0;
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null != stopsCustomizedPropertyVo && null != currentUser) {
            String stopId = stopsCustomizedPropertyVo.getStopId();
            if (StringUtils.isNotBlank(stopId)) {
                List<CustomizedProperty> customizedPropertyListByStopsIdAndName = customizedPropertyMapper.getCustomizedPropertyListByStopsIdAndName(stopId, stopsCustomizedPropertyVo.getName());
                if (null == customizedPropertyListByStopsIdAndName || customizedPropertyListByStopsIdAndName.size() <= 0) {
                    Stops stopsById = stopsMapper.getStopsById(stopId);
                    if (null != stopsById) {
                        String username = currentUser.getUsername();
                        CustomizedProperty customizedProperty = new CustomizedProperty();
                        BeanUtils.copyProperties(stopsCustomizedPropertyVo, customizedProperty);
                        String id = SqlUtils.getUUID32();
                        customizedProperty.setId(id);
                        customizedProperty.setCrtDttm(new Date());
                        customizedProperty.setCrtUser(username);
                        customizedProperty.setLastUpdateDttm(new Date());
                        customizedProperty.setLastUpdateUser(username);
                        customizedProperty.setEnableFlag(true);

                        customizedProperty.setStops(stopsById);
                        optDataCount = customizedPropertyMapper.addCustomizedProperty(customizedProperty);
                        if (optDataCount > 0) {
                            rtnMap.put("code", 200);
                            rtnMap.put("stopPageId", stopsById.getPageId());
                        } else {
                            logger.warn("save failed");
                            rtnMap.put("errorMsg", "save failed");
                        }
                    } else {
                        logger.warn("Can't find ‘stop’ with id " + stopsById);
                        rtnMap.put("errorMsg", "Can't find ‘stop’ with id " + stopsById);
                    }
                } else {
                    logger.warn("Key repeat, please re-enter");
                    rtnMap.put("errorMsg", "Key repeat, please re-enter");
                }

            } else {
                logger.warn("stopId is null");
                rtnMap.put("errorMsg", "stopId is null");
            }
        } else {
            logger.warn("Incorrect param or illegal operation");
            rtnMap.put("errorMsg", "Incorrect param or illegal operation");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String updateStopsCustomizedProperty(StopsCustomizedPropertyVo stopsCustomizedPropertyVo) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        int optDataCount = 0;
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null != stopsCustomizedPropertyVo && null != currentUser) {
            String id = stopsCustomizedPropertyVo.getId();
            if (StringUtils.isNotBlank(id)) {
                optDataCount = customizedPropertyMapper.updateCustomizedPropertyCustomValue(stopsCustomizedPropertyVo.getCustomValue(), id);
                if (optDataCount > 0) {
                    rtnMap.put("code", 200);
                    rtnMap.put("value", stopsCustomizedPropertyVo.getCustomValue());
                } else {
                    logger.warn("save failed");
                    rtnMap.put("errorMsg", "save failed");
                }
            } else {
                logger.warn("stopId is null");
                rtnMap.put("errorMsg", "stopId is null");
            }
        } else {
            logger.warn("Incorrect param or illegal operation");
            rtnMap.put("errorMsg", "Incorrect param or illegal operation");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String deleteStopsCustomizedProperty(String customPropertyId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        int optDataCount = 0;
        optDataCount = customizedPropertyMapper.updateEnableFlagByStopId(customPropertyId);
        if (optDataCount > 0) {
            rtnMap.put("code", 200);
        } else {
            logger.warn("remove failed");
            rtnMap.put("errorMsg", "remove failed");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String deleteRouterStopsCustomizedProperty(String customPropertyId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        int optDataCount = 0;
        //CustomizedProperty customizedPropertyById = customizedPropertyMapper.getCustomizedPropertyById(customPropertyId);
        //optDataCount = customizedPropertyMapper.updateEnableFlagByStopId(customPropertyId);
        if (optDataCount > 0) {
            rtnMap.put("code", 200);
            //rtnMap.put("stopPageId", stopsById.getPageId());
        } else {
            logger.warn("remove failed");
            rtnMap.put("errorMsg", "remove failed");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String getRouterStopsCustomizedProperty(String customPropertyId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        int optDataCount = 0;
        CustomizedProperty customizedPropertyById = customizedPropertyMapper.getCustomizedPropertyById(customPropertyId);
        if (null != customizedPropertyById) {
            Stops stops = customizedPropertyById.getStops();
            if (null != stops && null != stops.getFlow()) {
                String flowId = stops.getFlow().getId();
                String stopsPageId = stops.getPageId();
                if (StringUtils.isNoneEmpty(flowId)) {
                    List<Paths> pathsList = pathsMapper.getPaths(flowId, null, stopsPageId, null);
                    List<PathsVo> pathsVoList = null;
                    if (null != pathsList && pathsList.size() > 0) {
                        pathsVoList = new ArrayList<>();
                        for (Paths paths : pathsList) {
                            PathsVo pathsVo = new PathsVo();
                            Stops stopByFlowIdAndStopPageId = stopsMapper.getStopByFlowIdAndStopPageId(flowId, paths.getTo());
                            if (null != stopByFlowIdAndStopPageId) {
                                BeanUtils.copyProperties(paths, pathsVo);
                                pathsVo.setFrom(stops.getName());
                                pathsVo.setTo(stopByFlowIdAndStopPageId.getName());
                                pathsVoList.add(pathsVo);
                            }
                        }
                    }
                    rtnMap.put("pathsVoList", pathsVoList);
                    rtnMap.put("code", 200);
                } else {
                    logger.warn("remove failed , stopID or flowID data error");
                    rtnMap.put("errorMsg", "remove failed , stopID or flowID data error");
                }
            } else {
                logger.warn("remove failed , stop or flow data error");
                rtnMap.put("errorMsg", "remove failed , stop or flow data error");
            }
        } else {
            logger.warn("remove failed , data does not exist");
            rtnMap.put("errorMsg", "remove failed , data does not exist");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

}
