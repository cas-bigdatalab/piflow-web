package com.nature.component.system.service;

import com.nature.base.vo.UserVo;
import com.nature.component.system.vo.SysMenuVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISysInitRecordsService {
    public String addSysInitRecords();

    public String initSample();

}
