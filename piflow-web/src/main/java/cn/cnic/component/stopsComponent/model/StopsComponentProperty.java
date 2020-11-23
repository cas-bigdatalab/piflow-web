package cn.cnic.component.stopsComponent.model;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "FLOW_STOPS_PROPERTY_TEMPLATE")
public class StopsComponentProperty extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    // @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "FK_STOPS_ID")
    private String stopsTemplate;

    private String name;

    @Column(columnDefinition = "text(0) COMMENT 'description'")
    private String displayName;

    @Column(columnDefinition = "text(0) COMMENT 'description'")
    private String description;

    @Column(columnDefinition = "text(0) COMMENT 'default value'")
    private String defaultValue;

    @Column(columnDefinition = "text(0) COMMENT 'default value'")
    private String allowableValues;

    @Column(name = "PROPERTY_REQUIRED")
    private Boolean required;

    @Column(name = "PROPERTY_SENSITIVE")
    private Boolean sensitive;

    @Column(columnDefinition = "bigint(20) COMMENT 'property sort'")
    private Long propertySort;

    @Column(columnDefinition = "text(0) COMMENT 'property example'")
    private String example;

    private String language;
}
