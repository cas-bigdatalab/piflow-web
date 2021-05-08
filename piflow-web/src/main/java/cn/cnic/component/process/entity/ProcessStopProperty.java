package cn.cnic.component.process.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "FLOW_PROCESS_STOP_PROPERTY")
public class ProcessStopProperty extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_PROCESS_STOP_ID")
    private ProcessStop processStop;

    private String name;

    private String displayName;

    @Column(name = "description", columnDefinition = "varchar(1024) COMMENT 'description'")
    private String description;

    @Column(name = "CUSTOM_VALUE")
    private String customValue;

    @Column(name = "ALLOWABLE_VALUES")
    private String allowableValues;

    @Column(name = "PROPERTY_REQUIRED")
    private Boolean required;

    @Column(name = "PROPERTY_SENSITIVE")
    private Boolean sensitive;

}
