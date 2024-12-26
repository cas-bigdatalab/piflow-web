package cn.cnic.component.system.vo;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.component.dataProduct.entity.EcosystemType;
import cn.cnic.component.system.entity.SysRole;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ForgetUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String password; //新密码
    private String name; //姓名
    private String phoneNumber; //电话
    private String email;//邮箱
    private String company; // 所属台站
    private String organization; // 所属单位

    private List<EcosystemType> ecosystemTypes;   //生态站专用，所属生态系统类型
    private String stationManagerName;//台站管理员姓名

}
