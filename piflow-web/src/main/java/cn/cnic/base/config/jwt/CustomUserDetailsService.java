package cn.cnic.base.config.jwt;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.component.system.mapper.SysMenuMapper;
import cn.cnic.component.system.mapper.SysUserMapper;
import cn.cnic.component.system.model.SysMenu;
import cn.cnic.component.system.model.SysRole;
import cn.cnic.component.system.model.SysUser;
import cn.cnic.component.system.vo.SysMenuVo;

/**
 * 登陆身份认证
 * Author: Nature
 * createAt: 2020/6/30
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public UserVo loadUserByUsername(String username) throws UsernameNotFoundException {

        UserVo userVo = null;
        SysUser sysUser = sysUserMapper.findUserByUserName(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        if (null != sysUser) {
            userVo = new UserVo();
            userVo.setUsername(sysUser.getUsername());
            userVo.setPassword(sysUser.getPassword());
            userVo.setName(sysUser.getName());
            userVo.setAge(sysUser.getAge());
            List<SysRole> sysRoleTypes = sysUser.getRoles();
            userVo.setRoles(sysRoleTypes);
            if (CollectionUtils.isNotEmpty(sysRoleTypes)) {
                SysRoleType sysRoleHighest = null;
                String[] valueArray = new String[sysRoleTypes.size()];
                for (int i = 0; i < sysRoleTypes.size(); i++) {
                    SysRole sysRole = sysRoleTypes.get(i);
                    if (null != sysRole && null != sysRole.getRole()) {
                        SysRoleType role = sysRole.getRole();
                        valueArray[i] = sysRole.getRole().getValue();
                        if (role == SysRoleType.ADMIN) {
                            sysRoleHighest = role;
                        }
                    }

                }
                if (valueArray.length > 0) {
                    userVo.setAuthorities(AuthorityUtils.createAuthorityList(valueArray));
                    if (null == sysRoleHighest) {
                        sysRoleHighest = SysRoleType.USER;
                    }
                }
                List<SysMenu> sysMenuList = sysMenuMapper.getSysMenuList(sysRoleHighest.getValue());
                if (CollectionUtils.isNotEmpty(sysMenuList)) {
                    List<SysMenuVo> sysMenuVoList = new ArrayList<>();
                    SysMenuVo sysMenuVo;
                    for (SysMenu sysMenu : sysMenuList) {
                        sysMenuVo = new SysMenuVo();
                        BeanUtils.copyProperties(sysMenu, sysMenuVo);
                        sysMenuVoList.add(sysMenuVo);
                    }
                    userVo.setSysMenuVoList(sysMenuVoList);
                }
            }
        }
        return userVo;

    }
}