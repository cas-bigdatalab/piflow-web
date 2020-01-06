package com.nature.component.system.service.Impl;

import com.nature.base.util.SqlUtils;
import com.nature.common.Eunm.SysRoleType;
import com.nature.component.system.model.SysRole;
import com.nature.component.system.model.SysUser;
import com.nature.component.system.service.ISysUserService;
import com.nature.component.system.vo.SysUserVo;
import com.nature.transaction.sysUser.SysUserTransaction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserTransaction sysUserTransaction;

    @Override
	public SysUser findByUsername(String username) {
        return sysUserTransaction.findUserByUserName(username);
    }

    @Override
	public List<SysUser> findByName(String name) {
        if (StringUtils.isBlank(name)) {
            name = "";
        }
        return sysUserTransaction.findUserByName(name);
    }

    @Override
	public List<SysUser> getUserList() {
        List<SysUser> listUser = sysUserTransaction.getUserList();
        return listUser;
    }

    @Override
	public SysUser addUser(SysUser user) {
        sysUserTransaction.addUser(user);
        return user;
    }

    @Override
	public int saveOrUpdate(SysUser user) {
        int update = sysUserTransaction.saveOrUpdate(user);
        return update;
    }

    @Override
	public int deleteUser(String id) {
        int delete = sysUserTransaction.deleteUser(id);
        return delete;
    }

    @Override
	public int registerUser(SysUserVo sysUserVo) {
        if (null != sysUserVo) {
            String username = sysUserVo.getUsername();
            String password = sysUserVo.getPassword();
            // Determine if it is empty
            if (!StringUtils.isAnyEmpty(username, password)) {
                //Encrypted password
                password = new BCryptPasswordEncoder().encode(password);
                SysUser sysUser = new SysUser();
                sysUser.setId(SqlUtils.getUUID32());
                sysUser.setCrtDttm(new Date());
                sysUser.setCrtUser("system");
                sysUser.setLastUpdateDttm(new Date());
                sysUser.setLastUpdateUser("system");
                sysUser.setEnableFlag(true);
                sysUser.setUsername(username);
                sysUser.setPassword(password);
                sysUser.setName(sysUserVo.getName());
                sysUser.setAge(sysUserVo.getAge());
                sysUser.setSex(sysUserVo.getSex());
                List<SysRole> sysUsers = new ArrayList<>();
                SysRole sysRole = new SysRole();
                sysRole.setRole(SysRoleType.USER);
                sysUsers.add(sysRole);
                sysUser.setRoles(sysUsers);
                return sysUserTransaction.addUser(sysUser);
            }
        }
        return 0;
    }
}
