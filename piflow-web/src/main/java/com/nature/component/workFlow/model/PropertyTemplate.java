package com.nature.component.workFlow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

@Entity
@Table(name = "FLOW_STOPS_PROPERTY_TEMPLATE")
public class PropertyTemplate extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	// @ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "FK_STOPS_ID")
	private String stopsTemplate;

	private String name;

	private String displayName;

	@Column(name = "DESCRIPTION", columnDefinition = "varchar(1000) COMMENT '描述'")
	private String description;

	@Column(name = "DEFAULT_VALUE", columnDefinition = "varchar(1000) COMMENT '默认值'")
	private String defaultValue;

	@Column(name = "ALLOWABLE_VALUES")
	private String allowableValues;

	@Column(name = "PROPERTY_REQUIRED")
	private Boolean required;

	@Column(name = "PROPERTY_SENSITIVE")
	private Boolean sensitive;

	public String getStopsTemplate() {
		return stopsTemplate;
	}

	public void setStopsTemplate(String stopsTemplate) {
		this.stopsTemplate = stopsTemplate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getAllowableValues() {
		return allowableValues;
	}

	public void setAllowableValues(String allowableValues) {
		this.allowableValues = allowableValues;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getSensitive() {
		return sensitive;
	}

	public void setSensitive(Boolean sensitive) {
		this.sensitive = sensitive;
	}
}
