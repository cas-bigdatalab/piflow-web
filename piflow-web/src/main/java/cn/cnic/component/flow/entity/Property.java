
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
@Table(name = "FLOW_STOPS_PROPERTY")
public class Property extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_STOPS_ID")
    private Stops stops;

    @Column(columnDefinition = "varchar(255) COMMENT 'name'")
    private String name;

    @Column(columnDefinition = "varchar(255) COMMENT 'display name'")
    private String displayName;

    @Column(columnDefinition = "text(0) COMMENT 'description'")
    private String description;

    @Column(columnDefinition = "text(0) COMMENT 'custom value'")
    private String customValue;

    @Column(name = "ALLOWABLE_VALUES")
    private String allowableValues;

    @Column(name = "PROPERTY_REQUIRED")
    private Boolean required;

    @Column(name = "PROPERTY_SENSITIVE")
    private Boolean sensitive;

    private Boolean isSelect;

    @Column(name = "IS_LOCKED")
    private Boolean isLocked = false;

    @Column(columnDefinition = "bigint(20) COMMENT 'property sort'")
    private Long propertySort;

    @Column(columnDefinition = "bit(1) COMMENT 'Has it been updated'")
    private Boolean isOldData = false;

    @Column(columnDefinition = "text(0) COMMENT 'property example'")
    private String example;

}
