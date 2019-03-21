package com.nature.third.service;

import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.PortType;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.flow.model.PropertyTemplate;
import com.nature.component.flow.model.StopGroup;
import com.nature.component.flow.model.StopsTemplate;
import com.nature.mapper.PropertyTemplateMapper;
import com.nature.mapper.StopGroupMapper;
import com.nature.mapper.StopsTemplateMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
@Transactional
public class GetGroupsAndStops {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private StopsTemplateMapper stopsTemplateMapper;
    @Autowired
    private StopGroupMapper stopGroupMapper;
    @Autowired
    private PropertyTemplateMapper propertyTemplateMapper;

    /**
     * 调接口添加group和stops关联添加
     */
    @Transactional
    public void addGroupAndStopsList(UserVo user) {
        getGroupAndSave(user);
        getStopsAndProperty(user);
    }

    /**
     * 调用getAllGroups并保存
     */
    @Transactional
    public void getGroupAndSave(UserVo user) {
        String username = (null != user) ? user.getUsername() : "-1";
        // 先清空Group表信息再插入
        int deleteGroup = stopGroupMapper.deleteGroup();
        System.out.println("成功删除Group" + deleteGroup + "条数据！！！");
        HttpClientStop stop = new HttpClientStop();
        String stopGroupInfo = stop.getGroupAndStopInfo("", SysParamsCache.STOP_GROUPS_URL());
        String jsonResult = "";
        if (StringUtils.isNotBlank(stopGroupInfo)) {
            jsonResult = JSONObject.fromObject(stopGroupInfo).getString("groups");
        }
        String[] group = jsonResult.split(",");
        int a = 0;
        for (String string : group) {
            if (string.length() > 0) {
                StopGroup stopGroup = new StopGroup();
                stopGroup.setId(Utils.getUUID32());
                stopGroup.setCrtDttm(new Date());
                stopGroup.setCrtUser(username);
                stopGroup.setLastUpdateUser(username);
                stopGroup.setEnableFlag(true);
                stopGroup.setLastUpdateDttm(new Date());
                stopGroup.setGroupName(string);
                int insertStopGroup = stopGroupMapper.insertStopGroup(stopGroup);
                a += insertStopGroup;
            }
        }
        System.out.println("成功插入Group" + a + "条数据！！！");
    }

