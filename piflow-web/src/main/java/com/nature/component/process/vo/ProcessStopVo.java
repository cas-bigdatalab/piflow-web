package com.nature.component.process.vo;

import com.nature.base.util.DateUtils;
import com.nature.common.Eunm.PortType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessStopVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ProcessVo processVo;
    private String name;
    private String bundel;
    private String groups;
    private String owner;
    private String description;
    private String inports;
    private PortType inPortType;
    private String outports;
    private PortType outPortType;
    private String state;
    private Date startTime;
    private Date endTime;
    private String pageId;
    private List<ProcessStopPropertyVo> processStopPropertyVoList = new ArrayList<ProcessStopPropertyVo>();

    public ProcessVo getProcessVo() {
        return processVo;
    }

    public void setProcessVo(ProcessVo processVo) {
        this.processVo = processVo;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public List<ProcessStopPropertyVo> getProcessStopPropertyVoList() {
        return processStopPropertyVoList;
    }

    public void setProcessStopPropertyVoList(List<ProcessStopPropertyVo> processStopPropertyVoList) {
        this.processStopPropertyVoList = processStopPropertyVoList;
    }

    public String getStartTimeStr() {
        return DateUtils.dateTimesToStr(this.startTime);
    }

    public String getEndTimeStr() {
        return DateUtils.dateTimesToStr(this.endTime);
    }
}
