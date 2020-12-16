package cn.cnic.component.flow.service.impl;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.flow.entity.CustomizedProperty;
import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.service.ICustomizedPropertyService;
import cn.cnic.component.flow.vo.PathsVo;
import cn.cnic.component.flow.vo.StopsCustomizedPropertyVo;
import cn.cnic.component.flow.mapper.CustomizedPropertyMapper;
import cn.cnic.component.flow.mapper.PathsMapper;
import cn.cnic.component.flow.mapper.StopsMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CustomizedPropertyServiceImpl implements ICustomizedPropertyService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private CustomizedPropertyMapper customizedPropertyMapper;

    @Resource
    private StopsMapper stopsMapper;

    @Resource
    private PathsMapper pathsMapper;

    @Override
    @Transactional
    public String addStopCustomizedProperty(String username, StopsCustomizedPropertyVo stopsCustomizedPropertyVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal operation");
        }
        if (null == stopsCustomizedPropertyVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Incorrect param");
        }
        String stopId = stopsCustomizedPropertyVo.getStopId();
        if (StringUtils.isBlank(stopId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("stopId is null");
        }
        List<CustomizedProperty> customizedPropertyListByStopsIdAndName = customizedPropertyMapper.getCustomizedPropertyListByStopsIdAndName(stopId, stopsCustomizedPropertyVo.getName());
        if (null != customizedPropertyListByStopsIdAndName && customizedPropertyListByStopsIdAndName.size() > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Key repeat, please re-enter");
        }
        Stops stopsById = stopsMapper.getStopsById(stopId);
        if (null == stopsById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Can't find ‘stop’ with id " + stopsById);
        }
        CustomizedProperty customizedProperty = new CustomizedProperty();
        BeanUtils.copyProperties(stopsCustomizedPropertyVo, customizedProperty);
        String id = UUIDUtils.getUUID32();
        customizedProperty.setId(id);
        customizedProperty.setCrtDttm(new Date());
        customizedProperty.setCrtUser(username);
        customizedProperty.setLastUpdateDttm(new Date());
        customizedProperty.setLastUpdateUser(username);
        customizedProperty.setEnableFlag(true);

        customizedProperty.setStops(stopsById);
        int optDataCount = customizedPropertyMapper.addCustomizedProperty(customizedProperty);
        if (optDataCount > 0) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("stopPageId", stopsById.getPageId());
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("save failed");
        }

    }

    @Override
    public String updateStopsCustomizedProperty(String username, StopsCustomizedPropertyVo stopsCustomizedPropertyVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == stopsCustomizedPropertyVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Incorrect param");
        }
        String id = stopsCustomizedPropertyVo.getId();
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("stopId is null");
        }
        int optDataCount = customizedPropertyMapper.updateCustomizedPropertyCustomValue(username, stopsCustomizedPropertyVo.getCustomValue(), id);
        if (optDataCount > 0) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("value", stopsCustomizedPropertyVo.getCustomValue());
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("save failed");
        }
    }

    @Override
    public String deleteStopsCustomizedProperty(String username, String customPropertyId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        int optDataCount = 0;
        optDataCount = customizedPropertyMapper.updateEnableFlagByStopId(username, customPropertyId);
        if (optDataCount > 0) {
            rtnMap.put("code", 200);
        } else {
            logger.warn("remove failed");
            rtnMap.put("errorMsg", "remove failed");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String deleteRouterStopsCustomizedProperty(String username, String customPropertyId) {
        return this.deleteStopsCustomizedProperty(username,customPropertyId);
    }

    @Override
    public String getRouterStopsCustomizedProperty(String customPropertyId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
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
