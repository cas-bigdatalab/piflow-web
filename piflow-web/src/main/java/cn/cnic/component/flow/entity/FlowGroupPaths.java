package cn.cnic.component.flow.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FlowGroupPaths extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private FlowGroup flowGroup;
    private String from;
    private String outport;
    private String inport;
    private String to;
    private String pageId;
    private String filterCondition;
}
