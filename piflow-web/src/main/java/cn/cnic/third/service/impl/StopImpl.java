package cn.cnic.third.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cnic.third.utils.ThirdInterfaceReturnMsgUtils;
import cn.cnic.third.utils.ThirdStopsComponentUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.third.service.IStop;
import cn.cnic.third.vo.stop.StopsHubVo;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;
import net.sf.json.JSONObject;


@Component
public class StopImpl implements IStop {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Override
    public String[] getAllGroup() {
        String sendGetData = HttpUtils.doGet(SysParamsCache.getStopsGroupsUrl(), null, 30 * 1000);
        logger.debug("Interface return value：" + sendGetData);
        if (ThirdInterfaceReturnMsgUtils.THIRD_INTERFACE_IS_ERROR(sendGetData).equals(ThirdInterfaceReturnMsgUtils.ERROR)) {
            return null;
        }
        String jsonResult = JSONObject.fromObject(sendGetData).getString("groups");
        if (StringUtils.isBlank(jsonResult)) {
        	return null;
        }
        String[] group = jsonResult.split(",");
        return group;
    }

    @Override
    public String[] getAllStops() {
        String[] stop = null;
        String sendGetData = HttpUtils.doGet(SysParamsCache.getStopsListUrl(), null, 30 * 1000);
        logger.debug("Interface return value：" + sendGetData);
        if (ThirdInterfaceReturnMsgUtils.THIRD_INTERFACE_IS_ERROR(sendGetData).equals(ThirdInterfaceReturnMsgUtils.ERROR)) {
            return null;
        }
        String jsonResult = JSONObject.fromObject(sendGetData).getString("stops");
        if (StringUtils.isBlank(jsonResult)) {
        	return null;
        }
        //Separate the tops from the array with the, sign
        stop = jsonResult.split(",");
        return stop;
    }

    @Override
    public Map<String, List<String>> getStopsListWithGroup() {
        String sendGetData = HttpUtils.doGet(SysParamsCache.getStopsListWithGroupUrl(), null, 30 * 1000);
        logger.debug("Interface return value：" + sendGetData);
        if (ThirdInterfaceReturnMsgUtils.THIRD_INTERFACE_IS_ERROR(sendGetData).equals(ThirdInterfaceReturnMsgUtils.ERROR)) {
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
        logger.info("Interface return value：" + sendGetData);
        if (ThirdInterfaceReturnMsgUtils.THIRD_INTERFACE_IS_ERROR(sendGetData).equals(ThirdInterfaceReturnMsgUtils.ERROR)) {
            return null;
        }

        JSONObject jsonObject = JSONObject.fromObject(sendGetData).getJSONObject("StopInfo");
        ThirdStopsComponentVo thirdStopsComponentVo = ThirdStopsComponentUtils.constructThirdStopsComponentVo(jsonObject);
        return thirdStopsComponentVo;

    }



    @Override
    public String getStopsHubPath() {

        Map<String, String> map = new HashMap<>();
        //map.put("bundle", bundleStr);
        String sendGetData = HttpUtils.doGet(SysParamsCache.getStopsHubPathUrl(), map, 30 * 1000);
        logger.debug("Interface return value：" + sendGetData);
        if (ThirdInterfaceReturnMsgUtils.THIRD_INTERFACE_IS_ERROR(sendGetData).equals(ThirdInterfaceReturnMsgUtils.ERROR)) {
            return null;
        }

        String pluginPath = JSONObject.fromObject(sendGetData).getString("pluginPath");
        return pluginPath;
    }

    @Override
    public StopsHubVo mountStopsHub(String stopsHubName) {
        Map<String, String> map = new HashMap<>();
        map.put("plugin", stopsHubName);
        String json = JSONObject.fromObject(map).toString();
        String doPost = HttpUtils.doPost(SysParamsCache.getStopsHubMountUrl(), json, 5 * 1000);
        logger.debug("Interface return value: " + doPost);
        if (ThirdInterfaceReturnMsgUtils.THIRD_INTERFACE_IS_ERROR(doPost).equals(ThirdInterfaceReturnMsgUtils.ERROR)) {
            return null;
        }
        StopsHubVo stopsHubVo = ThirdStopsComponentUtils.constructStopsHubVo(JSONObject.fromObject(doPost));
        return stopsHubVo;

    }

    @Override
    public StopsHubVo unmountStopsHub(String stopsHubMountId) {

        //String stopsHubMountId = "";
        Map<String, String> map = new HashMap<>();
        map.put("pluginId", stopsHubMountId);
        String json = JSONObject.fromObject(map).toString();
        String doPost = HttpUtils.doPost(SysParamsCache.getStopsHubUNMountUrl(), json, 5 * 1000);
        logger.info("Interface return value: " + doPost);
        if (ThirdInterfaceReturnMsgUtils.THIRD_INTERFACE_IS_ERROR(doPost).equals(ThirdInterfaceReturnMsgUtils.ERROR)) {
            return null;
        }

        StopsHubVo stopsHubVo = ThirdStopsComponentUtils.constructStopsHubVo(JSONObject.fromObject(doPost));
        return stopsHubVo;
    }



}
