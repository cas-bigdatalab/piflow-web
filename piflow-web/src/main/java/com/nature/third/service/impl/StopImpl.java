package com.nature.third.service.impl;

import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.PortType;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.group.model.PropertyTemplate;
import com.nature.component.group.model.StopGroup;
import com.nature.component.group.model.StopsTemplate;
import com.nature.mapper.flow.StopGroupMapper;
import com.nature.third.service.IStop;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class StopImpl implements IStop {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
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
        logger.info("return msg：" + sendGetData);
        if (StringUtils.isNotBlank(sendGetData)) {
            String jsonResult = JSONObject.fromObject(sendGetData).getString("stops");
            //将tops用,号隔开到数组
            stop = jsonResult.split(",");
        }
        return stop;
    }

    @Override
    public StopsTemplate getStopInfo(String bundleStr) {
        StopsTemplate stopsTemplate = null;
        UserVo user = null; //SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        String stopInfo = null;
        if (StringUtils.isNotBlank(bundleStr)) {
            Map<String, String> map = new HashMap<String, String>();
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
                    if(StringUtils.isNotBlank(icon)){
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
                    List<StopGroup> stopGroupByName = stopGroupMapper.getStopGroupByName(list);
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

}
