package com.nature.component.group.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import com.nature.common.Eunm.PortType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Stop component table
 *
 * @author Nature
 */
@Entity
@Getter
@Setter
@Table(name = "FLOW_STOPS_TEMPLATE")
public class StopsTemplate extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String name;

    private String bundel;

    private String groups;

    private String owner;

    @Column(columnDefinition = "text(0) COMMENT 'description'")
    private String description;

    private String inports;

    @Enumerated(EnumType.STRING)
    private PortType inPortType;

    private String outports;

    @Enumerated(EnumType.STRING)
    private PortType outPortType;

    private String stopGroup;

    private Boolean isCustomized = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stopsTemplate")
    @Where(clause = "enable_flag=1")
    private List<PropertyTemplate> properties = new ArrayList<PropertyTemplate>();

    //	@ManyToMany(mappedBy = "stopsTemplateList")
    @Transient
    private List<StopGroup> stopGroupList = new ArrayList<StopGroup>();
}
