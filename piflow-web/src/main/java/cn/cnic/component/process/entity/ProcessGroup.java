package cn.cnic.component.process.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.ProcessParentType;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "FLOW_PROCESS_GROUP")
public class ProcessGroup extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(255) COMMENT 'Process name'")
    private String name;

    @Column(columnDefinition = "text COMMENT 'Process view xml string'")
    private String viewXml;

    @Column(columnDefinition = "varchar(1024) COMMENT 'description'")
    private String description;

    @Column(name = "page_id")
    private String pageId;

    @Column(columnDefinition = "varchar(255) COMMENT 'flowId'")
    private String flowId;

    @Column(columnDefinition = "varchar(255) COMMENT 'The id returned when calling runProcess'")
    private String appId;

    @Column(columnDefinition = "varchar(255) COMMENT 'third parentProcessId'")
    private String parentProcessId;

    @Column(columnDefinition = "varchar(255) COMMENT 'third processId'")
    private String processId;

    @Column(columnDefinition = "varchar(255) COMMENT 'Process status'")
    @Enumerated(EnumType.STRING)
    private ProcessState state;

    @Column(columnDefinition = "datetime  COMMENT 'Process startup time'")
    private Date startTime;

    @Column(columnDefinition = "datetime  COMMENT 'End time of the process'")
    private Date endTime;

    @Column(columnDefinition = "varchar(255) COMMENT 'Process progress'")
    private String progress;

    @Column(columnDefinition = "varchar(255) COMMENT 'Process RunModeType'")
    @Enumerated(EnumType.STRING)
    private RunModeType runModeType = RunModeType.RUN;

    @Column(columnDefinition = "varchar(255) COMMENT 'Process parent type'")
    @Enumerated(EnumType.STRING)
    private ProcessParentType processParentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_PROCESS_GROUP_ID")
    private ProcessGroup processGroup;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "processGroup")
    @Where(clause = "enable_flag=1")
    private MxGraphModel mxGraphModel;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "processGroup")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<Process> processList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "processGroup")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<ProcessGroupPath> processGroupPathList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "processGroup")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<ProcessGroup> processGroupList = new ArrayList<>();

}
