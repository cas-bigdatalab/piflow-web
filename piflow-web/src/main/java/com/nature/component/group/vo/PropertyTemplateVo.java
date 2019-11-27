package com.nature.component.group.vo;

import java.io.Serializable;

public class PropertyTemplateVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String stopsTemplate;

    private String name;

    private String displayName;

    private String description;

    private String defaultValue;

    private String allowableValues;

    private Boolean required;

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
