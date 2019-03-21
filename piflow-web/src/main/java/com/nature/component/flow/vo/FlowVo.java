package com.nature.component.flow.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nature.component.mxGraph.vo.MxGraphModelVo;

public class FlowVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;

	private String uuid;

	private String crtDttmString;
	
	private String description; //描述

	private String driverMemory;

	private String executorNumber;

	private String executorMemory;

	private String executorCores;
	
	private Date crtDttm;

	private MxGraphModelVo mxGraphModelVo;//画板信息

	private List<StopsVo> stopsVoList = new ArrayList<StopsVo>();//当前流所有的stop

	private List<PathsVo> pathsVoList = new ArrayList<PathsVo>();//当前流所有的paths

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

	public String getDriverMemory() {
		return driverMemory;
	}

	public void setDriverMemory(String driverMemory) {
		this.driverMemory = driverMemory;
	}

	public String getExecutorNumber() {
		return executorNumber;
	}

	public void setExecutorNumber(String executorNumber) {
		this.executorNumber = executorNumber;
	}

	public String getExecutorMemory() {
		return executorMemory;
	}

	public void setExecutorMemory(String executorMemory) {
		this.executorMemory = executorMemory;
	}

	public String getExecutorCores() {
		return executorCores;
	}

	public void setExecutorCores(String executorCores) {
		this.executorCores = executorCores;
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

	public Date getCrtDttm() {
		return crtDttm;
	}

	public void setCrtDttm(Date crtDttm) {
		this.crtDttm = crtDttm;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
