package cn.cnic.component.system.transactional;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.component.system.mapper.SysRoleMapper;
import cn.cnic.component.system.mapper.SysUserMapper;
import cn.cnic.component.system.entity.SysRole;
import cn.cnic.component.system.entity.SysUser;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class SysUserTransactional {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    public int addSysUser(SysUser sysUser) throws Exception {
        if (null == sysUser) {
            return 0;
        }
        int i = sysUserMapper.insertSysUser(sysUser);
        if (i <= 0) {
            throw new Exception("save failed");
        }
        List<SysRole> roles = sysUser.getRoles();
        if (null == roles) {
            throw new Exception("save failed");
        }
        int i1 = sysRoleMapper.insertSysRoleList(sysUser.getId(), roles);
        if (i1 <= 0) {
            throw new Exception("save failed");
        }
        return i + i1;
    }

    public SysUser findUserByUserName(String userName) {
        return sysUserMapper.findUserByUserName(userName);
    }
    public List<SysUser> findUserByName(String name) {
        return sysUserMapper.findUserByName(name);
    }

    public List<SysUser> getUserList(){
        return sysUserMapper.getUserList();
    }

    public long getSysRoleMaxId() {
        return sysRoleMapper.getMaxId();
    }
}