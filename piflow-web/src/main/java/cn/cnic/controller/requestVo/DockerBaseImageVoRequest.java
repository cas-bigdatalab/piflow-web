package cn.cnic.controller.requestVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@ApiModel(description = "baseimage")
public class DockerBaseImageVoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "baseImageName", required = true)
    private String baseImageName;

    @ApiModelProperty(value = "baseImageVersion", required = true)
    private String baseImageVersion;

    @ApiModelProperty(value = "baseImageDescription", required = true)
    private String baseImageDescription;

    @ApiModelProperty(value = "harborUser", required = true)
    private String harborUser;

    @ApiModelProperty(value = "harborPassword", required = true)
    private String harborPassword;
}
