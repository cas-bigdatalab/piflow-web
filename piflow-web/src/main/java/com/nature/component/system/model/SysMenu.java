package com.nature.component.system.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import com.nature.common.Eunm.SysRoleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "SYS_MENU")
public class SysMenu extends BaseHibernateModelUUIDNoCorpAgentId {

    @Column(columnDefinition = "varchar(255) COMMENT 'menu name'")
    private String menuName;

    @Column(columnDefinition = "varchar(255) COMMENT 'menu url'")
    private String menuUrl;

    @Column(columnDefinition = "varchar(255) COMMENT 'menu parent'")
    private String menuParent;

    @Column(columnDefinition = "varchar(255) COMMENT 'jurisdiction'")
    @Enumerated(EnumType.STRING)
    private SysRoleType menuJurisdiction;

    @Column(columnDefinition = "varchar(1024) COMMENT 'description'")
    private String menuDescription;

    @Column(columnDefinition = "int(11) COMMENT 'menu sort'")
    private Integer menuSort = 9;
}
