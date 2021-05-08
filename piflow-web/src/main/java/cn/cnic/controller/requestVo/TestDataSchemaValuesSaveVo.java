package cn.cnic.controller.requestVo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(description = "save TestData")
public class TestDataSchemaValuesSaveVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    

    @ApiModelProperty(value = "testData Id", required = true)
    private String testDataId;
    
    @ApiModelProperty(value = "schemaValues")
    private SchemaValuesVo[] schemaValuesList;
}
