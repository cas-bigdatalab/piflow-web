package cn.cnic.component.stopsComponent.entity;

import java.util.ArrayList;
import java.util.List;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.PortType;
import lombok.Getter;
import lombok.Setter;

/**
 * Stop component table
 */
@Setter
@Getter
public class StopsComponent extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String name;
    private String bundel;
    private String groups;
    private String owner;
    private String description;
    private String inports;
    private PortType inPortType;
    private String outports;
    private PortType outPortType;
    private String stopGroup;
    private Boolean isCustomized = false;
    private String visualizationType;

    private List<StopsComponentProperty> properties = new ArrayList<StopsComponentProperty>();

    private List<StopsComponentGroup> stopGroupList = new ArrayList<>();
}
