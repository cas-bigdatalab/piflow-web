package com.nature.component.process.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

import javax.persistence.*;

@Entity
@Table(name = "FLOW_PROCESS_PATH")
public class ProcessPath extends BaseHibernateModelUUIDNoCorpAgentId {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_PROCESS_ID")
    private Process process;

    @Column(name = "LINE_FROM")
    private String from;

    @Column(name = "LINE_OUTPORT")
    private String outport;

    @Column(name = "LINE_INPORT")
    private String inport;

    @Column(name = "LINE_TO")
    private String to;

    @Column(name = "page_id")
    private String pageId;

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
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

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
}
