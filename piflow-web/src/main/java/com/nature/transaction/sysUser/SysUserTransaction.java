package com.nature.transaction.sysUser;

import com.nature.base.util.LoggerUtil;
import com.nature.component.sysUser.model.SysUser;
import com.nature.mapper.sysUser.SysUserMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class SysUserTransaction {

    /**
     * 引入日志，注意都是"org.slf4j"包下
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private SysUserMapper sysUserMapper;


    public List<SysUser> findUserByName(String name) {
        return sysUserMapper.findUserByName(name);
    }

    public SysUser findUserByNames(String name) {
        return sysUserMapper.findUserByNames(name);
    }

    public SysUser findUserByUserName(String userName) {
        return sysUserMapper.findUserByUserName(userName);
    }

    ;

    public List<SysUser> getUserList() {
        return sysUserMapper.getUserList();
    }

    public int addUser(SysUser user) {
        return sysUserMapper.addUser(user);
    }

    public int saveOrUpdate(SysUser user) {
        return sysUserMapper.saveOrUpdate(user);
    }

    public int deleteUser(int id) {
        return sysUserMapper.deleteUser(id);
    }
}
