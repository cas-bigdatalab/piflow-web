package cn.cnic.component.dataProduct.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductUser extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private Long productId;
    private String productName;
    private String userId;
    private String userName;
    private String userEmail;
    private String reason;
    private String opinion;
    private Integer state; //0-失效 1-待审核 2-审核通过 3-审核拒绝
}
