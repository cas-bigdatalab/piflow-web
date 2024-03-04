package cn.cnic.component.dataProduct.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductTypeAssociate extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private Long productTypeId;
    private String productTypeName;
    private String associateId;
    private Integer associateType;
    private Integer state;//状态 0-已删除 1-感兴趣 2-不感兴趣
}
