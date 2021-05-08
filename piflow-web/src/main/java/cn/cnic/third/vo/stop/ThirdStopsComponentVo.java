package cn.cnic.third.vo.stop;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ThirdStopsComponentVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private String bundle;
    private String owner;
    private String inports;
    private String outports;
    private String groups;
    private boolean isCustomized;
    private String description;
    private String icon;
    private String visualizationType;
    List<ThirdStopsComponentPropertyVo> properties;

}
