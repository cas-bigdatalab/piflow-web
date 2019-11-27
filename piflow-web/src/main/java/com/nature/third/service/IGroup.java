package com.nature.third.service;

import com.nature.common.Eunm.RunModeType;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessGroup;
import com.nature.third.vo.flow.ThirdProgressVo;
import com.nature.third.vo.flowGroup.ThirdFlowGroupInfoResponse;

import java.util.List;
import java.util.Map;

public interface IGroup {

    /**
     * startFlowGroup
     *
     * @param processGroup
     * @return
     */
    public Map<String,Object> startFlowGroup(ProcessGroup processGroup, RunModeType runModeType);

    /**
     * stopFlowGroup
     *
     * @param processGroupId
     * @return
     */
    public String stopFlowGroup(String processGroupId);

    /**
     * getFlowGroupInfoStr
     *
     * @param groupId
     * @return
     */
    public String getFlowGroupInfoStr(String groupId);

    /**
     * getFlowGroupInfo
     *
     * @param groupId
     * @return
     */
    public ThirdFlowGroupInfoResponse getFlowGroupInfo(String groupId);

    /**
     * getFlowGroupProgress
     *
     * @param groupId
     * @return
     */
    public Double getFlowGroupProgress(String groupId);

    /**
     * update FlowGroup by interface
     *
     * @param groupId
     */
    public void updateFlowGroupByInterface(String groupId);

    /**
     * update FlowGroups by interface
     *
     * @param groupIds
     */
    public void updateFlowGroupsByInterface(List<String> groupIds);


}
