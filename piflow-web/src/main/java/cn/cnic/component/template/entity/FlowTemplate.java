package cn.cnic.component.template.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.TemplateType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "flow_template")
public class FlowTemplate extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(255) COMMENT 'source flow name'")
    private String sourceFlowName;

    @Column(columnDefinition = "varchar(255) COMMENT 'template type'")
    @Enumerated(EnumType.STRING)
    private TemplateType templateType;

    @Column(columnDefinition = "varchar(255) COMMENT 'template name'")
    private String name;

    @Column(columnDefinition = "varchar(1024) COMMENT 'description'")
    private String description;

    private String path;

    private String url;

}
