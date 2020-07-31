package cn.cnic.component.template.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.TemplateType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "FLOW_GROUP_TEMPLATE")
public class FlowGroupTemplate extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(255) COMMENT 'source flow name'")
    private String flowGroupName;

    @Column(columnDefinition = "varchar(255) COMMENT 'template type'")
    @Enumerated(EnumType.STRING)
    private TemplateType templateType;

    private String name;

    @Column(name = "description", columnDefinition = "varchar(1024) COMMENT 'description'")
    private String description;

    private String path;

}
