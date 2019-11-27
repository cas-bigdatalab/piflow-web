package com.nature.component.system.service.Impl;

import com.nature.base.util.LoggerUtil;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.SysRoleType;
import com.nature.component.system.model.SysMenu;
import com.nature.component.system.model.SysRole;
import com.nature.component.system.service.ISysMenuService;
import com.nature.component.system.vo.SysMenuVo;
import com.nature.mapper.system.SysMenuMapper;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements ISysMenuService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenuVo> getSysMenuList(UserVo currentUser) {
        List<SysMenuVo> sysMenuVoList = null;
        if (null != currentUser) {
            String role = "";
            List<SysRole> roles = currentUser.getRoles();
            for (SysRole sysRole : roles) {
                SysRoleType currentUserRole = sysRole.getRole();
                role = currentUserRole.getValue();
                if (SysRoleType.ADMIN == currentUserRole) {
                    break;
                }
            }
            List<SysMenu> sysMenuList = sysMenuMapper.getSysMenuList(role);
            if (null != sysMenuList && sysMenuList.size() > 0) {
                sysMenuVoList = new ArrayList<>();
                SysMenuVo sysMenuVo = null;
                for (SysMenu sysMenu : sysMenuList) {
                    sysMenuVo = new SysMenuVo();
                    BeanUtils.copyProperties(sysMenu, sysMenuVo);
                    sysMenuVoList.add(sysMenuVo);
                }
            } else {
                logger.warn("errorMsg", "not query menu");
            }
        } else {
            logger.warn("No logged-in user was obtained");
        }
        return sysMenuVoList;
    }

}
