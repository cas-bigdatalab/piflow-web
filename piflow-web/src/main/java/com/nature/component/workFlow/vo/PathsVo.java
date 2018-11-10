package com.nature.component.workFlow.vo;

import com.nature.component.workFlow.model.Flow;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

public class PathsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private Flow flow;

    private String from;

    private String outport;

    private String inport;

    private String to;

    private String port;

    private String pageId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getOutport() {
        return outport;
    }

    public void setOutport(String outport) {
        this.outport = outport;
    }

    public String getInport() {
        return inport;
    }

    public void setInport(String inport) {
        this.inport = inport;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
}
