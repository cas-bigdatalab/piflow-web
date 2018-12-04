package com.nature.third.vo.flowInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThirdFlowInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String pid;
	private String name;
	private String state;
	private Date startTime;
	private Date endTime;
	private String progress;

	List<ThirdFlowInfoStopsVo> stops = new ArrayList<ThirdFlowInfoStopsVo>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public List<ThirdFlowInfoStopsVo> getStops() {
		return stops;
	}

	public void setStops(List<ThirdFlowInfoStopsVo> stops) {
		this.stops = stops;
	}
}
