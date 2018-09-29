package com.nature.component.workFlow.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

@Entity
@Table(name = "FLOW")
public class Flow extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	private String appId;

	private String name;

	private String uuid;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "flow")
	@Where(clause = "enable_flag=1")
	@OrderBy(clause = "lastUpdateDttm desc")
	private List<Stops> stops = new ArrayList<Stops>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "flow")
	@Where(clause = "enable_flag=1")
	@OrderBy(clause = "lastUpdateDttm desc")
	private List<Paths> paths = new ArrayList<Paths>();

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
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

	public List<Stops> getStops() {
		return stops;
	}

	public void setStops(List<Stops> stops) {
		this.stops = stops;
	}

	public List<Paths> getPaths() {
		return paths;
	}

	public void setPaths(List<Paths> paths) {
		this.paths = paths;
	}

}
