
package com.nature.component.flow.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

import javax.persistence.*;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_STOPS_ID")
	private Stops stops;

	private String name;

	private String displayName;

	@Column(name = "DESCRIPTION", columnDefinition = "varchar(1000) COMMENT '描述'")
	private String description;

	@Column(name = "DEFAULT_VALUE", columnDefinition = "varchar(1000) COMMENT '默认值'")
	private String customValue;

	@Column(name = "ALLOWABLE_VALUES")
	private String allowableValues;

	@Column(name = "PROPERTY_REQUIRED")
	private Boolean required;

	@Column(name = "PROPERTY_SENSITIVE")
	private Boolean sensitive;
	
	private Boolean isSelect;

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

	public Boolean getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(Boolean isSelect) {
		this.isSelect = isSelect;
	}
	
}
