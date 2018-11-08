package com.nature.component.workFlow.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import com.nature.component.mxGraph.model.MxGraphModel;

@Entity
@Table(name = "FLOW")
public class Flow extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name = "app_id", referencedColumnName = "id")
	private FlowInfoDb appId;

	private String name;

	private String uuid;
	
	private String driverMemory;
	
	private String executorNumber;
	
	private String executorMemory;
	
	private String executorCores;
	
	@Column(name = "description",columnDefinition="varchar(1000) COMMENT '描述'")
	private String description;

	@OneToOne
	@JoinColumn(name = "fk_mx_graph_model_id", referencedColumnName = "id")
	private MxGraphModel mxGraphModel;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "flow")
	@Where(clause = "enable_flag=1")
	@OrderBy(clause = "lastUpdateDttm desc")
	private List<Stops> stopsList = new ArrayList<Stops>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "flow")
	@Where(clause = "enable_flag=1")
	@OrderBy(clause = "lastUpdateDttm desc")
	private List<Paths> pathsList = new ArrayList<Paths>();

	public FlowInfoDb getAppId() {
		return appId;
	}

	public void setAppId(FlowInfoDb appId) {
		this.appId = appId;
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

	public MxGraphModel getMxGraphModel() {
		return mxGraphModel;
	}

	public void setMxGraphModel(MxGraphModel mxGraphModel) {
		this.mxGraphModel = mxGraphModel;
	}

	public List<Stops> getStopsList() {
		return stopsList;
	}

	public void setStopsList(List<Stops> stopsList) {
		this.stopsList = stopsList;
	}

	public List<Paths> getPathsList() {
		return pathsList;
	}

	public void setPathsList(List<Paths> pathsList) {
		this.pathsList = pathsList;
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
	
}
