package com.nature.component.template.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "FLOW_GROUP_TEMPLATE")
public class FlowGroupTemplate extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String flowGroupName;

    private String name;

    @Column(name = "description", columnDefinition = "varchar(1024) COMMENT 'description'")
    private String description;

    private String path;

}
