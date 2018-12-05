package com.nature.component.process.vo;

import com.nature.base.util.DateUtils;
import com.nature.common.Eunm.ProcessState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessVo implements Serializable {

    private String id;
    private Date crtDttm;
    private String name;
    private String driverMemory;
    private String executorNumber;
    private String executorMemory;
    private String executorCores;
    private String description;
    private String flowId;
    private String appId;
    private String parentProcessId;
    private String processId;
    private ProcessState state;
    private Date startTime;
    private Date endTime;
    private String progress;
    private String viewXml;
    private List<ProcessStopVo> processStopVoList = new ArrayList<ProcessStopVo>();
    private List<ProcessPathVo> processPathVoList = new ArrayList<ProcessPathVo>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCrtDttm() {
        return crtDttm;
    }

    public void setCrtDttm(Date crtDttm) {
        this.crtDttm = crtDttm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriverMemory() {
        return driverMemory;
    }

    public void setDriverMemory(String driverMemory) {
        this.driverMemory = driverMemory;
    }

    public String getExecutorNumber() {
        return executorNumber;
    }

    public void setExecutorNumber(String executorNumber) {
        this.executorNumber = executorNumber;
    }

    public String getExecutorMemory() {
        return executorMemory;
    }

    public void setExecutorMemory(String executorMemory) {
        this.executorMemory = executorMemory;
    }

    public String getExecutorCores() {
        return executorCores;
    }

    public void setExecutorCores(String executorCores) {
        this.executorCores = executorCores;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getParentProcessId() {
        return parentProcessId;
    }

    public void setParentProcessId(String parentProcessId) {
        this.parentProcessId = parentProcessId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state) {
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

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getViewXml() {
        return viewXml;
    }

    public void setViewXml(String viewXml) {
        this.viewXml = viewXml;
    }

    public List<ProcessStopVo> getProcessStopVoList() {
        return processStopVoList;
    }

    public void setProcessStopVoList(List<ProcessStopVo> processStopVoList) {
        this.processStopVoList = processStopVoList;
    }

    public List<ProcessPathVo> getProcessPathVoList() {
        return processPathVoList;
    }

    public void setProcessPathVoList(List<ProcessPathVo> processPathVoList) {
        this.processPathVoList = processPathVoList;
    }

    public String getCrtDttmStr() {
        return DateUtils.dateTimesToStr(this.crtDttm);
    }

    public String getStartTimeStr() {
        return DateUtils.dateTimesToStr(this.startTime);
    }

    public String getEndTimeStr() {
        return DateUtils.dateTimesToStr(this.endTime);
    }
}
