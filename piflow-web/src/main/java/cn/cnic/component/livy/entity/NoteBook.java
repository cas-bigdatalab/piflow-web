package cn.cnic.component.livy.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class NoteBook extends BaseModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String sessionsId;
    private String codeType;
    private List<CodeSnippet> codeSnippetList = new ArrayList<>();


}
