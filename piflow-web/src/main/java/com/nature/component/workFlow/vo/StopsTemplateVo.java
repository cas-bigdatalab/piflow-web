package com.nature.component.workFlow.vo;

import com.nature.common.Eunm.PortType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StopsTemplateVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private String bundel;

    private String groups;

    private String owner;

    private String description;

    private String inports;

    private PortType inPortType;

    private String outports;

    private PortType outPortType;

    private String stopGroup;

    private List<PropertyTemplateVo> propertiesVo = new ArrayList<PropertyTemplateVo>();

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

    public String getInports() {
        return inports;
    }

    public void setInports(String inports) {
        this.inports = inports;
    }

    public PortType getInPortType() {
        return inPortType;
    }

    public void setInPortType(PortType inPortType) {
        this.inPortType = inPortType;
    }

    public String getOutports() {
        return outports;
    }

    public void setOutports(String outports) {
        this.outports = outports;
    }

    public PortType getOutPortType() {
        return outPortType;
    }

    public void setOutPortType(PortType outPortType) {
        this.outPortType = outPortType;
    }

    public String getStopGroup() {
        return stopGroup;
    }

    public void setStopGroup(String stopGroup) {
        this.stopGroup = stopGroup;
    }

    public List<PropertyTemplateVo> getPropertiesVo() {
        return propertiesVo;
    }

    public void setPropertiesVo(List<PropertyTemplateVo> propertiesVo) {
        this.propertiesVo = propertiesVo;
    }
}
