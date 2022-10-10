package cn.cnic.component.process.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessPath extends BaseModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private Process process;
    private String from;
    private String outport;
    private String inport;
    private String to;
    private String pageId;

}
