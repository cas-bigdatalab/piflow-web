package com.nature.third.vo.flowInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlowInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String state;
	private String startTime;
	private String endTime;

	List<FlowInfoStopVo> stops = new ArrayList<FlowInfoStopVo>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<FlowInfoStopVo> getStops() {
		return stops;
	}

	public void setStops(List<FlowInfoStopVo> stops) {
		this.stops = stops;
	}

}
