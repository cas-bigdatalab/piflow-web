package cn.cnic.component.system.vo;

import cn.cnic.base.vo.BasePageVo;
import cn.cnic.component.dataProduct.entity.ProductUser;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserOpiniontVo extends BasePageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private Integer state; //0-待解决 1-已解决
    private String bak; //备注


    private String company; // 创建人台站
    private String organization; // 创建人单位

}
