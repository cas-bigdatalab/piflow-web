package cn.cnic.component.stopsComponent.vo;

import cn.cnic.common.Eunm.PortType;

import lombok.Getter;
import lombok.Setter;

/**
 * Stop component table
 */
@Getter
@Setter
public class StopsComponentVo {

    private String id;
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
    private Boolean isShow = true;

}
