package cn.cnic.component.process.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessStopProperty extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private ProcessStop processStop;
    private String name;
    private String displayName;
    private String description;
    private String customValue;
    private String allowableValues;
    private Boolean required;
    private Boolean sensitive;

}
