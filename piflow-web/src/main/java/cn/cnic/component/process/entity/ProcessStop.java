package cn.cnic.component.process.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.Eunm.StopState;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "FLOW_PROCESS_STOP")
public class ProcessStop extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_PROCESS_ID")
    private Process process;

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

    @Enumerated(EnumType.STRING)
    private StopState state = StopState.INIT;

    private Date startTime;

    private Date endTime;

    @Column(name = "page_id")
    private String pageId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "processStop")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<ProcessStopProperty> processStopPropertyList = new ArrayList<ProcessStopProperty>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "processStop")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<ProcessStopCustomizedProperty> processStopCustomizedPropertyList = new ArrayList<>();
}
