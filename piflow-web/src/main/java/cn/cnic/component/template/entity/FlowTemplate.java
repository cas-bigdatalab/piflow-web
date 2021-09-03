package cn.cnic.component.template.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.TemplateType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FlowTemplate extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String sourceFlowName;
    private TemplateType templateType;
    private String name;
    private String description;
    private String path;
    private String url;

}
