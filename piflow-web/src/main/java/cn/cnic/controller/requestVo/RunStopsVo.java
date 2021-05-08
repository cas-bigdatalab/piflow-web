package cn.cnic.controller.requestVo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "run stops request data")
public class RunStopsVo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "stops id", required = true)
    private String stopsId;
    
    @ApiModelProperty(value = "is run follow-up", required = true)
    private Boolean isRunFollowUp;
    
    @ApiModelProperty(value = "port array")
    private String ports[];
    
    @ApiModelProperty(value = "testData Id array")
    private String testDataIds[];

}
