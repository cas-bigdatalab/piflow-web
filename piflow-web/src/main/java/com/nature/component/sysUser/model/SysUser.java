package com.nature.component.sysUser.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 使用JPA定义用户。实现UserDetails接口，用户实体即为springSecurity所使用的用户。
 *
 * @author Nature
 */
@Entity
@Table(name = "SYS_USER")
public class SysUser extends BaseHibernateModelUUIDNoCorpAgentId {

    private String username;

    private String password;

    private String name;

    private Integer age;

    private String sex;

    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
