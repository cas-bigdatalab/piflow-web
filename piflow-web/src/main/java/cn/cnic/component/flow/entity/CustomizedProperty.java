
package cn.cnic.component.flow.entity;

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
@Table(name = "FLOW_STOPS_CUSTOMIZED_PROPERTY")
public class CustomizedProperty extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_STOPS_ID")
    private Stops stops;

    @Column(columnDefinition = "varchar(255) COMMENT 'name'")
    private String name;

    @Column(columnDefinition = "text(0) COMMENT 'custom value'")
    private String customValue;

    @Column(name = "description", columnDefinition = "text(0) COMMENT 'description'")
    private String description;
}
