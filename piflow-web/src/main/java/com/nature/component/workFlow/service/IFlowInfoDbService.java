package com.nature.component.workFlow.service;

import java.util.List;

import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.FlowInfoDb;


public interface IFlowInfoDbService {
	 
	public List<Flow> findAppList();
	
	public List<FlowInfoDb> getFlowInfoByIds(List<String> ids);
	
	public int deleteFlowInfoById(String id);
}
