package com.nature.component.flow.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import com.nature.component.mxGraph.model.MxGraphModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "FLOW_PROJECT")
public class FlowProject extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(255) COMMENT 'flow name'")
    private String name;

    @Column(columnDefinition = "varchar(255) COMMENT 'flow uuid'")
    private String uuid;

    @Column(name = "description", columnDefinition = "text(0) COMMENT 'description'")
    private String description;

    @Column(columnDefinition = "bit(1) COMMENT 'isExample'")
    private Boolean isExample = false;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "flowProject")
    @Where(clause = "enable_flag=1")
    private MxGraphModel mxGraphModel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flowProject")
    @Where(clause = "enable_flag=1")
    @org.hibernate.annotations.OrderBy(clause = "lastUpdateDttm desc")
    private List<Flow> flowList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flowProject")
    @Where(clause = "enable_flag=1")
    @org.hibernate.annotations.OrderBy(clause = "lastUpdateDttm desc")
    private List<FlowGroup> flowGroupList = new ArrayList<>();

}
