package cn.cnic.controller.requestVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@ApiModel(description = "CodeSnippet")
public class RequesCodeSnippetVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "noteBook id")
    private String noteBookId;
    
    @ApiModelProperty(value = "codeSnippet id")
    private String id;

    @ApiModelProperty(value = "codeSnippet code content", required = true)
    private String codeContent;
    
    @ApiModelProperty(value = "codeSnippet execute id")
    private String executeId;

    @ApiModelProperty(value = "codeSnippet sort id")
    private String codeSnippetSort;
}
