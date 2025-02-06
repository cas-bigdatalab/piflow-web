package cn.cnic.component.system.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import cn.cnic.component.dataProduct.entity.ProductUser;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserOpinion extends BaseModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private Integer state; //0-待解决 1-已解决
    private String bak; //备注


    private String company; // 创建人台站
    private String organization; // 创建人单位
}
