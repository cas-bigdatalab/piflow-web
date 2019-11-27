package com.nature.component.template.vo;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class FlowGroupTemplateVo implements Serializable {

    private String id;
    private Date crtDttm;
    private String flowGroupName;
    private String name;
    private String description;
    private String path;

}
