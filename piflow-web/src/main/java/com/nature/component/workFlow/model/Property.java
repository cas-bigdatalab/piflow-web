
package com.nature.component.workFlow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

/**
 * stop的属性
 * 
 * @author Nature
 *
 */
@Entity
@Table(name = "FLOW_STOPS_PROPERTY")
public class Property extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FK_STOPS_ID")
	private Stops stops;

	private String name;

	private String displayName;

	private String description;

	@Column(name = "CUSTOM_VALUE")
	private String customValue;

	@Column(name = "ALLOWABLE_VALUES")
	private String allowableValues;

	@Column(name = "PROPERTY_REQUIRED")
	private boolean required;

	@Column(name = "PROPERTY_SENSITIVE")
	private boolean sensitive;

	public Stops getStops() {
		return stops;
	}

	public void setStops(Stops stops) {
		this.stops = stops;
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

	public String getCustomValue() {
		return customValue;
	}

	public void setCustomValue(String customValue) {
		this.customValue = customValue;
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
