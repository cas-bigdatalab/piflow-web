package com.nature.component.system.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import com.nature.common.Eunm.SysRoleType;
import com.nature.common.Eunm.TaskState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "MENU")
public class Menu extends BaseHibernateModelUUIDNoCorpAgentId {

    @Column(columnDefinition = "varchar(255) COMMENT 'menu name'")
    private String menuName;

    @Column(columnDefinition = "varchar(255) COMMENT 'menu url'")
    private String menuUrl;

    @Column(columnDefinition = "varchar(255) COMMENT 'task status'")
    private String menuParent;

    @Column(columnDefinition = "varchar(255) COMMENT 'task status'")
    @Enumerated(EnumType.STRING)
    private SysRoleType menuJurisdiction;

    @Column(columnDefinition = "varchar(1024) COMMENT 'description'")
    private String menuDescription;
}
