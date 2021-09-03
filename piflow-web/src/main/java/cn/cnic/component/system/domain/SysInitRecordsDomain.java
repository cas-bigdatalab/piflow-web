package cn.cnic.component.system.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.component.system.entity.SysInitRecords;
import cn.cnic.component.system.mapper.SysInitRecordsMapper;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class SysInitRecordsDomain {

    @Autowired
    private SysInitRecordsMapper sysInitRecordsMapper;

    public Integer insertSysInitRecords(SysInitRecords sysInitRecords) {
        return sysInitRecordsMapper.insertSysInitRecords(sysInitRecords);

    }

    public List<SysInitRecords> getSysInitRecordsList() {
        return sysInitRecordsMapper.getSysInitRecordsList();
    }


    public SysInitRecords getSysInitRecordsById(String id) {
        return sysInitRecordsMapper.getSysInitRecordsById(id);
    }

    public SysInitRecords getSysInitRecordsLastNew(int limit) {
        return sysInitRecordsMapper.getSysInitRecordsLastNew(limit);
    }

}