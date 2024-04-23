package cn.cnic.component.dataProduct.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EcosystemTypeAssociate extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private Long ecosystemTypeId;
    private String ecosystemTypeName;
    private String associateId;
    private Integer associateType;
}
