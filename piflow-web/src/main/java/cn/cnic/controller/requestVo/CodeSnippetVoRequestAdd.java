package cn.cnic.controller.requestVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@ApiModel(description = "AddCodeSnippet")
public class CodeSnippetVoRequestAdd implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "noteBook id", required = true)
    private String noteBookId;

    @ApiModelProperty(value = "codeSnippet code content", required = true)
    private String codeContent;

    @ApiModelProperty(value = "codeSnippet sort id", example = "0", required = true)
    private int codeSnippetSort;
}
