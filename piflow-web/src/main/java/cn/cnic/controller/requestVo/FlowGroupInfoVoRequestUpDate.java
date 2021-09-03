package cn.cnic.controller.requestVo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

@Getter
@Setter
public class FlowGroupInfoVoRequestUpDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "FlowGroup id", required = true)
    private String id;
    
    @ApiModelProperty(value = "FlowGroup name", required = true)
    private String name;
    
    @ApiModelProperty(value = "FlowGroup description")
    private String description;
    
    @ApiModelProperty(value = "FlowGroup pageId")
    private String pageId;
    
}
