package cn.cnic.component.system.vo;

import cn.cnic.base.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

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
    private Date crtDttm;
    private Byte status;
    private String lastLoginIp;
    private SysRole role;

    public String getCreateTime() {
        return DateUtils.dateTimesToStr(crtDttm);
    }
}
