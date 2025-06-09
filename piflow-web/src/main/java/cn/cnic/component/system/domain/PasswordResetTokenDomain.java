package cn.cnic.component.system.domain;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.FileUtils;
import cn.cnic.common.Eunm.FileAssociateType;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.system.entity.File;
import cn.cnic.component.system.entity.PasswordResetToken;
import cn.cnic.component.system.mapper.FileMapper;
import cn.cnic.component.system.mapper.PasswordResetTokenMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class PasswordResetTokenDomain {

    @Autowired
    private PasswordResetTokenMapper  passwordResetTokenMapper;

    public int save(PasswordResetToken resetToken) {
        return passwordResetTokenMapper.save(resetToken);
    }

    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenMapper.findByToken(token);
    }

    public int markAsUsed(String token) {
        return passwordResetTokenMapper.markAsUsed(token);
    }
}
