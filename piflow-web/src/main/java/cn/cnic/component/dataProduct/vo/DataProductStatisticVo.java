package cn.cnic.component.dataProduct.vo;

import cn.cnic.base.vo.BasePageVo;
import cn.cnic.component.dataProduct.entity.ProductUser;
import cn.cnic.component.system.vo.FileVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class DataProductStatisticVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long totalDataProductNum;//数据产品个数
    private Long totalDataProductSize;//数据产品体量 单位字节，需换算成KB、MB、GB、TB
    private Long totalDataProductLine;//数据条数
    private Long totalCompanyNum;//台站数量

}
