package cn.cnic.third.vo.flowLog;

import java.io.Serializable;

public class ThirdAppVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String user;
    private String name;
    private String queue;
    private String state;
    private String finalStatus;
    private Boolean progress;
    private String trackingUI;
    private String trackingUrl;
    private String diagnostics;
    private Long clusterId;
    private String applicationType;
    private String applicationTags;
    private Long startedTime;
    private Long finishedTime;
    private Long elapsedTime;
    private String amContainerLogs;
    private String amHostHttpAddress;
    private Integer allocatedMB;
    private Integer allocatedVCores;
    private Integer runningContainers;
    private Integer memorySeconds;
    private Integer vcoreSeconds;
    private Integer preemptedResourceMB;
    private Integer preemptedResourceVCores;
    private Integer numNonAMContainerPreempted;
    private Integer numAMContainerPreempted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public Boolean getProgress() {
        return progress;
    }

    public void setProgress(Boolean progress) {
        this.progress = progress;
    }

    public String getTrackingUI() {
        return trackingUI;
    }

    public void setTrackingUI(String trackingUI) {
        this.trackingUI = trackingUI;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public String getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(String diagnostics) {
        this.diagnostics = diagnostics;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationTags() {
        return applicationTags;
    }

    public void setApplicationTags(String applicationTags) {
        this.applicationTags = applicationTags;
    }

    public Long getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(Long startedTime) {
        this.startedTime = startedTime;
    }

    public Long getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Long finishedTime) {
        this.finishedTime = finishedTime;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getAmContainerLogs() {
        return amContainerLogs;
    }

    public void setAmContainerLogs(String amContainerLogs) {
        this.amContainerLogs = amContainerLogs;
    }

    public String getAmHostHttpAddress() {
        return amHostHttpAddress;
    }

    public void setAmHostHttpAddress(String amHostHttpAddress) {
        this.amHostHttpAddress = amHostHttpAddress;
    }

    public Integer getAllocatedMB() {
        return allocatedMB;
    }

    public void setAllocatedMB(Integer allocatedMB) {
        this.allocatedMB = allocatedMB;
    }

    public Integer getAllocatedVCores() {
        return allocatedVCores;
    }

    public void setAllocatedVCores(Integer allocatedVCores) {
        this.allocatedVCores = allocatedVCores;
    }

    public Integer getRunningContainers() {
        return runningContainers;
    }

    public void setRunningContainers(Integer runningContainers) {
        this.runningContainers = runningContainers;
    }

    public Integer getMemorySeconds() {
        return memorySeconds;
    }

    public void setMemorySeconds(Integer memorySeconds) {
        this.memorySeconds = memorySeconds;
    }

    public Integer getVcoreSeconds() {
        return vcoreSeconds;
    }

    public void setVcoreSeconds(Integer vcoreSeconds) {
        this.vcoreSeconds = vcoreSeconds;
    }

    public Integer getPreemptedResourceMB() {
        return preemptedResourceMB;
    }

    public void setPreemptedResourceMB(Integer preemptedResourceMB) {
        this.preemptedResourceMB = preemptedResourceMB;
    }

    public Integer getPreemptedResourceVCores() {
        return preemptedResourceVCores;
    }

    public void setPreemptedResourceVCores(Integer preemptedResourceVCores) {
        this.preemptedResourceVCores = preemptedResourceVCores;
    }

    public Integer getNumNonAMContainerPreempted() {
        return numNonAMContainerPreempted;
    }

    public void setNumNonAMContainerPreempted(Integer numNonAMContainerPreempted) {
        this.numNonAMContainerPreempted = numNonAMContainerPreempted;
    }

    public Integer getNumAMContainerPreempted() {
        return numAMContainerPreempted;
    }

    public void setNumAMContainerPreempted(Integer numAMContainerPreempted) {
        this.numAMContainerPreempted = numAMContainerPreempted;
    }

}
