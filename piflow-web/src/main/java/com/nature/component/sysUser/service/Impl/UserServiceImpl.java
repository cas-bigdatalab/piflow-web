package com.nature.component.sysUser.service.Impl;

import com.nature.component.sysUser.model.SysUser;
import com.nature.component.sysUser.service.IUserService;
import com.nature.transaction.sysUser.SysUserTransaction;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private SysUserTransaction sysUserTransaction;

    public SysUser findByUsername(String useename){
        return sysUserTransaction.findUserByUserName(useename);
    }

    public List<SysUser> findByName(String name) {
        if (StringUtils.isBlank(name)) {
            name = "";
        }
        return sysUserTransaction.findUserByName(name);
    }

    public List<SysUser> getUserList() {
        List<SysUser> listUser = sysUserTransaction.getUserList();
        return listUser;
    }

    public SysUser addUser(SysUser user) {
        sysUserTransaction.addUser(user);
        return user;
    }

    public int saveOrUpdate(SysUser user) {
        int update = sysUserTransaction.saveOrUpdate(user);
        return update;
    }

    public int deleteUser(int id) {
        int delete = sysUserTransaction.deleteUser(id);
        return delete;
    }
}
