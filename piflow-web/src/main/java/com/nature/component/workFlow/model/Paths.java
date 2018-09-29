package com.nature.component.workFlow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

@Entity
@Table(name = "FLOW_PATH")
public class Paths extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FK_FLOW_ID")
	private Flow flow;

	@Column(name = "LINE_FROM")
	private String from;

	@Column(name = "LINE_OUTPORT")
	private String outport;

	@Column(name = "LINE_INPORT")
	private String inport;

	@Column(name = "LINE_TO")
	private String to;

	@Column(name = "LINE_PORT")
	private String port;

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getOutport() {
		return outport;
	}

	public void setOutport(String outport) {
		this.outport = outport;
	}

	public String getInport() {
		return inport;
	}

	public void setInport(String inport) {
		this.inport = inport;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
