package com.nature.component.workFlow.vo;

import com.nature.component.mxGraph.vo.MxGraphModelVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlowVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private FlowInfoDbVo flowInfoDbVo;

	private String name;

	private String uuid;

	private String crtDttmString;
	
	private String description; //描述

	private MxGraphModelVo mxGraphModelVo;//画板信息

	private List<StopsVo> stopsVoList = new ArrayList<StopsVo>();//当前流所有的stop

	private List<PathsVo> pathsVoList = new ArrayList<PathsVo>();//当前流所有的paths

	public FlowInfoDbVo getFlowInfoDbVo() {
		return flowInfoDbVo;
	}

	public void setFlowInfoDbVo(FlowInfoDbVo flowInfoDbVo) {
		this.flowInfoDbVo = flowInfoDbVo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCrtDttmString() {
		return crtDttmString;
	}

	public void setCrtDttmString(String crtDttmString) {
		this.crtDttmString = crtDttmString;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MxGraphModelVo getMxGraphModelVo() {
		return mxGraphModelVo;
	}

	public void setMxGraphModelVo(MxGraphModelVo mxGraphModelVo) {
		this.mxGraphModelVo = mxGraphModelVo;
	}

	public List<StopsVo> getStopsVoList() {
		return stopsVoList;
	}

	public void setStopsVoList(List<StopsVo> stopsVoList) {
		this.stopsVoList = stopsVoList;
	}

	public List<PathsVo> getPathsVoList() {
		return pathsVoList;
	}

	public void setPathsVoList(List<PathsVo> pathsVoList) {
		this.pathsVoList = pathsVoList;
	}
}
