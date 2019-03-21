package com.nature.component.process.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

import javax.persistence.*;

@Entity
@Table(name = "FLOW_PROCESS_STOP_PROPERTY")
public class ProcessStopProperty extends BaseHibernateModelUUIDNoCorpAgentId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_PROCESS_STOP_ID")
    private ProcessStop processStop;

    private String name;

    private String displayName;

    @Column(name = "description", columnDefinition = "varchar(1024) COMMENT '描述'")
    private String description;

    @Column(name = "CUSTOM_VALUE")
    private String customValue;

    @Column(name = "ALLOWABLE_VALUES")
    private String allowableValues;

    @Column(name = "PROPERTY_REQUIRED")
    private Boolean required;

    @Column(name = "PROPERTY_SENSITIVE")
    private Boolean sensitive;

    public ProcessStop getProcessStop() {
        return processStop;
    }

    public void setProcessStop(ProcessStop processStop) {
        this.processStop = processStop;
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
}
