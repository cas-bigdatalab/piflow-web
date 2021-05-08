package cn.cnic.third.vo.stop;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ThirdStopsComponentPropertyVo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String displayName;
    private String description;
    private String defaultValue;
    private String allowableValues;
    private String required;
    private boolean sensitive;
    private String example;
    private String language;

}
