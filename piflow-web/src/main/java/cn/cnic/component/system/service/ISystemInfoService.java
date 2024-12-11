package cn.cnic.component.system.service;

import cn.cnic.component.system.vo.SystemInfoVo;

public interface ISystemInfoService {

    String update(SystemInfoVo systemInfoVo);

    String select();
}
