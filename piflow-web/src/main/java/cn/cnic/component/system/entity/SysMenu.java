package cn.cnic.component.system.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.SysRoleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "SYS_MENU")
public class SysMenu extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;
    
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
