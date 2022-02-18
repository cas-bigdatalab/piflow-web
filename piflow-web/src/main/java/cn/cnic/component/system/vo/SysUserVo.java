package cn.cnic.component.system.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import cn.cnic.component.system.entity.SysRole;

@Getter
@Setter
public class SysUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String password;
    private String name;
    private Integer age;
    private String sex;
    private Byte status;
    private String lastLoginIp;
    private SysRole role;

}