    /**
     * 调用getAllStops与Group进行管理，并保存stop属性信息
     */
    @Transactional
    public void getStopsAndProperty(UserVo user) {
        String username = (null != user) ? user.getUsername() : "-1";
        int deleteStopsInfo = stopGroupMapper.deleteStopsInfo();
        logger.info("成功删除StopsInfo" + deleteStopsInfo + "条数据！！！");
        // 1.先调stop接口获取getAllStops数据；
        HttpClientStop httpClientStop = new HttpClientStop();
        String stopList = httpClientStop.getGroupAndStopInfo("", SysParamsCache.STOP_LIST_URL());
        String jsonResult = JSONObject.fromObject(stopList).getString("stops");
        String[] stopNameList = jsonResult.split(",");
        int num = 0;
        for (String stopListInfos : stopNameList) {
            num++;
            // 2.先根据bundle查询stopInfo
            logger.info("现在调用的是：" + stopListInfos);
            String stopInfo = httpClientStop.getGroupAndStopInfo(stopListInfos, SysParamsCache.STOP_INFO_URL());
            if (stopInfo.contains("Error")) {
                continue;
            }
            if (StringUtils.isNotBlank(stopInfo)) {
                List<String> list = new ArrayList<>();
                List<PropertyTemplate> listPropertyTemplate = new ArrayList<>();
                List<StopsTemplate> listStopsTemplate = new ArrayList<>();
                JSONObject jb1 = JSONObject.fromObject(stopInfo);
                JSONArray ja = JSONArray.fromObject(jb1.get("StopInfo"));
                @SuppressWarnings("unchecked")
                Iterator<Object> it = ja.iterator();
                while (it.hasNext()) {
                    JSONObject ob = (JSONObject) it.next();
                    String bundle = ob.get("bundle") + "";
                    String owner = ob.get("owner") + "";
                    String groups = ob.get("groups") + "";
                    String properties = ob.get("properties") + "";
                    String name = ob.get("name") + "";
                    String description = ob.get("description") + "";
                    String icon = ob.get("icon") + "";
                    String inports = ob.get("inports") + "";
                    PortType inPortType = null;
                    if (StringUtils.isNotBlank(inports)) {
                        inPortType = null;
                        for (PortType value : PortType.values()) {
                            if (inports.equalsIgnoreCase(value.getValue())) {
                                inPortType = value;
                            }
                        }
                        if(null == inPortType){
                            inPortType = PortType.USER_DEFAULT;
                        }
                    }
                    PortType.selectGenderByValue(inports);
                    String outports = ob.get("outports") + "";
                    PortType outPortType = null;
                    if (StringUtils.isNotBlank(outports)) {
                        outPortType = null;
                        for (PortType value : PortType.values()) {
                            if (outports.equalsIgnoreCase(value.getValue())) {
                                outPortType = value;
                            }
                        }
                        if(null == outPortType){
                            outPortType = PortType.USER_DEFAULT;
                        }
                    }
                    ImageUtils.generateImage(icon, name + "_128x128", "png", SysParamsCache.IMAGES_PATH);
                    // 一个stops存在有多个group,所以此处需要处理一下
                    if (groups.contains(",")) {
                        String[] split = groups.split(",");
                        for (int i = 0; i < split.length; i++) {
                            if (!"".equals(split[i]) && split[i] != null)
                                list.add(split[i]);
                        }
                    } else {
                        list.add(groups);
                    }
                    // 根据stops中的groupName查询Group信息
                    List<StopGroup> stopGroupByName = stopGroupMapper.getStopGroupByName(list);
                    StopsTemplate stopsTemplate = new StopsTemplate();
                    stopsTemplate.setId(Utils.getUUID32());
                    stopsTemplate.setCrtDttm(new Date());
                    stopsTemplate.setCrtUser(username);
                    stopsTemplate.setEnableFlag(true);
                    stopsTemplate.setLastUpdateUser(username);
                    stopsTemplate.setLastUpdateDttm(new Date());
                    stopsTemplate.setBundel(bundle);
                    stopsTemplate.setDescription(Utils.replaceString(description));
                    stopsTemplate.setGroups(Utils.replaceString(groups));
                    stopsTemplate.setName(Utils.replaceString(name));
                    stopsTemplate.setInports(inports);
                    stopsTemplate.setInPortType(inPortType);
                    stopsTemplate.setOutports(outports);
                    stopsTemplate.setOutPortType(outPortType);
                    stopsTemplate.setOwner(owner);
                    listStopsTemplate.add(stopsTemplate);
                    int insertStopsTemplate = stopsTemplateMapper.insertStopsTemplate(listStopsTemplate);
                    logger.info("flow_stops_template表插入影响行数：" + insertStopsTemplate);
                    logger.info("=============================association_groups_stops_template=====start==================");
                    for (StopGroup stopGroup : stopGroupByName) {
                        String stopGroupId = stopGroup.getId();
                        String stopsTemplateId = stopsTemplate.getId();
                        int insertAssociationGroupsStopsTemplate = stopGroupMapper.insertAssociationGroupsStopsTemplate(stopGroupId, stopsTemplateId);
                        logger.info("association_groups_stops_template关联表插入影响行数：" + insertAssociationGroupsStopsTemplate);
                    }
                    JSONArray jsonArray = JSONArray.fromObject(properties);
                    if (null != jsonArray && !jsonArray.isEmpty()) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            PropertyTemplate PropertyTemplate = new PropertyTemplate();
                            PropertyTemplate.setId(Utils.getUUID32());
                            PropertyTemplate.setCrtDttm(new Date());
                            PropertyTemplate.setCrtUser(username);
                            PropertyTemplate.setEnableFlag(true);
                            PropertyTemplate.setLastUpdateUser(username);
                            PropertyTemplate.setLastUpdateDttm(new Date());
                            PropertyTemplate.setDefaultValue(Utils.replaceString(jsonArray.getJSONObject(i).getString("defaultValue")));
                            PropertyTemplate.setAllowableValues(Utils.replaceString(jsonArray.getJSONObject(i).getString("allowableValues")));
                            PropertyTemplate.setDescription(Utils.replaceString(jsonArray.getJSONObject(i).getString("description")));
                            PropertyTemplate.setDisplayName(Utils.replaceString(jsonArray.getJSONObject(i).getString("displayName")));
                            PropertyTemplate.setName(Utils.replaceString(jsonArray.getJSONObject(i).getString("name")));
                            PropertyTemplate.setRequired(jsonArray.getJSONObject(i).getString("required").equals("true") ? true : false);
                            PropertyTemplate.setSensitive(jsonArray.getJSONObject(i).getString("sensitive").equals("true") ? true : false);
                            PropertyTemplate.setStopsTemplate(stopsTemplate.getId());
                            listPropertyTemplate.add(PropertyTemplate);
                        }
                    }
                    int insertPropertyTemplate = propertyTemplateMapper.insertPropertyTemplate(listPropertyTemplate);
                    logger.info("flow_stops_property_template表插入影响行数：" + insertPropertyTemplate);
                }
            }
        }
        logger.info(num + "次数");
    }

}
