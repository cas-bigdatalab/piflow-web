package cn.cnic.controller.requestVo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Stop component table
 */
@Getter
@Setter
@ApiModel(description = "Update stops is show request data")
public class UpdatestopsComponentIsShow implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "bundle", required = true)
    private String[] bundle;

    @ApiModelProperty(value = "stopsGroups", required = true)
    private String[] stopsGroups;

    @ApiModelProperty(value = "isShow", required = true)
    private Boolean isShow = true;
}
