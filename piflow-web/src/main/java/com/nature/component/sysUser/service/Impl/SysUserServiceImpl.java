package com.nature.component.sysUser.service.Impl;

import com.nature.base.util.Utils;
import com.nature.component.sysUser.model.SysUser;
import com.nature.component.sysUser.service.ISysUserService;
import com.nature.component.sysUser.vo.SysUserVo;
import com.nature.transaction.sysUser.SysUserTransaction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Resource
    private SysUserTransaction sysUserTransaction;

    public SysUser findByUsername(String useename) {
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

    public int deleteUser(String id) {
        int delete = sysUserTransaction.deleteUser(id);
        return delete;
    }

    public int registerUser(SysUserVo sysUserVo) {
        if (null != sysUserVo) {
            String username = sysUserVo.getUsername();
            String password = sysUserVo.getPassword();
            // 判空
            if (!StringUtils.isAnyEmpty(username, password)) {
                //加密密码
                password = new BCryptPasswordEncoder().encode(password);
                SysUser sysUser = new SysUser();
                sysUser.setId(Utils.getUUID32());
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
                sysUser.setRole("User");
                return sysUserTransaction.addUser(sysUser);
            }
        }
        return 0;
    }
}
