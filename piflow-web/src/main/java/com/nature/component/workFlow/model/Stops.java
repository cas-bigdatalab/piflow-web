package com.nature.component.workFlow.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.nature.common.Eunm.PortType;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

/**
 * stop组建表
 *
 * @author Nature
 */
@Entity
@Table(name = "FLOW_STOPS")
public class Stops extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_FLOW_ID")
    private Flow flow;

    private String name;

    private String bundel;

    private String groups;

    private String owner;

    private String description;

    private String inports;

    @Enumerated(EnumType.STRING)
    private PortType inPortType;

    private String outports;

    @Enumerated(EnumType.STRING)
    private PortType outPortType;

    @Column(name = "page_id")
    private String pageId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stops")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<Property> properties = new ArrayList<Property>();

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
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

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
