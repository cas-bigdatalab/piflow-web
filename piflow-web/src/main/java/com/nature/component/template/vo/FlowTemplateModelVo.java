package com.nature.component.template.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FlowTemplateModelVo implements Serializable {
    private String id;
    private String name;
    private String description;
}
