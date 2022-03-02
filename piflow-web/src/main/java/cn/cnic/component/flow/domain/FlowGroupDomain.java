package cn.cnic.component.flow.domain;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.entity.FlowGroupPaths;
import cn.cnic.component.flow.mapper.FlowGroupMapper;
import cn.cnic.component.flow.mapper.FlowGroupPathsMapper;
import cn.cnic.component.flow.vo.FlowGroupVo;
import cn.cnic.component.mxGraph.domain.MxGraphModelDomain;
import cn.cnic.component.mxGraph.entity.MxGraphModel;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class FlowGroupDomain {

    private final FlowGroupMapper flowGroupMapper;
    private final FlowGroupPathsMapper flowGroupPathsMapper;
    private final FlowDomain flowDomain;
    private final MxGraphModelDomain mxGraphModelDomain;

    @Autowired
    public FlowGroupDomain(FlowGroupMapper flowGroupMapper,
                           FlowGroupPathsMapper flowGroupPathsMapper,
                           FlowDomain flowDomain,
                           MxGraphModelDomain mxGraphModelDomain) {
        this.flowGroupMapper = flowGroupMapper;
        this.flowGroupPathsMapper = flowGroupPathsMapper;
        this.flowDomain = flowDomain;
        this.mxGraphModelDomain = mxGraphModelDomain;
    }

    public int saveOrUpdate(FlowGroup flowGroup) throws Exception {
        if (null == flowGroup) {
            throw new Exception("save failed, flow is null");
        }
        if (StringUtils.isBlank(flowGroup.getId())) {
            return addFlowGroup(flowGroup);
        }
        return updateFlowGroup(flowGroup);
    }

    public int addFlowGroup(FlowGroup flowGroup) throws Exception {
        if (null == flowGroup) {
            throw new Exception("save failed");
        }
        String id = flowGroup.getId();
        if (StringUtils.isBlank(id)) {
            flowGroup.setId(UUIDUtils.getUUID32());
        }
        int affectedRows = flowGroupMapper.addFlowGroup(flowGroup);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        List<FlowGroupPaths> flowGroupPathsList = flowGroup.getFlowGroupPathsList();
        if (null != flowGroupPathsList && flowGroupPathsList.size() > 0) {
            for (FlowGroupPaths flowGroupPaths : flowGroupPathsList) {
                if (null == flowGroupPaths) {
                    continue;
                }
                flowGroupPaths.setFlowGroup(flowGroup);
                affectedRows += addFlowGroupPaths(flowGroupPaths);
            }
        }
        List<Flow> flowList = flowGroup.getFlowList();
        if (null != flowList && flowList.size() > 0) {
            for (Flow flow : flowList) {
                if (null == flow) {
                    continue;
                }
                flow.setFlowGroup(flowGroup);
                affectedRows += flowDomain.addFlow(flow);
            }
        }
        List<FlowGroup> flowGroupList = flowGroup.getFlowGroupList();
        if (null != flowGroupList && flowGroupList.size() > 0) {
            for (FlowGroup flowGroup_i : flowGroupList) {
                if (null == flowGroup_i) {
                    continue;
                }
                flowGroup_i.setFlowGroup(flowGroup);
                affectedRows += addFlowGroup(flowGroup_i);
            }
        }
        MxGraphModel mxGraphModel = flowGroup.getMxGraphModel();
        if (null != mxGraphModel) {
            mxGraphModel.setFlowGroup(flowGroup);
            affectedRows += mxGraphModelDomain.addMxGraphModel(mxGraphModel);
        }
        return affectedRows;
    }

    public int addFlowGroupPaths(FlowGroupPaths flowGroupPaths) throws Exception {
        if (null == flowGroupPaths) {
            throw new Exception("save failed");
        }
        String id = flowGroupPaths.getId();
        if (StringUtils.isBlank(id)) {
            flowGroupPaths.setId(UUIDUtils.getUUID32());
        }
        int affectedRows = flowGroupPathsMapper.addFlowGroupPaths(flowGroupPaths);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        return affectedRows;
    }

    public int updateFlowGroup(FlowGroup flowGroup) throws Exception {
        if (null == flowGroup) {
            return 0;
        }
        int affectedRows = flowGroupMapper.updateFlowGroup(flowGroup);
        List<FlowGroupPaths> flowGroupPathsList = flowGroup.getFlowGroupPathsList();
        if (null != flowGroupPathsList && flowGroupPathsList.size() > 0) {
            for (FlowGroupPaths flowGroupPaths : flowGroupPathsList) {
                if (null == flowGroupPaths) {
                    continue;
                }
                flowGroupPaths.setFlowGroup(flowGroup);
                if (StringUtils.isBlank(flowGroupPaths.getId())) {
                    affectedRows += addFlowGroupPaths(flowGroupPaths);
                    continue;
                }
                affectedRows += updateFlowGroupPaths(flowGroupPaths);
            }
        }
        List<Flow> flowList = flowGroup.getFlowList();
        if (null != flowList && flowList.size() > 0) {
            for (Flow flow : flowList) {
                if (null == flow) {
                    continue;
                }
                flow.setFlowGroup(flowGroup);
                if (StringUtils.isBlank(flow.getId())) {
                    affectedRows += flowDomain.addFlow(flow);
                    continue;
                }
                affectedRows += flowDomain.updateFlow(flow);
            }
        }
        List<FlowGroup> flowGroupList = flowGroup.getFlowGroupList();
        if (null != flowGroupList && flowGroupList.size() > 0) {
            for (FlowGroup flowGroup_i : flowGroupList) {
                if (null == flowGroup_i) {
                    continue;
                }
                flowGroup_i.setFlowGroup(flowGroup);
                if (StringUtils.isBlank(flowGroup_i.getId())) {
                    affectedRows += addFlowGroup(flowGroup_i);
                    continue;
                }
                affectedRows += updateFlowGroup(flowGroup_i);
            }
        }
        MxGraphModel mxGraphModel = flowGroup.getMxGraphModel();
        if (null != mxGraphModel) {
            mxGraphModel.setFlowGroup(flowGroup);
            if (StringUtils.isBlank(mxGraphModel.getId())) {
                affectedRows += mxGraphModelDomain.addMxGraphModel(mxGraphModel);
            } else {
                affectedRows += mxGraphModelDomain.updateMxGraphModel(mxGraphModel);
            }
        }
        return affectedRows;
    }

    public int updateFlowGroupPaths(FlowGroupPaths flowGroupPaths) {
        if (null == flowGroupPaths) {
            return 0;
        }
        return flowGroupPathsMapper.updateFlowGroupPaths(flowGroupPaths);
    }

    public int updateEnableFlagById(String username, String id, boolean enableFlag) {
        return flowGroupMapper.updateEnableFlagById(username, id, enableFlag);
    }

    public List<FlowGroupVo> getFlowGroupListParam(String username, boolean isAdmin, String param) {
        return flowGroupMapper.getFlowGroupListParam(username, isAdmin, param);
    }

    public FlowGroup getFlowGroupById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return flowGroupMapper.getFlowGroupById(id);
    }

    public FlowGroup getFlowGroupByPageId(String fid, String pageId) {
        return flowGroupMapper.getFlowGroupByPageId(fid, pageId);
    }

    public String getFlowIdByNameAndFlowGroupId(String fid, String flowGroupName) {
        return flowGroupMapper.getFlowGroupIdByNameAndFid(fid, flowGroupName);
    }

    public String getFlowGroupIdByPageId(String fid, String pageId) {
        return flowGroupMapper.getFlowGroupIdByPageId(fid, pageId);
    }

    public String[] getFlowGroupNameByNameInGroup(String fId, String name) {
        return flowGroupMapper.getFlowGroupNamesByNameAndEnableFlagInGroup(fId, name);
    }

    public Integer getMaxFlowGroupPageIdByFlowGroupId(String flowGroupId) {
        return flowGroupMapper.getMaxFlowGroupPageIdByFlowGroupId(flowGroupId);
    }

    public String getFlowGroupName(String flowGroupName) {
        return flowGroupMapper.getFlowGroupName(flowGroupName);
    }

    public Integer getMaxFlowGroupPathPageIdByFlowGroupId(String flowGroupId) {
        return flowGroupPathsMapper.getMaxFlowGroupPathPageIdByFlowGroupId(flowGroupId);
    }

    public String getFlowGroupNameByPageId(String fid, String pageId) {
        return flowGroupMapper.getFlowGroupNameByPageId(fid, pageId);
    }

    public List<FlowGroupPaths> getFlowGroupPaths(String flowGroupId, String pageId, String from, String to) {
        return flowGroupPathsMapper.getFlowGroupPaths(flowGroupId, pageId, from, to);
    }

}
