package com.nature.transaction.sysUser;

import com.nature.base.util.LoggerUtil;
import com.nature.component.sysUser.model.SysUser;
import com.nature.mapper.sysUser.SysRoleMapper;
import com.nature.mapper.sysUser.SysUserMapper;
import org.apache.commons.collections.CollectionUtils;
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
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;


    @Transactional
    public List<SysUser> findUserByNameLike(String name) {
        return sysUserMapper.findUserByNameLike(name);
    }

    @Transactional
    public List<SysUser> findUserByName(String name) {
        return sysUserMapper.findUserByName(name);
    }

    @Transactional
    public SysUser findUserByUserName(String userName) {
        return sysUserMapper.findUserByUserName(userName);
    }

    @Transactional
    public List<SysUser> getUserList() {
        return sysUserMapper.getUserList();
    }

    @Transactional
    public int addUser(SysUser user) {
        int rtn = 0;
        int i = sysUserMapper.addSysUser(user);
        if (i > 0) {
            rtn += i;
            if (CollectionUtils.isNotEmpty(user.getRoles())) {
                int j = sysRoleMapper.addSysRoleList(user.getRoles(), user.getId());
                if (j <= 0) {
                    rtn = 0;
                    logger.warn("角色保存失败");
                }
                rtn += j;
            }
        }
        return rtn;
    }

    @Transactional
    public int saveOrUpdate(SysUser user) {
        return sysUserMapper.updateSysUser(user);
    }

    @Transactional
    public int deleteUser(String id) {
        return sysUserMapper.deleteUser(id);
    }
}
