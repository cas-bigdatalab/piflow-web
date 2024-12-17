package cn.cnic.component.system.vo;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.component.dataProduct.entity.EcosystemType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    private String phoneNumber;
    private String email;
    private String company; // 所属台站
    private SysRole role;
    private String organization; // 所属单位

    private List<EcosystemType> ecosystemTypes;   //生态站专用，所属生态系统类型
    private String ecosystemTypeIds;

    public String getCreateTime() {
        return DateUtils.dateTimesToStr(crtDttm);
    }
}
