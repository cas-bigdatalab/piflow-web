package com.nature.third.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nature.base.util.HttpClientStop;
import com.nature.base.util.ImageUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.workFlow.model.PropertyTemplate;
import com.nature.component.workFlow.model.StopGroup;
import com.nature.component.workFlow.model.StopsTemplate;
import com.nature.mapper.PropertyTemplateMapper;
import com.nature.mapper.StopGroupMapper;
import com.nature.mapper.StopsTemplateMapper;

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
	public void addGroupAndStopsList() {
		getGroupAndSave();
		getStopsAndProperty();
	}

	/**
	 * 调用getAllGroups并保存
	 */
	@RequestMapping("/save")
	@Transactional
	public void getGroupAndSave() {
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
				stopGroup.setCrtUser("Nature");
				stopGroup.setLastUpdateUser("Nature");
				stopGroup.setEnableFlag(true);
				stopGroup.setLastUpdateDttm(new Date());
				stopGroup.setVersion(0L);
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
	@RequestMapping("/saveStopsAndProperty")
	@Transactional
	private void getStopsAndProperty() {
		// 先清空所有stop和stop属性表，重新插入
		int deleteStopsInfo = stopGroupMapper.deleteStopsInfo();
		System.out.println("成功删除StopsInfo" + deleteStopsInfo + "条数据！！！");
		// 1.先调stop接口获取getAllStops数据；
		HttpClientStop stops = new HttpClientStop();
		String stopList = stops.getGroupAndStopInfo("", SysParamsCache.STOP_LIST_URL());
		String jsonResult = JSONObject.fromObject(stopList).getString("stops");
		String[] stop = jsonResult.split(",");
		int num = 0;
		for (String stopListInfos : stop) {
			num++;
			// 2.先根据bundle查询stopInfo
			System.out.println("现在调用的是：" + stopListInfos);
			String stopInfo = stops.getGroupAndStopInfo(stopListInfos, SysParamsCache.STOP_INFO_URL());
			if (stopInfo.contains("Error")) {
				continue;
			}
			if (!stopInfo.isEmpty()) {
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
					String inports = ob.get("inports") + "";
					String outports = ob.get("outports") + "";
					String groups = ob.get("groups") + "";
					String properties = ob.get("properties") + "";
					String name = ob.get("name") + "";
					String description = ob.get("description") + "";
					String icon = ob.get("icon") + "";
					ImageUtils.generateImage(icon, name);
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
					for (StopGroup stopGroup : stopGroupByName) {
						StopsTemplate stopsTemplate = new StopsTemplate();
						stopsTemplate.setId(Utils.getUUID32());
						stopsTemplate.setCrtDttm(new Date());
						stopsTemplate.setCrtUser("wdd");
						stopsTemplate.setEnableFlag(true);
						stopsTemplate.setLastUpdateUser("Nature");
						stopsTemplate.setLastUpdateDttm(new Date());
						stopsTemplate.setVersion(0L);
						stopsTemplate.setBundel(bundle);
						stopsTemplate.setDescription(description);
						stopsTemplate.setGroups(groups);
						stopsTemplate.setName(name);
						stopsTemplate.setInports(inports);
						stopsTemplate.setOutports(outports);
						stopsTemplate.setOwner(owner);
						stopsTemplate.setStopGroup(stopGroup.getId());
						listStopsTemplate.add(stopsTemplate);
					}
					int insertStopsTemplate = stopsTemplateMapper.insertStopsTemplate(listStopsTemplate);
					System.out.println("flow_stops_template表插入影响行数：" + insertStopsTemplate);
					System.out.println(
							"=============================association_groups_stops_template=====start==================");
					for (StopsTemplate zjb : listStopsTemplate) {
						String stopGroupId = zjb.getStopGroup();
						String stopsTemplateId = zjb.getId();
						int insertAssociationGroupsStopsTemplate = stopGroupMapper
								.insertAssociationGroupsStopsTemplate(stopGroupId, stopsTemplateId);
						System.out.println(
								"association_groups_stops_template关联表插入影响行数：" + insertAssociationGroupsStopsTemplate);
					}
					JSONArray jsonArray = JSONArray.fromObject(properties);
					for (StopsTemplate zjb : listStopsTemplate) {
						for (int i = 0; i < jsonArray.size(); i++) {
							PropertyTemplate PropertyTemplate = new PropertyTemplate();
							PropertyTemplate.setId(Utils.getUUID32());
							PropertyTemplate.setCrtDttm(new Date());
							PropertyTemplate.setCrtUser("wdd");
							PropertyTemplate.setEnableFlag(true);
							PropertyTemplate.setLastUpdateUser("wdd");
							PropertyTemplate.setLastUpdateDttm(new Date());
							PropertyTemplate.setVersion(0L);
							PropertyTemplate.setDefaultValue(jsonArray.getJSONObject(i).getString("defaultValue"));
							PropertyTemplate
									.setAllowableValues(jsonArray.getJSONObject(i).getString("allowableValues"));
							PropertyTemplate.setDescription(jsonArray.getJSONObject(i).getString("description"));
							PropertyTemplate.setDisplayName(jsonArray.getJSONObject(i).getString("displayName"));
							PropertyTemplate.setName(jsonArray.getJSONObject(i).getString("name"));
							PropertyTemplate.setRequired(
									jsonArray.getJSONObject(i).getString("required").equals("true") ? true : false);
							PropertyTemplate.setSensitive(
									jsonArray.getJSONObject(i).getString("sensitive").equals("true") ? true : false);
							PropertyTemplate.setStopsTemplate(zjb.getId());
							listPropertyTemplate.add(PropertyTemplate);
						}
					}
					int insertPropertyTemplate = propertyTemplateMapper.insertPropertyTemplate(listPropertyTemplate);
					System.out.println("flow_stops_property_template表插入影响行数：" + insertPropertyTemplate);
				}
			}
		}
		System.out.println(num + "次数");

	}

}
