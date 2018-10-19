package com.nature.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.nature.ApplicationTests;
import com.nature.base.util.HttpClientStop;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.PropertyTemplate;
import com.nature.component.workFlow.model.StopGroup;
import com.nature.component.workFlow.model.StopsTemplate;

public class StopsTemplateMapperTest extends ApplicationTests {

	@Autowired
	private StopsTemplateMapper stopsTemplateMapper;
	@Autowired
	private StopGroupMapper stopGroupMapper;
	@Autowired
	private PropertyTemplateMapper propertyTemplateMapper;
	

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetStopsTemplateById() {
		StopsTemplate stopsTemplate = stopsTemplateMapper.getStopsTemplateById("fbb42f0d8ca14a83bfab13e0ba2d7293");
		if (null == stopsTemplate) {
			logger.info("查询结果为空");
			stopsTemplate = new StopsTemplate();
		}
		logger.info(stopsTemplate.toString());
	}

	@Test
	public void testGetStopsPropertyById() {
		StopsTemplate stopsTemplate = stopsTemplateMapper.getStopsPropertyById("fbb42f0d8ca14a83bfab13e0ba2d7293");
		if (null == stopsTemplate) {
			logger.info("查询结果为空");
			stopsTemplate = new StopsTemplate();
		}
		logger.info(stopsTemplate.toString());
	}

	@Test
	public void testGetStopsTemplateListByGroupId() {
		List<StopsTemplate> stopsTemplateList = stopsTemplateMapper
				.getStopsTemplateListByGroupId("fbb42f0d8ca14a83bfab13e0ba2d7290");
		if (null == stopsTemplateList) {
			logger.info("查询结果为空");
		}
		logger.info(stopsTemplateList.size() + "");
	}
	
