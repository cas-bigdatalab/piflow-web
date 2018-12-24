package com.nature.component.sysUser.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

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

    private String role;
}
