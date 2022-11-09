package cn.cnic.component.flow.domain;

import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.flow.entity.FlowStopsPublishing;
import cn.cnic.component.flow.mapper.FlowStopsPublishingMapper;
import cn.cnic.component.flow.utils.FlowStopsPublishingUtils;
import cn.cnic.component.flow.vo.FlowStopsPublishingVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class FlowStopsPublishingDomain {

    @Autowired
    private FlowStopsPublishingMapper flowStopsPublishingMapper;

    public int addFlowStopsPublishing(FlowStopsPublishing flowStopsPublishing) throws Exception {
        if (null == flowStopsPublishing) {
            throw new Exception("save failed");
        }
        String id = flowStopsPublishing.getId();
        if (StringUtils.isBlank(id)) {
            flowStopsPublishing.setId(UUIDUtils.getUUID32());
        }
        int affectedRows = flowStopsPublishingMapper.addFlowStopsPublishing(flowStopsPublishing);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        return affectedRows;
    }

    public int updateFlowStopsPublishing(boolean isAdmin, String username, String publishingId, String name, List<String> stopsIds) throws Exception {
        if (StringUtils.isBlank(username)) {
            throw new Exception("lastUpdateUser is null");
        }
        if (StringUtils.isBlank(publishingId)) {
            throw new Exception("publishingId is null");
        }
        if (StringUtils.isBlank(name)) {
            throw new Exception("name is null");
        }
        if (null == stopsIds || stopsIds.size() <= 0) {
            throw new Exception("stopsId is null");
        }
        // find stops id list by publishingId
        List<String> publishingStopsIdList = null;
        if (isAdmin) {
            publishingStopsIdList = getFlowStopsPublishingByPublishingId(publishingId);
        } else {
            publishingStopsIdList = getFlowStopsPublishingByPublishingIdAndCreateUser(username, publishingId);
        }
        if (null == publishingStopsIdList || publishingStopsIdList.size() <= 0) {
            throw new Exception("data error!!!");
        }
        int affectedRows = flowStopsPublishingMapper.updateFlowStopsPublishingName(username, publishingId, name);
        if (affectedRows <= 0) {
            return 0;
        }
        // Eliminate existing and to be deleted
        for (String stopsId : publishingStopsIdList) {
            if (stopsIds.contains(stopsId)) {
                stopsIds.remove(stopsId);
                continue;
            }
            affectedRows += flowStopsPublishingMapper.updateFlowStopsPublishingEnableFlagByPublishingIdAndStopId(username, publishingId, stopsId);
        }

        // add stopsId
        if (stopsIds.size() > 0) {
            FlowStopsPublishing flowStopsPublishing = FlowStopsPublishingUtils.flowStopsPublishingNewNoId(username);
            flowStopsPublishing.setId(UUIDUtils.getUUID32());
            flowStopsPublishing.setPublishingId(publishingId);
            flowStopsPublishing.setName(name);
            flowStopsPublishing.setStopsIds(stopsIds);
            affectedRows += flowStopsPublishingMapper.addFlowStopsPublishing(flowStopsPublishing);
        }
        return affectedRows;
    }

    public int updateFlowStopsPublishingEnableFlagByPublishingId(String username, String publishingId) {
        return flowStopsPublishingMapper.updateFlowStopsPublishingEnableFlagByPublishingId(username, publishingId);
    }

    public List<FlowStopsPublishing> getFlowStopsPublishingList(String username, boolean isAdmin, String param) {
        return flowStopsPublishingMapper.getFlowStopsPublishingList(username, isAdmin, param);
    }

    public List<FlowStopsPublishingVo> getFlowStopsPublishingVoByPublishingId(String publishingId) {
        return flowStopsPublishingMapper.getFlowStopsPublishingVoByPublishingId(publishingId);
    }

    public List<String> getFlowStopsPublishingByPublishingId(String publishingId) {
        return flowStopsPublishingMapper.getPublishingStopsIdsByPublishingId(publishingId);
    }

    public List<String> getFlowStopsPublishingByPublishingIdAndCreateUser(String username, String publishingId) {
        return flowStopsPublishingMapper.getFlowStopsPublishingByPublishingIdAndCreateUser(username, publishingId);
    }

    public List<FlowStopsPublishing> getFlowStopsPublishingListByPublishingIdAndStopsId(String publishingId, String stopsId) {
        return flowStopsPublishingMapper.getFlowStopsPublishingListByPublishingIdAndStopsId(publishingId, stopsId);
    }

    public List<FlowStopsPublishing> getFlowStopsPublishingListByFlowId(String username, String flowId) {
        return flowStopsPublishingMapper.getFlowStopsPublishingListByFlowId(username, flowId);
    }

    public List<String> getPublishingNameListByStopsIds(List<String> stopsIds) {
        return flowStopsPublishingMapper.getPublishingNameListByStopsIds(stopsIds);
    }

    public List<String> getPublishingNameListByFlowId(String flowId) {
        return flowStopsPublishingMapper.getPublishingNameListByFlowId(flowId);
    }

    public List<String> getFlowIdByPublishingId(String publishingId) {
        return flowStopsPublishingMapper.getFlowIdByPublishingId(publishingId);
    }

    public List<String> getPublishingIdsByPublishingName(String publishingName) {
        return flowStopsPublishingMapper.getPublishingIdsByPublishingName(publishingName);
    }

    public List<String> getPublishingNamesByStopsId(String stopsId) {
        return flowStopsPublishingMapper.getPublishingNamesByStopsId(stopsId);
    }

}