	@Test
	@Transactional
	@Rollback(value = false)
	public void testAddFlow() {
		List<StopsTemplate> list = new ArrayList<>();
		List<PropertyTemplate> listPropertyTemplate = new ArrayList<>();
		//先插入主表stopGroup,再通过id来进行关联插入
		StopGroup stopGroup = new StopGroup();
		stopGroup.setId(Utils.getUUID32());
		stopGroup.setCrtDttm(new Date());
		stopGroup.setCrtUser("Nature");
		stopGroup.setLastUpdateUser("Nature");
		stopGroup.setEnableFlag(true);
		stopGroup.setLastUpdateDttm(new Date());
		stopGroup.setVersion(0L);
		stopGroup.setGroupName("new stopGroupName");
		int insertStopGroup = stopGroupMapper.insertStopGroup(stopGroup);
		System.out.println("flow_sotps_groups表插入影响行数："+insertStopGroup);
		StopsTemplate stopsTemplate = new StopsTemplate();
		stopsTemplate.setId(Utils.getUUID32());
		stopsTemplate.setCrtDttm(new Date());
		stopsTemplate.setCrtUser("wdd");
		stopsTemplate.setEnableFlag(true);
		stopsTemplate.setLastUpdateUser("Nature");
		stopsTemplate.setLastUpdateDttm(new Date());
		stopsTemplate.setVersion(0L);
		stopsTemplate.setBundel("111");
		stopsTemplate.setDescription("miaosu");
		stopsTemplate.setGroups("group");
		stopsTemplate.setName("nameaa");
		stopsTemplate.setNumberOfEntrances(112);
		stopsTemplate.setNumberOfExports(110);
		stopsTemplate.setOwner("ow");
		stopsTemplate.setStopGroup(stopGroup.getId());
		StopsTemplate stopsTemplate2 = new StopsTemplate();
		stopsTemplate2.setId(Utils.getUUID32());
		stopsTemplate2.setCrtDttm(new Date());
		stopsTemplate2.setCrtUser("wdd");
		stopsTemplate2.setEnableFlag(true);
		stopsTemplate2.setLastUpdateUser("Nature");
		stopsTemplate2.setLastUpdateDttm(new Date());
		stopsTemplate2.setVersion(0L);
		stopsTemplate2.setBundel("111");
		stopsTemplate2.setDescription("miaosu");
		stopsTemplate2.setGroups("group");
		stopsTemplate2.setName("nameaa");
		stopsTemplate2.setNumberOfEntrances(112);
		stopsTemplate2.setNumberOfExports(110);
		stopsTemplate2.setOwner("ow");
		stopsTemplate2.setStopGroup(stopGroup.getId());
		list.add(stopsTemplate);
		list.add(stopsTemplate2);
		int insertStopsTemplate = stopsTemplateMapper.insertStopsTemplate(list);
		System.out.println("flow_stops_template表插入影响行数："+insertStopsTemplate);
		stopGroup.setStopsTemplateList(list);
		
		System.out.println("=============================association_groups_stops_template=====start==================");
		for (StopsTemplate zjb : list) {
			String stopGroupId = stopGroup.getId();
			String stopsTemplateId = zjb.getId();
			int insertAssociationGroupsStopsTemplate = stopGroupMapper.insertAssociationGroupsStopsTemplate(stopGroupId, stopsTemplateId);
			System.out.println("association_groups_stops_template关联表插入影响行数："+insertAssociationGroupsStopsTemplate);
		}
		System.out.println("=============================association_groups_stops_template===========stop============");
		for (StopsTemplate stopsTemplate3 : list) {
			PropertyTemplate PropertyTemplate = new PropertyTemplate();
			PropertyTemplate.setId(Utils.getUUID32());
			PropertyTemplate.setCrtDttm(new Date());
			PropertyTemplate.setCrtUser("wdd");
			PropertyTemplate.setEnableFlag(true);
			PropertyTemplate.setLastUpdateUser("Nature");
			PropertyTemplate.setLastUpdateDttm(new Date());
			PropertyTemplate.setVersion(0L);
			PropertyTemplate.setDefaultValue("defaultValue");
			PropertyTemplate.setAllowableValues("123");
			PropertyTemplate.setDescription("description");
			PropertyTemplate.setDisplayName("displayName");
			PropertyTemplate.setName("nameas");
			PropertyTemplate.setRequired(false);
			PropertyTemplate.setSensitive(true);
			PropertyTemplate.setStopsTemplate(stopsTemplate3.getId());
			PropertyTemplate PropertyTemplate1 = new PropertyTemplate();
			PropertyTemplate1.setId(Utils.getUUID32());
			PropertyTemplate1.setCrtDttm(new Date());
			PropertyTemplate1.setCrtUser("wdd");
			PropertyTemplate1.setEnableFlag(true);
			PropertyTemplate1.setLastUpdateUser("Nature");
			PropertyTemplate1.setLastUpdateDttm(new Date());
			PropertyTemplate1.setVersion(0L);
			PropertyTemplate1.setDefaultValue("defaultValue");
			PropertyTemplate1.setAllowableValues("123");
			PropertyTemplate1.setDescription("description");
			PropertyTemplate1.setDisplayName("displayName");
			PropertyTemplate1.setName("nameas");
			PropertyTemplate1.setRequired(false);
			PropertyTemplate1.setSensitive(true);
			PropertyTemplate1.setStopsTemplate(stopsTemplate3.getId());
			listPropertyTemplate.add(PropertyTemplate1);
			listPropertyTemplate.add(PropertyTemplate);
		}
		int insertPropertyTemplate = propertyTemplateMapper.insertPropertyTemplate(listPropertyTemplate);
		System.out.println("flow_stops_property_template表插入影响行数："+insertPropertyTemplate);
	}
	
	/**
	 * 调用getAllGroups并保存group
	 */
	@Test
	public void getStopGroupAndSave() {
		HttpClientStop stop = new HttpClientStop();
		String stopGroupInfo = stop.getAllGroupsAndStops("groups");
		System.out.println(stopGroupInfo);
		String[] group = stopGroupInfo.split(",");
		System.out.println(group.length-1);
		int a = 0;
		for (String string : group) {
			if (string.length()>0) {
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
			a+=insertStopGroup;
			}
		}
		System.out.println("成功插入Group"+a+"条数据！！！");
	}
	
