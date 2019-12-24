package com.nature.component.flow.service;

import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowInfoDb;

import java.util.List;


public interface IFlowInfoDbService {

    public List<Flow> findAppList();

    public List<FlowInfoDb> getFlowInfoByIds(List<String> ids);

    public FlowInfoDb getFlowInfoById(String id);

    public int deleteFlowInfoById(String id);

    public List<FlowInfoDb> getAppListByFlowId(String flowId);
}
