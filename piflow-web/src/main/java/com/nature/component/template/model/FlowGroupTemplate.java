package com.nature.component.template.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import com.nature.component.flow.model.Flow;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
