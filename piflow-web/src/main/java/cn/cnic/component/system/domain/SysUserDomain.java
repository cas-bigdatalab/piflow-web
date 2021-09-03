package cn.cnic.component.system.domain;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.component.system.entity.SysRole;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.mapper.SysRoleMapper;
import cn.cnic.component.system.mapper.SysUserMapper;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class SysUserDomain {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    public int addSysUser(SysUser sysUser) throws Exception {
        if (null == sysUser) {
            throw new Exception("sysUser is null");
        }
        int insertSysUserAffectedRows = sysUserMapper.insertSysUser(sysUser);
        if (insertSysUserAffectedRows <= 0) {
            throw new Exception("save failed");
        }
        List<SysRole> roles = sysUser.getRoles();
        if (null == roles) {
            throw new Exception("save failed");
        }
        int insertSysRoleListAffectedRows = sysRoleMapper.insertSysRoleList(sysUser.getId(), roles);
        if (insertSysRoleListAffectedRows <= 0) {
            throw new Exception("save failed");
        }
        return insertSysUserAffectedRows + insertSysRoleListAffectedRows;
    }

    public SysUser findUserByUserName(String userName) {
        return sysUserMapper.findUserByUserName(userName);
    }

    public List<SysUser> findUserByName(String name) {
        return sysUserMapper.findUserByName(name);
    }

    public List<SysUser> getUserList() {
        return sysUserMapper.getUserList();
    }

    public long getSysRoleMaxId() {
        return sysRoleMapper.getMaxId();
    }

    public int deleteUserById(String id) {
        if (StringUtils.isBlank(id)) {
            return 0;
        }
        return sysUserMapper.deleteUserById(id);
    }

    public String checkUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return sysUserMapper.checkUsername(username);
    }
}