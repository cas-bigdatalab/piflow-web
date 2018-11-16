package com.nature.component.workFlow.model;

import java.util.Date;

import javax.persistence.*;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

@Entity
@Table(name =  "flow_info")
public class FlowInfoDb extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FK_FLOW_ID")
	private Flow flow;

	private String name;
	private String state;
	private Date startTime;
	private Date endTime;
	private String progress;

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
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

}
