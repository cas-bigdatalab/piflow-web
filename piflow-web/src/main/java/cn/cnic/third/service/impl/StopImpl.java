package cn.cnic.third.service.impl;

import cn.cnic.base.util.HttpUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.stopsComponent.mapper.StopsComponentGroupMapper;
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
    private StopsComponentGroupMapper stopsComponentGroupMapper;

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
    public ThirdStopsComponentVo getStopInfo(String bundleStr) {
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
        @SuppressWarnings("rawtypes")
		Map<String, Class> classMap = new HashMap<>();
        // Key is the name of the List in jsonObj, and the value is a generic class of list
        classMap.put("properties", ThirdStopsComponentPropertyVo.class);
        // Convert a json object to a java object
        thirdStopsComponentVo.setName(jsonObject.getString("name"));
        thirdStopsComponentVo.setBundle(jsonObject.getString("bundle"));
        thirdStopsComponentVo.setOwner(jsonObject.getString("owner"));
        thirdStopsComponentVo.setOwner(jsonObject.getString("owner"));
        thirdStopsComponentVo.setInports(jsonObject.getString("inports"));
        thirdStopsComponentVo.setOutports(jsonObject.getString("outports"));
        thirdStopsComponentVo.setGroups(jsonObject.getString("groups"));
        thirdStopsComponentVo.setCustomized(jsonObject.getBoolean("isCustomized"));
        thirdStopsComponentVo.setDescription(jsonObject.getString("description"));
        thirdStopsComponentVo.setIcon(jsonObject.getString("icon"));
        JSONArray jsonArray = jsonObject.getJSONArray("properties");
        if (null != jsonArray && jsonArray.size() > 0) {
            List<ThirdStopsComponentPropertyVo> thirdStopsComponentPropertyVoList = new ArrayList<>();
            ThirdStopsComponentPropertyVo thirdStopsComponentPropertyVo;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject propertyJsonObject = jsonArray.getJSONObject(i);
                if(null==propertyJsonObject){continue;}
                thirdStopsComponentPropertyVo = new ThirdStopsComponentPropertyVo();
                thirdStopsComponentPropertyVo.setName(propertyJsonObject.getString("name"));
                thirdStopsComponentPropertyVo.setDisplayName(propertyJsonObject.getString("displayName"));
                thirdStopsComponentPropertyVo.setDescription(propertyJsonObject.getString("description"));
                thirdStopsComponentPropertyVo.setDefaultValue(propertyJsonObject.getString("defaultValue"));
                thirdStopsComponentPropertyVo.setAllowableValues(propertyJsonObject.getString("allowableValues"));
                thirdStopsComponentPropertyVo.setRequired(propertyJsonObject.getString("required"));
                thirdStopsComponentPropertyVo.setSensitive(propertyJsonObject.getBoolean("sensitive"));
                thirdStopsComponentPropertyVo.setExample(propertyJsonObject.getString("example"));
            }
            thirdStopsComponentVo.setProperties(thirdStopsComponentPropertyVoList);
        }


        return thirdStopsComponentVo;
    }

}
