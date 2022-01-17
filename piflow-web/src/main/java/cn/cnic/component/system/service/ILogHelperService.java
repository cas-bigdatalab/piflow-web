package cn.cnic.component.system.service;

import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.vo.SysUserVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ILogHelperService {

    public void logAuthSucceed(String action, String result);
    public void logAdmin(Integer type, String action, Boolean succeed, String result, String comment);
}
