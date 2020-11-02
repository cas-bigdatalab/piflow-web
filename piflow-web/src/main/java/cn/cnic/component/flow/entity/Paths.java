package cn.cnic.component.flow.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "FLOW_PATH")
@Setter
@Getter
public class Paths extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_ID")
    private Flow flow;

    @Column(name = "LINE_FROM", columnDefinition = "varchar(255) COMMENT 'line from'")
    private String from;

    @Column(name = "LINE_OUTPORT", columnDefinition = "varchar(255) COMMENT 'line out port'")
    private String outport;

    @Column(name = "LINE_INPORT", columnDefinition = "varchar(255) COMMENT 'line in port'")
    private String inport;

    @Column(name = "LINE_TO", columnDefinition = "varchar(255) COMMENT 'line to'")
    private String to;

    @Column(name = "page_id")
    private String pageId;

    @Column(name = "filter_condition")
    private String filterCondition;
}
