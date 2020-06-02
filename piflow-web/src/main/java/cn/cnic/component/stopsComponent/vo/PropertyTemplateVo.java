package cn.cnic.component.stopsComponent.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PropertyTemplateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String stopsTemplate;

    private String name;

    private String displayName;

    private String description;

    private String defaultValue;

    private String allowableValues;

    private Boolean required;

    private Boolean sensitive;

}
