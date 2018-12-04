package com.nature.third.vo.flowInfo;

import java.io.Serializable;
import java.util.Date;

public class ThirdFlowInfoStopVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String state;
	private Date startTime;
	private Date endTime;
	private String flowId;

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

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	

}
