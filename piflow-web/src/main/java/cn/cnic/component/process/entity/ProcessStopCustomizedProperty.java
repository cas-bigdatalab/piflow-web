
package cn.cnic.component.process.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * stop property
 */
@Getter
@Setter
@Entity
@Table(name = "PROCESS_STOPS_CUSTOMIZED_PROPERTY")
public class ProcessStopCustomizedProperty extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_PROCESS_STOP_ID")
    private ProcessStop processStop;

    @Column(columnDefinition = "varchar(255) COMMENT 'name'")
    private String name;

    @Column(columnDefinition = "text(0) COMMENT 'custom value'")
    private String customValue;

    @Column(name = "description", columnDefinition = "text(0) COMMENT 'description'")
    private String description;
}
