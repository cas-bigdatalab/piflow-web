package com.nature.mapper;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.stopsComponent.model.PropertyTemplate;
import com.nature.component.stopsComponent.model.StopGroup;
import com.nature.component.stopsComponent.model.StopsTemplate;
import com.nature.mapper.stopsComponent.PropertyTemplateMapper;
import com.nature.mapper.stopsComponent.StopGroupMapper;
import com.nature.mapper.stopsComponent.StopsTemplateMapper;
import com.nature.third.service.IStop;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StopsTemplateMapperTest extends ApplicationTests {

    @Autowired
    private StopsTemplateMapper stopsTemplateMapper;
    @Autowired
    private StopGroupMapper stopGroupMapper;
    @Autowired
    private PropertyTemplateMapper propertyTemplateMapper;

    @Autowired
    private IStop stopImpl;

    Logger logger = LoggerUtil.getLogger();

    @Test
    public void testGetStopsTemplateById() {
        StopsTemplate stopsTemplate = stopsTemplateMapper.getStopsTemplateById("fbb42f0d8ca14a83bfab13e0ba2d7293");
        if (null == stopsTemplate) {
            logger.info("The query result is empty");
            stopsTemplate = new StopsTemplate();
        }
        logger.info(stopsTemplate.toString());
    }

    @Test
    public void testGetStopsPropertyById() {
        StopsTemplate stopsTemplate = stopsTemplateMapper.getStopsTemplateAndPropertyById("fbb42f0d8ca14a83bfab13e0ba2d7293");
        if (null == stopsTemplate) {
            logger.info("The query result is empty");
            stopsTemplate = new StopsTemplate();
        }
        logger.info(stopsTemplate.toString());
    }

    @Test
    public void testGetStopsTemplateListByGroupId() {
        List<StopsTemplate> stopsTemplateList = stopsTemplateMapper
                .getStopsTemplateListByGroupId("fbb42f0d8ca14a83bfab13e0ba2d7290");
        if (null == stopsTemplateList) {
            logger.info("The query result is empty");
        }
        logger.info(stopsTemplateList.size() + "");
    }

    /**
     * Call getAllGroups and save the group
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void getStopGroupAndSave() {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";

        String[] group = stopImpl.getAllGroup();
		if (null != group && group.length > 0) {
			// The call is successful, the group table information is cleared and then inserted.
			stopGroupMapper.deleteGroupCorrelation();
			int deleteGroup = stopGroupMapper.deleteGroup();
			logger.debug("Group" + deleteGroup + "data was successfully deleted！！！");
			int a = 0;
			for (String string : group) {
				if (string.length() > 0) {
					StopGroup stopGroup = new StopGroup();
					stopGroup.setId(SqlUtils.getUUID32());
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
			logger.debug("Group" + a + "data was successfully inserted！！！");
		}

    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void saveStopsAndProperty() {
		// 1.First call the stop interface to get the getAllStops data；
		String[] stopNameList = stopImpl.getAllStops();
		if (null != stopNameList && stopNameList.length > 0) {
			// The call is successful and the Stop message is cleared before insertion
			stopGroupMapper.deleteStopsPropertyInfo();
			int deleteStopsInfo = stopGroupMapper.deleteStopsInfo();
			logger.info("Successful deletion StopsInfo" + deleteStopsInfo + "piece of data!!!");
			int num = 0;
			for (String stopListInfos : stopNameList) {
				num++;
				// 2.Start by querting stopInfo against the bundle
				logger.info("Now the call is：" + stopListInfos);
				StopsTemplate stopsTemplate = stopImpl.getStopInfo(stopListInfos);
				if (null != stopsTemplate) {
					List<StopsTemplate> listStopsTemplate = new ArrayList<>();
					listStopsTemplate.add(stopsTemplate);
					int insertStopsTemplate = stopsTemplateMapper.insertStopsTemplate(listStopsTemplate);
					logger.info("flow_stops_template affects the number of rows : " + insertStopsTemplate);
					logger.info("=============================association_groups_stops_template=====start==================");
					List<StopGroup> stopGroupList = stopsTemplate.getStopGroupList();
					for (StopGroup stopGroup : stopGroupList) {
						String stopGroupId = stopGroup.getId();
						String stopsTemplateId = stopsTemplate.getId();
						int insertAssociationGroupsStopsTemplate = stopGroupMapper.insertAssociationGroupsStopsTemplate(stopGroupId, stopsTemplateId);
						logger.info("association_groups_stops_template Association table insertion affects the number of rows : " + insertAssociationGroupsStopsTemplate);
					}
					List<PropertyTemplate> properties = stopsTemplate.getProperties();
					int insertPropertyTemplate = propertyTemplateMapper.insertPropertyTemplate(properties);
					logger.info("flow_stops_property_template affects the number of rows : " + insertPropertyTemplate);
				}
			}
			logger.info(num + "num");
		}
    }

}
