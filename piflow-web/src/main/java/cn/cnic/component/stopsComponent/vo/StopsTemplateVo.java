package cn.cnic.component.stopsComponent.vo;

import cn.cnic.common.Eunm.PortType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StopsTemplateVo implements Serializable {
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

    private List<PropertyTemplateVo> propertiesVo = new ArrayList<>();

}
