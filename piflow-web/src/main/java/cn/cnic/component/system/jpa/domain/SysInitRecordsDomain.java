package cn.cnic.component.system.jpa.domain;

import cn.cnic.component.system.entity.SysInitRecords;
import cn.cnic.component.system.jpa.repository.SysInitRecordsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SysInitRecordsDomain {

    @Autowired
    private SysInitRecordsJpaRepository sysInitRecordsJpaRepository;

    public SysInitRecords getSysInitRecordsById(String id) {
        return sysInitRecordsJpaRepository.getOne(id);
    }

    public List<SysInitRecords> getSysInitRecordsList() {
        return sysInitRecordsJpaRepository.findAll();
    }

    @SuppressWarnings("deprecation")
    public SysInitRecords getSysInitRecordsLastNew(int limit) {
        SysInitRecords sysInitRecords = null;
        PageRequest initDate = new PageRequest(0, limit, new Sort(Sort.Direction.DESC, "initDate"));
        List<SysInitRecords> content = sysInitRecordsJpaRepository.findAll(initDate).getContent();
        if (null != content && content.size() > 0) {
            sysInitRecords = content.get(0);
        }
        return sysInitRecords;
    }

    public SysInitRecords saveOrUpdate(SysInitRecords sysInitRecords) {
        return sysInitRecordsJpaRepository.save(sysInitRecords);
    }

    public List<SysInitRecords> saveOrUpdate(List<SysInitRecords> sysInitRecordsList) {
        return sysInitRecordsJpaRepository.saveAll(sysInitRecordsList);
    }

}
