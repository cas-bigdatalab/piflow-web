package com.nature.component.system.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Use JPA to define users. Implement the UserDetails interface, the user entity is the user used by springSecurity.
 *
 * @author Nature
 */

@Getter
@Setter
@Entity
@Table(name = "SYS_USER")
public class SysUser extends BaseHibernateModelUUIDNoCorpAgentId {

    private String username;

    private String password;

    private String name;

    private Integer age;

    private String sex;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sysUser")
    private List<SysRole> roles;
}
