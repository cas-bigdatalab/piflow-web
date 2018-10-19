package com.nature.component.workFlow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

@Entity
@Table(name = "FLOW_STOPS_PROPERTY_TEMPLATE")
public class PropertyTemplate extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	//@ManyToOne(fetch = FetchType.EAGER)
	@Column(name = "FK_STOPS_ID")
	private String stopsTemplate;

	private String name;

	private String displayName;

	private String description;

	@Column(name = "DEFAULT_VALUE")
	private String defaultValue;

	@Column(name = "ALLOWABLE_VALUES")
	private String allowableValues;

	@Column(name = "PROPERTY_REQUIRED")
	private boolean required;

	@Column(name = "PROPERTY_SENSITIVE")
	private boolean sensitive;

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

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isSensitive() {
		return sensitive;
	}

	public void setSensitive(boolean sensitive) {
		this.sensitive = sensitive;
	}

}