	@Test
	@Transactional
	@Rollback(value = false)
	public void saveStopsAndProperty(){
		// 1.先调stop接口获取getAllStops数据；
		HttpClientStop stops = new HttpClientStop();
		String stopList = stops.getAllGroupsAndStops("stops");
		System.out.println(stopList);
		String[] stop = stopList.split(",");
		int num = 0;
		System.out.println(stop.length);
		for (String stopListInfos : stop) {
			num++;
			// 2.先根据bundle查询stopInfo
			System.out.println("现在调用的是："+stopListInfos);
		HashMap<String, Object> stopInfo = stops.getStopInfo(stopListInfos);
		if (!stopInfo.isEmpty()) {
			List<String> list = new ArrayList<>();
			List<PropertyTemplate> listPropertyTemplate = new ArrayList<>();
			List<StopsTemplate> listStopsTemplate = new ArrayList<>();
			System.out.println(stopInfo.size());
			String bundle = stopInfo.get("bundle").toString();
			String groups = stopInfo.get("groups").toString();
			String owner = stopInfo.get("owner").toString();
			String inports = stopInfo.get("inports").toString();
			String outports = stopInfo.get("outports").toString();
			/*int outportCount = 0;
			int inportCount = 0;
			if(stopInfo.get("inportCount").toString().equals("") && !stopInfo.get("inportCount").toString().equals("null")){
				inportCount = Integer.parseInt(stopInfo.get("inportCount").toString());
			}
			if(stopInfo.get("outportCount").toString().equals("") && !stopInfo.get("outportCount").toString().equals("null")){
				outportCount = Integer.parseInt(stopInfo.get("outportCount").toString());
			}*/
			String properties = stopInfo.get("properties").toString();
			String name = stopInfo.get("name").toString();
			String description = stopInfo.get("description").toString();
			System.out.println(groups + "&");
			System.out.println(stopInfo);
			//一个stops存在有多个group,所以此处需要处理一下
			if (groups.contains(",")) {
				String[] split = groups.split(",");
				for (int i = 0; i < split.length; i++) {
					if (!"".equals(split[i]) && split[i] != null)
						list.add(split[i]);
				}
			} else {
				list.add(groups);
			}
			//根据stops中的groupName查询Group信息
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
				//stopsTemplate.setNumberOfEntrances(inportCount);
				//stopsTemplate.setNumberOfExports(outportCount);
				stopsTemplate.setInports(inports);
				stopsTemplate.setOutports(outports);
				stopsTemplate.setOwner(owner);
				stopsTemplate.setStopGroup(stopGroup.getId());
				listStopsTemplate.add(stopsTemplate);
			}
			int insertStopsTemplate = stopsTemplateMapper.insertStopsTemplate(listStopsTemplate);
			System.out.println("flow_stops_template表插入影响行数："+insertStopsTemplate);
			
			System.out.println("=============================association_groups_stops_template=====start==================");
			for (StopsTemplate zjb : listStopsTemplate) {
				String stopGroupId = zjb.getStopGroup();
				String stopsTemplateId = zjb.getId();
				int insertAssociationGroupsStopsTemplate = stopGroupMapper.insertAssociationGroupsStopsTemplate(stopGroupId, stopsTemplateId);
				System.out.println("association_groups_stops_template关联表插入影响行数："+insertAssociationGroupsStopsTemplate);
			}
			JSONArray jsonArray = JSONArray.fromObject(properties);
			for (StopsTemplate zjb : listStopsTemplate) {
				for(int i = 0 ;i < jsonArray.size() ; i++){
				    PropertyTemplate PropertyTemplate = new PropertyTemplate();
					PropertyTemplate.setId(Utils.getUUID32());
					PropertyTemplate.setCrtDttm(new Date());
					PropertyTemplate.setCrtUser("wdd");
					PropertyTemplate.setEnableFlag(true);
					PropertyTemplate.setLastUpdateUser("wdd");
					PropertyTemplate.setLastUpdateDttm(new Date());
					PropertyTemplate.setVersion(0L);
					PropertyTemplate.setDefaultValue(jsonArray.getJSONObject(i).getString("defaultValue"));
					PropertyTemplate.setAllowableValues(jsonArray.getJSONObject(i).getString("allowableValues"));
					PropertyTemplate.setDescription(jsonArray.getJSONObject(i).getString("description"));
					PropertyTemplate.setDisplayName(jsonArray.getJSONObject(i).getString("displayName"));
					PropertyTemplate.setName(jsonArray.getJSONObject(i).getString("name"));
					PropertyTemplate.setRequired(jsonArray.getJSONObject(i).getString("required").equals("true")?true:false);
					PropertyTemplate.setSensitive(jsonArray.getJSONObject(i).getString("sensitive").equals("true")?true:false);
					PropertyTemplate.setStopsTemplate(zjb.getId());
					listPropertyTemplate.add(PropertyTemplate);
				}
			}
				int insertPropertyTemplate = propertyTemplateMapper.insertPropertyTemplate(listPropertyTemplate);
				System.out.println("flow_stops_property_template表插入影响行数："+insertPropertyTemplate);
			}
		}
		System.out.println(num+"次数");
	}
		
}
