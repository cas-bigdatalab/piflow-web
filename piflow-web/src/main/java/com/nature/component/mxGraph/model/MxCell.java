package com.nature.component.mxGraph.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "MX_CELL")
public class MxCell extends BaseHibernateModelUUIDNoCorpAgentId {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_MX_GRAPH_ID")
    private MxGraphModel mxGraphModel;

    @Column(name = "MX_PAGEID")
    private String pageId;

    @Column(name = "MX_PARENT")
    private String parent;

    @Column(name = "MX_STYLE")
    private String style;

    @Column(name = "MX_EDGE")
    private String edge; // 线有

    @Column(name = "MX_SOURCE")
    private String source; // 线有

    @Column(name = "MX_TARGET")
    private String target; // 线有

    @Column(name = "MX_VALUE")
    private String value;

    @Column(name = "MX_VERTEX")
    private String vertex;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "mxCell")
    @Where(clause = "enable_flag=1")
    private MxGeometry mxGeometry;

}
