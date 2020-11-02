package cn.cnic.component.flow.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FLOW_GROUP")
@Setter
@Getter
public class FlowGroup extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(255) COMMENT 'flow name'")
    private String name;

    @Column(name = "description", columnDefinition = "text(0) COMMENT 'description'")
    private String description;

    @Column(name = "page_id")
    private String pageId;

    @Column(columnDefinition = "bit(1) COMMENT 'isExample'")
    private Boolean isExample = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "flowGroup")
    @Where(clause = "enable_flag=1")
    private MxGraphModel mxGraphModel;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "flowGroup")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<Flow> flowList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "flowGroup")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<FlowGroupPaths> flowGroupPathsList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_GROUP_ID")
    private FlowGroup flowGroup;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "flowGroup")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<FlowGroup> flowGroupList = new ArrayList<>();

}
