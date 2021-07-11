package cn.cnic.third.service;

import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.third.vo.flowGroup.ThirdFlowGroupInfoResponse;

import java.util.List;
import java.util.Map;

public interface IGroup {

    /**
     * startFlowGroup
     *
     * @param processGroup
     * @return
     */
    public Map<String, Object> startFlowGroup(ProcessGroup processGroup, RunModeType runModeType);

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
     * update FlowGroup By Interface
     *
     * @param groupId
     * @throws Exception 
     */
    public void updateFlowGroupByInterface(String groupId) throws Exception;

    /**
     * update FlowGroups By Interface
     *
     * @param groupIds
     * @throws Exception 
     */
    public void updateFlowGroupsByInterface(List<String> groupIds) throws Exception ;


}
