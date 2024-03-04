package cn.cnic.component.dataProduct.vo;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import cn.cnic.base.vo.BasePageVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class ProductUserVo  extends BasePageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; //申请id
    private String productId;
    private String productName;
    private String userId;
    private String userName;
    private String userEmail;
    private String reason;
    private String opinion;
    private Integer state; //0-失效 1-待审核 2-审核通过 3-审核拒绝

    private Date lastUpdateDttm;
    private String lastUpdateUser;


}
