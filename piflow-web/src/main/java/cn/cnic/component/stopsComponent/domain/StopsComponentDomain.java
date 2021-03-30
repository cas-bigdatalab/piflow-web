package cn.cnic.component.stopsComponent.domain;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.stopsComponent.mapper.StopsComponentGroupMapper;
import cn.cnic.component.stopsComponent.mapper.StopsComponentMapper;
import cn.cnic.component.stopsComponent.mapper.StopsComponentPropertyMapper;
import cn.cnic.component.stopsComponent.model.StopsComponent;
import cn.cnic.component.stopsComponent.model.StopsComponentGroup;
import cn.cnic.component.stopsComponent.model.StopsComponentProperty;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class StopsComponentDomain {

	Logger logger = LoggerUtil.getLogger();

	@Resource
	private StopsComponentMapper stopsComponentMapper;

	@Resource
	private StopsComponentPropertyMapper stopsComponentPropertyMapper;

	@Resource
	private StopsComponentGroupMapper stopsComponentGroupMapper;

	public int addListStopsComponentAndChildren(List<StopsComponent> stopsComponentList) {
		if (null == stopsComponentList || stopsComponentList.size() <= 0) {
			return 0;
		}
		int insertRows = 0;
		for (StopsComponent stopsComponent : stopsComponentList) {
			insertRows += addStopsComponentAndChildren(stopsComponent);
		}
		return insertRows;
	}

	public int addStopsComponentAndChildren(StopsComponent stopsComponent) {
		if (null == stopsComponent) {
			return 0;
		}
		int insertStopsComponentRows = stopsComponentMapper.insertStopsComponent(stopsComponent);
		int insertRows = insertStopsComponentRows;
		if (insertStopsComponentRows > 0) {
			List<StopsComponentProperty> properties = stopsComponent.getProperties();
			if (null == properties || properties.size() <= 0) {
				return insertRows;
			}
			int insertStopsTemplateRows = stopsComponentPropertyMapper.insertStopsComponentProperty(properties);
			insertRows += insertStopsTemplateRows;
		}
		return insertRows;
	}

	public int deleteStopsComponentAndChildren(StopsComponent stopsComponent) {
		if (null == stopsComponent) {
			return 0;
		}
		List<StopsComponentProperty> properties = stopsComponent.getProperties();
		if (null == properties || properties.size() <= 0) {
			return 0;
		}
		int deleteStopsComponentPropertyCount = stopsComponentPropertyMapper.deleteStopsComponentPropertyByStopId(stopsComponent.getId());

		int deleteStopsComponentRows = stopsComponentMapper.deleteStopsComponentById(stopsComponent.getId());

		return deleteStopsComponentRows + deleteStopsComponentPropertyCount;
	}

	public int addListStopsComponent(List<StopsComponent> stopsComponentList) {
		if (null == stopsComponentList || stopsComponentList.size() <= 0) {
			return 0;
		}
		int insertRows = 0;
		for (StopsComponent stopsComponent : stopsComponentList) {
			insertRows += addStopsComponent(stopsComponent);
		}
		return insertRows;
	}

	public int addStopsComponent(StopsComponent stopsComponent) {
		if (null == stopsComponent) {
			return 0;
		}
		return stopsComponentMapper.insertStopsComponent(stopsComponent);
	}

	public int deleteStopsComponent() {
		// empty the "Stop" message and insert
		stopsComponentPropertyMapper.deleteStopsComponentProperty();
		int deleteRows = stopsComponentMapper.deleteStopsComponent();
		return deleteRows;
	}

	public int stopsComponentLinkStopsComponentGroupList(StopsComponent stopsComponent, List<StopsComponentGroup> stopsComponentGroupList) {
		if (null == stopsComponent) {
			return 0;
		}
		if (null == stopsComponentGroupList || stopsComponentGroupList.isEmpty()) {
			return 0;
		}
		int affectedRows = 0;
		for (StopsComponentGroup stopGroup : stopsComponentGroupList) {
			String stopGroupId = stopGroup.getId();
			String stopsTemplateId = stopsComponent.getId();
			int insertAssociationGroupsStopsTemplate = stopsComponentGroupMapper.insertAssociationGroupsStopsTemplate(stopGroupId, stopsTemplateId);
			affectedRows += insertAssociationGroupsStopsTemplate;
			logger.info("association_groups_stops_template Association table insertion affects the number of rows : " + insertAssociationGroupsStopsTemplate);
		}
		return affectedRows;
	}

	public int deleteStopsComponent(StopsComponent stopsComponent) {
		// delete relationship
		stopsComponentGroupMapper.deleteGroupCorrelationByStopId(stopsComponent.getId());
		logger.debug("Successful delete " + stopsComponent.getName() + " 's association!!!");

		// delete stop
		int stopCount = deleteStopsComponentAndChildren(stopsComponent);
		logger.debug("Successful delete " + stopsComponent.getName() + " !!!");

		// delete group
		String[] stopsComponentGroup = stopsComponent.getGroups().split(",");
		List<StopsComponentGroup> stopsComponentGroupList = stopsComponentGroupMapper.getStopGroupByNameList(Arrays.asList(stopsComponentGroup));
		for (StopsComponentGroup sGroup : stopsComponentGroupList) {

			int count = stopsComponentGroupMapper.getGroupStopCount(sGroup.getId());
			if (count == 0) {
				stopsComponentGroupMapper.deleteGroupById(sGroup.getId());
				logger.debug("Successful delete " + stopsComponent.getName() + " group!!!");
			}
		}

		return stopCount;
	}

}
