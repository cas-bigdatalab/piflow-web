package cn.cnic.component.stopsComponent.domain;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.stopsComponent.mapper.StopsComponentGroupMapper;
import cn.cnic.component.stopsComponent.model.StopsComponent;
import cn.cnic.component.stopsComponent.model.StopsComponentGroup;
import cn.cnic.component.stopsComponent.vo.StopsComponentGroupVo;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class StopsComponentGroupDomain {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private StopsComponentGroupMapper stopsComponentGroupMapper;

    @Resource
    private StopsComponentDomain stopsComponentDomain;

    public int addStopsComponentGroupAndChildren(StopsComponentGroup stopsComponentGroup) {
        if (null == stopsComponentGroup) {
            return 0;
        }
        int insertStopsComponentGroupRows = stopsComponentGroupMapper.insertStopGroup(stopsComponentGroup);
        int affectedRows = insertStopsComponentGroupRows;
        if (insertStopsComponentGroupRows > 0) {
            List<StopsComponent> stopsComponentList = stopsComponentGroup.getStopsComponentList();
            if (null == stopsComponentList || stopsComponentList.size() <= 0) {
                return affectedRows;
            }
            int insertStopsTemplateRows = stopsComponentDomain.addListStopsComponentAndChildren(stopsComponentList);
            affectedRows = insertStopsComponentGroupRows + insertStopsTemplateRows;
        }
        return affectedRows;
    }

    public int addStopsComponentGroup(StopsComponentGroup stopsComponentGroup) {
        if (null == stopsComponentGroup) {
            return 0;
        }
        return stopsComponentGroupMapper.insertStopGroup(stopsComponentGroup);
    }

    public List<StopsComponentGroup> getStopGroupByNameList(List<String> groupNameList){
        return stopsComponentGroupMapper.getStopGroupByNameList(groupNameList);
    }

    public StopsComponentGroup getStopsComponentGroupByGroupName(String groupName) {
        if (StringUtils.isBlank(groupName)) {
            return null;
        }
        List<StopsComponentGroup> stopGroupByName = stopsComponentGroupMapper.getStopGroupByName(groupName);
        if (null == stopGroupByName || stopGroupByName.size() <= 0) {
            return null;
        }
        return stopGroupByName.get(0);
    }

    public int deleteStopsComponentGroup() {
        //the group table information is cleared
        // The call is successful, the group table information is cleared and then inserted.
        stopsComponentGroupMapper.deleteGroupCorrelation();
        int deleteRows = stopsComponentGroupMapper.deleteGroup();
        logger.debug("Successful deletion Group" + deleteRows + "piece of data!!!");
        return deleteRows;
    }

    public List<StopsComponentGroup> getStopGroupList(){
        return stopsComponentGroupMapper.getStopGroupList();
    }

    public List<StopsComponentGroupVo> getManageStopGroupList(){
        return stopsComponentGroupMapper.getManageStopGroupList();
    }

}
