package com.nature.component.template.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import com.nature.common.Eunm.TemplateType;
import com.nature.component.flow.model.Flow;
import com.nature.component.template.model.StopTemplateModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
