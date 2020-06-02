package cn.cnic.component.system.service;

import cn.cnic.base.vo.UserVo;
import cn.cnic.component.system.vo.SysMenuVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISysMenuService {

    public List<SysMenuVo> getSysMenuList(UserVo currentUser);


}
