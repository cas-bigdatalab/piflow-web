package cn.cnic.component.stopsComponent.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.PortType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Stop component table
 */
@Setter
@Getter
public class StopsComponent extends BaseModelUUIDNoCorpAgentId {

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
    private Boolean isDataSource = false;
    private List<StopsComponentProperty> properties = new ArrayList<StopsComponentProperty>();

    private List<StopsComponentGroup> stopGroupList = new ArrayList<>();

    private String imageUrl;
}
