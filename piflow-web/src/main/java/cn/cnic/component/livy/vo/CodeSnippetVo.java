package cn.cnic.component.livy.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CodeSnippetVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String codeContent;
    private String executeId;
    private int codeSnippetSort;


}
