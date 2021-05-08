package cn.cnic.component.stopsComponent.model;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.PortType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Stop component table
 */
@Getter
@Setter
@Entity
@Table(name = "FLOW_STOPS_TEMPLATE")
public class StopsComponent extends BaseHibernateModelUUIDNoCorpAgentId {

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

    private String visualizationType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stopsTemplate")
    @Where(clause = "enable_flag=1")
    private List<StopsComponentProperty> properties = new ArrayList<StopsComponentProperty>();

    //    @ManyToMany(mappedBy = "stopsTemplateList")
    @Transient
    private List<StopsComponentGroup> stopGroupList = new ArrayList<>();
}
