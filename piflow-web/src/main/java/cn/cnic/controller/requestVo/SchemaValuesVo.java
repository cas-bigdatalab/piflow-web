package cn.cnic.controller.requestVo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(description = "save TestData")
public class SchemaValuesVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    

    @ApiModelProperty(value = "schema name")
    private String schemaName;

    @ApiModelProperty(value = "schemaValues id")
    private String schemaValueId;
    
    @ApiModelProperty(value = "schema value")
    private String schemaValue;
    
    @ApiModelProperty(value = "schema value row", example = "0")
    private int dataRow;

    @ApiModelProperty(value = "delete or not")
    private boolean isDelete;

}
