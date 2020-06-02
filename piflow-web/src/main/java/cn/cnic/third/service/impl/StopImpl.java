package cn.cnic.third.service.impl;

import cn.cnic.base.util.HttpUtils;
import cn.cnic.base.util.ImageUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.stopsComponent.mapper.StopGroupMapper;
import cn.cnic.component.stopsComponent.model.PropertyTemplate;
import cn.cnic.component.stopsComponent.model.StopsComponentGroup;
import cn.cnic.component.stopsComponent.model.StopsTemplate;
import cn.cnic.third.service.IStop;
import cn.cnic.third.vo.stop.ThirdStopsComponentPropertyVo;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class StopImpl implements IStop {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private StopGroupMapper stopGroupMapper;

    @Override
    public String[] getAllGroup() {
        String[] group = null;
        String sendGetData = HttpUtils.doGet(SysParamsCache.getStopsGroupsUrl(), null, 30 * 1000);
        logger.debug("return msg：" + sendGetData);
        if (StringUtils.isNotBlank(sendGetData)) {
            String jsonResult = JSONObject.fromObject(sendGetData).getString("groups");
            if (StringUtils.isNotBlank(jsonResult)) {
                group = jsonResult.split(",");
            }
        }
        return group;
    }

    @Override
    public String[] getAllStops() {
        String[] stop = null;
        String sendGetData = HttpUtils.doGet(SysParamsCache.getStopsListUrl(), null, 30 * 1000);
        logger.debug("return msg：" + sendGetData);
        if (StringUtils.isNotBlank(sendGetData)) {
            String jsonResult = JSONObject.fromObject(sendGetData).getString("stops");
            //Separate the tops from the array with the, sign
            stop = jsonResult.split(",");
        }
        return stop;
    }

    @Override
    public StopsTemplate getStopInfo(String bundleStr) {
        StopsTemplate stopsTemplate = null;
        UserVo user = null; //SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        if (StringUtils.isNotBlank(bundleStr)) {
            Map<String, String> map = new HashMap<>();
            map.put("bundle", bundleStr);
            String sendGetData = HttpUtils.doGet(SysParamsCache.getStopsInfoUrl(), map, 30 * 1000);
            logger.info("return msg：" + sendGetData);
            if (StringUtils.isNotBlank(sendGetData)) {
                if (sendGetData.contains("Error")) {
                    logger.warn("return err");
                } else {
                    JSONObject jsonObject = JSONObject.fromObject(sendGetData);
                    JSONObject stopInfoJson = jsonObject.getJSONObject("StopInfo");
                    List<String> list = new ArrayList<>();
                    String bundle = stopInfoJson.getString("bundle") + "";
                    String owner = stopInfoJson.getString("owner") + "";
                    String groups = stopInfoJson.getString("groups") + "";
                    String properties = stopInfoJson.getString("properties") + "";
                    String name = stopInfoJson.getString("name") + "";
                    String description = stopInfoJson.getString("description") + "";
                    String icon = stopInfoJson.getString("icon") + "";
                    String inports = stopInfoJson.getString("inports") + "";
                    boolean isCustomized = stopInfoJson.getBoolean("isCustomized");
                    PortType inPortType = null;
                    if (StringUtils.isNotBlank(inports)) {
                        inPortType = null;
                        for (PortType value : PortType.values()) {
                            if (inports.equalsIgnoreCase(value.getValue())) {
                                inPortType = value;
                            }
                        }
                        if (null == inPortType) {
                            inPortType = PortType.USER_DEFAULT;
                        }
                    }
                    PortType.selectGenderByValue(inports);
                    String outports = stopInfoJson.getString("outports") + "";
                    PortType outPortType = null;
                    if (StringUtils.isNotBlank(outports)) {
                        outPortType = null;
                        for (PortType value : PortType.values()) {
                            if (outports.equalsIgnoreCase(value.getValue())) {
                                outPortType = value;
                            }
                        }
                        if (null == outPortType) {
                            outPortType = PortType.USER_DEFAULT;
                        }
                    }
                    if (StringUtils.isNotBlank(icon)) {
                        ImageUtils.generateImage(icon, name + "_128x128", "png", SysParamsCache.IMAGES_PATH);
                    }
                    // There are multiple groups in a stop, so I need to deal with it here.
                    if (groups.contains(",")) {
                        String[] split = groups.split(",");
                        for (int i = 0; i < split.length; i++) {
                            if (!"".equals(split[i]) && split[i] != null)
                                list.add(split[i]);
                        }
                    } else {
                        list.add(groups);
                    }
                    // Query group information according to groupName in stops
                    List<StopsComponentGroup> stopGroupByName = stopGroupMapper.getStopGroupByName(list);
                    stopsTemplate = new StopsTemplate();
                    stopsTemplate.setId(SqlUtils.getUUID32());
                    stopsTemplate.setCrtDttm(new Date());
                    stopsTemplate.setCrtUser(username);
                    stopsTemplate.setEnableFlag(true);
                    stopsTemplate.setLastUpdateUser(username);
                    stopsTemplate.setLastUpdateDttm(new Date());
                    stopsTemplate.setBundel(bundle);
                    stopsTemplate.setDescription(description);
                    stopsTemplate.setGroups(groups);
                    stopsTemplate.setName(name);
                    stopsTemplate.setInports(inports);
                    stopsTemplate.setInPortType(inPortType);
                    stopsTemplate.setOutports(outports);
                    stopsTemplate.setOutPortType(outPortType);
                    stopsTemplate.setOwner(owner);
                    stopsTemplate.setIsCustomized(isCustomized);
                    stopsTemplate.setStopGroupList(stopGroupByName);
                    JSONArray jsonArray = JSONArray.fromObject(properties);
                    if (null != jsonArray && !jsonArray.isEmpty()) {
                        List<PropertyTemplate> listPropertyTemplate = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            PropertyTemplate PropertyTemplate = new PropertyTemplate();
                            PropertyTemplate.setId(SqlUtils.getUUID32());
                            PropertyTemplate.setCrtDttm(new Date());
                            PropertyTemplate.setCrtUser(username);
                            PropertyTemplate.setEnableFlag(true);
                            PropertyTemplate.setLastUpdateUser(username);
                            PropertyTemplate.setLastUpdateDttm(new Date());
                            PropertyTemplate.setDefaultValue(jsonArray.getJSONObject(i).getString("defaultValue"));
                            PropertyTemplate.setAllowableValues(jsonArray.getJSONObject(i).getString("allowableValues"));
                            PropertyTemplate.setDescription(jsonArray.getJSONObject(i).getString("description"));
                            PropertyTemplate.setDisplayName(jsonArray.getJSONObject(i).getString("displayName"));
                            PropertyTemplate.setName(jsonArray.getJSONObject(i).getString("name"));
                            PropertyTemplate.setRequired(jsonArray.getJSONObject(i).getString("required").equals("true") ? true : false);
                            PropertyTemplate.setSensitive(jsonArray.getJSONObject(i).getString("sensitive").equals("true") ? true : false);
                            PropertyTemplate.setStopsTemplate(stopsTemplate.getId());
                            PropertyTemplate.setPropertySort((long) i);
                            listPropertyTemplate.add(PropertyTemplate);
                        }
                        stopsTemplate.setProperties(listPropertyTemplate);
                    }
                }
            } else {
                logger.warn("Interface return value is null");
            }
        } else {
            logger.warn("bundleStr value is null");
        }
        return stopsTemplate;
    }

    @Override
    public Map<String, List<String>> getStopsListWithGroup() {
        String sendGetData = HttpUtils.doGet(SysParamsCache.getStopsListWithGroupUrl(), null, 30 * 1000);
        logger.debug("return msg：" + sendGetData);
        if (StringUtils.isBlank(sendGetData)) {
            return null;
        }
        Map<String, List<String>> stopsListWithGroup = new HashMap<>();
        String jsonResult = JSONObject.fromObject(sendGetData).getString("stopWithGroup");
        String[] bundleAndGroupArray = jsonResult.split(",");
        for (String str : bundleAndGroupArray) {
            if (StringUtils.isBlank(str)) {
                continue;
            }
            String[] split = str.split(":");
            List<String> bundleList = stopsListWithGroup.get(split[0]);
            if (null == bundleList) {
                bundleList = new ArrayList<>();
            }
            if (split.length == 2) {
                bundleList.add(split[1]);
            }
            stopsListWithGroup.put(split[0], bundleList);
        }
        return stopsListWithGroup;
    }

    @Override
    public ThirdStopsComponentVo getStopInfoNew(String bundleStr) {
        if (StringUtils.isBlank(bundleStr)) {
            logger.warn("bundleStr value is null");
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("bundle", bundleStr);
        String sendGetData = HttpUtils.doGet(SysParamsCache.getStopsInfoUrl(), map, 30 * 1000);
        logger.info("return msg：" + sendGetData);
        if (StringUtils.isBlank(sendGetData)) {
            logger.warn("Interface return value is null");
            return null;
        }
        if (sendGetData.contains("Error")) {
            logger.warn("return err");
            return null;
        }
        ThirdStopsComponentVo thirdStopsComponentVo = new ThirdStopsComponentVo();
        // Also convert the json string to a json object, and then convert the json object to a java object, as shown below.
        JSONObject jsonObject = JSONObject.fromObject(sendGetData).getJSONObject("StopInfo");// Convert a json string to a json object
        // Needed when there is a List in jsonObj
        Map<String, Class> classMap = new HashMap<>();
        // Key is the name of the List in jsonObj, and the value is a generic class of list
        classMap.put("properties", ThirdStopsComponentPropertyVo.class);
        //classMap.put("allowableValues", ThirdAllowableValuesVo.class);
        // Convert a json object to a java object
        thirdStopsComponentVo = (ThirdStopsComponentVo) JSONObject.toBean(jsonObject, ThirdStopsComponentVo.class, classMap);

        return thirdStopsComponentVo;
    }

}
