package com.nature.component.sysUser.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * 使用JPA定义用户。实现UserDetails接口，用户实体即为springSecurity所使用的用户。
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
