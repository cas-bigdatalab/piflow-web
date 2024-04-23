package cn.cnic.component.dataProduct.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EcosystemType extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
}
