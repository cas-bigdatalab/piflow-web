package com.nature.domain.system;

import com.nature.component.system.model.SysInitRecords;
import com.nature.repository.system.SysInitRecordsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.OrderBy;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
