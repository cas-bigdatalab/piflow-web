package cn.cnic.component.system.domain;

import cn.cnic.component.system.entity.SysLog;
import cn.cnic.component.system.mapper.AdminLogMapper;
import cn.cnic.component.system.vo.SysLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class AdminLogDomain {

    private final AdminLogMapper adminLogMapper;

    @Autowired
    public AdminLogDomain(AdminLogMapper adminLogMapper) {
        this.adminLogMapper = adminLogMapper;
    }

    public List<SysLogVo> getLogList(boolean isAdmin, String username, String param){
        return adminLogMapper.getLogList(isAdmin, username, param);
    }

    public int insertSelective(SysLog record){
        return adminLogMapper.insertSelective(record);
    }

}