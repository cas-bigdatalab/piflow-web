package com.nature.component.workFlow.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

@Entity
@Table(name =  "flow_info")
public class FlowInfoDb extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	private String name;
	private String state;
	private Date startTime;
	private Date endTime;
	private String progress;

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

}
