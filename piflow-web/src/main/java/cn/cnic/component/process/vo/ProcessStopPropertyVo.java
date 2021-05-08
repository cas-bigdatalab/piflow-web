package cn.cnic.component.process.vo;

import java.io.Serializable;

public class ProcessStopPropertyVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private ProcessStopVo processStopVo;
    private String name;
    private String displayName;
    private String description;
    private String customValue;
    private String allowableValues;
    private Boolean required;
    private Boolean sensitive;

    public ProcessStopVo getProcessStopVo() {
        return processStopVo;
    }

    public void setProcessStopVo(ProcessStopVo processStopVo) {
        this.processStopVo = processStopVo;
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
