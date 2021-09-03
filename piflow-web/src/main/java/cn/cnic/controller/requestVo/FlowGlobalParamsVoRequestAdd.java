package cn.cnic.controller.requestVo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(description = "flowGlobalParams")
public class FlowGlobalParamsVoRequestAdd implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "Global params name", required = true)
    private String name;
    
    @ApiModelProperty(value = "Global params type", required = true)
    private String type;
    
    @ApiModelProperty(value = "Global params content", required = true)
    private String content;

}
