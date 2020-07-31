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
@Table(name = "FLOW")
@Setter
@Getter
public class Flow extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(255) COMMENT 'flow name'")
    private String name;

    @Column(columnDefinition = "varchar(255) COMMENT 'flow uuid'")
    private String uuid;

    @Column(columnDefinition = "varchar(255) COMMENT 'driverMemory'")
    private String driverMemory = "1g";

    @Column(columnDefinition = "varchar(255) COMMENT 'executorNumber'")
    private String executorNumber = "1";

    @Column(columnDefinition = "varchar(255) COMMENT 'executorMemory'")
    private String executorMemory = "1g";

    @Column(columnDefinition = "varchar(255) COMMENT 'executorCores'")
    private String executorCores = "1";

    @Column(name = "description", columnDefinition = "text(0) COMMENT 'description'")
    private String description;

    @Column(name = "page_id")
    private String pageId;

    @Column(columnDefinition = "bit(1) COMMENT 'isExample'")
    private Boolean isExample = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_GROUP_ID")
    private FlowGroup flowGroup;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "flow")
    @Where(clause = "enable_flag=1")
    private MxGraphModel mxGraphModel;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "flow")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<Stops> stopsList = new ArrayList<Stops>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "flow")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<Paths> pathsList = new ArrayList<Paths>();

}
