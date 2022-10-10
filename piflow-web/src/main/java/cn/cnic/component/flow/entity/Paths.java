package cn.cnic.component.flow.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Paths extends BaseModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private Flow flow;
    private String from;
    private String outport;
    private String inport;
    private String to;
    private String pageId;
    private String filterCondition;
}
