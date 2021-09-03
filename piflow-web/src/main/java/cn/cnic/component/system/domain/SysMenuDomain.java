package cn.cnic.component.system.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.component.system.entity.SysMenu;
import cn.cnic.component.system.mapper.SysMenuMapper;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class SysMenuDomain {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    public List<SysMenu> getSysMenuList(String role){
        return sysMenuMapper.getSysMenuList(role);
    }

}