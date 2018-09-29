package com.nature.component.workFlow.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

/**
 * stop组建表
 * 
 * @author Nature
 *
 */
@Entity
@Table(name = "FLOW_STOPS_TEMPLATE")
public class StopsTemplate extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FK_STOP_GROUP_ID")
	private StopGroup stopGroup;

	private String name;

	private String bundel;

	private String groups;

	private String owner;

	private String description;

	private Integer numberOfEntrances = 1;// 入口数

	private Integer numberOfExports = 1;// 出口数

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "stopsTemplate")
	@Where(clause = "enable_flag=1")
	private List<PropertyTemplate> properties = new ArrayList<PropertyTemplate>();

	public StopGroup getStopGroup() {
		return stopGroup;
	}

	public void setStopGroup(StopGroup stopGroup) {
		this.stopGroup = stopGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBundel() {
		return bundel;
	}

	public void setBundel(String bundel) {
		this.bundel = bundel;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNumberOfEntrances() {
		return numberOfEntrances;
	}

	public void setNumberOfEntrances(Integer numberOfEntrances) {
		this.numberOfEntrances = numberOfEntrances;
	}

	public Integer getNumberOfExports() {
		return numberOfExports;
	}

	public void setNumberOfExports(Integer numberOfExports) {
		this.numberOfExports = numberOfExports;
	}

	public List<PropertyTemplate> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyTemplate> properties) {
		this.properties = properties;
	}

}
