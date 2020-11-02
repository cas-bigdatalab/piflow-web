package cn.cnic.component.mxGraph.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "MX_GRAPH_MODEL")
public class MxGraphModel extends BaseHibernateModelUUIDNoCorpAgentId {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_ID")
    private Flow flow;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_GROUP_ID")
    private FlowGroup flowGroup;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_PROCESS_ID")
    private Process process;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_PROCESS_GROUP_ID")
    private ProcessGroup processGroup;

    @Column(name = "MX_DX")
    private String dx;

    @Column(name = "MX_DY")
    private String dy;

    @Column(name = "MX_GRID")
    private String grid;

    @Column(name = "MX_GRIDSIZE")
    private String gridSize;

    @Column(name = "MX_GUIDES")
    private String guides;

    @Column(name = "MX_TOOLTIPS")
    private String tooltips;

    @Column(name = "MX_CONNECT")
    private String connect;

    @Column(name = "MX_ARROWS")
    private String arrows;

    @Column(name = "MX_FOLD")
    private String fold;

    @Column(name = "MX_PAGE")
    private String page;

    @Column(name = "MX_PAGESCALE")
    private String pageScale;

    @Column(name = "MX_PAGEWIDTH")
    private String pageWidth;

    @Column(name = "MX_PAGEHEIGHT")
    private String pageHeight;

    @Column(name = "MX_BACKGROUND")
    private String background;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "mxGraphModel")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<MxCell> root = new ArrayList<>();

}
