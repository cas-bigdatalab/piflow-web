package com.nature.component.stopsComponent.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "FLOW_STOPS_PROPERTY_TEMPLATE")
public class PropertyTemplate extends BaseHibernateModelUUIDNoCorpAgentId {

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
}
