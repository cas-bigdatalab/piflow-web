package cn.cnic.component.system.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.component.system.domain.SysMenuDomain;
import cn.cnic.component.system.entity.SysMenu;
import cn.cnic.component.system.entity.SysRole;
import cn.cnic.component.system.service.ISysMenuService;
import cn.cnic.component.system.vo.SysMenuVo;


@Service
public class SysMenuServiceImpl implements ISysMenuService {

    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private SysMenuDomain sysMenuDomain;

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
            List<SysMenu> sysMenuList = sysMenuDomain.getSysMenuList(role);
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
