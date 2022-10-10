package cn.cnic.component.livy.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CodeSnippet extends BaseModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String codeContent;
    private String executeId;
    private int codeSnippetSort;

    private NoteBook noteBook;


}
