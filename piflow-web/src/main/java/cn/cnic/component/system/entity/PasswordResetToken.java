package cn.cnic.component.system.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
/**
 * @ClassName PasswordResetToken
 * @Description TODO
 * @Author ty
 * @Date 2025/5/30 16:28
 * @Version 1.0
 **/
@Getter
@Setter
public class PasswordResetToken extends BaseModelUUIDNoCorpAgentId {

    private String userId;
    private String token;
    private Date expiryDate;
    private boolean used;
}
