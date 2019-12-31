package com.nature.domain.system;

import com.nature.common.Eunm.ScheduleState;
import com.nature.component.system.model.SysSchedule;
import com.nature.repository.system.SysScheduleJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Component
public class SysScheduleDomain {

    @Autowired
    private SysScheduleJpaRepository sysScheduleJpaRepository;

    private Specification<SysSchedule> addEnableFlagParam() {
        Specification<SysSchedule> specification = new Specification<SysSchedule>() {
        	private static final long serialVersionUID = 1L;
        	
            @Override
            public Predicate toPredicate(Root<SysSchedule> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    public SysSchedule getSysScheduleById(String id) {
        SysSchedule sysSchedule = null;
        Optional<SysSchedule> sysScheduleOptional = sysScheduleJpaRepository.findById(id);
        if ("Optional.empty" != sysScheduleOptional.toString()) {
            sysSchedule = sysScheduleOptional.get();
        }
        if (null != sysSchedule && !sysSchedule.getEnableFlag()) {
            sysSchedule = null;
        }
        return sysSchedule;
    }

    public List<SysSchedule> getSysScheduleList() {
        return sysScheduleJpaRepository.findAll(addEnableFlagParam());
    }

    public SysSchedule saveOrUpdate(SysSchedule sysSchedule) {
        return sysScheduleJpaRepository.save(sysSchedule);
    }

    public List<SysSchedule> saveOrUpdate(List<SysSchedule> sysScheduleList) {
        return sysScheduleJpaRepository.saveAll(sysScheduleList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return sysScheduleJpaRepository.updateEnableFlagById(id, enableFlag);
    }

    public List<SysSchedule> getSysScheduleByStatus(ScheduleState scheduleState) {
        return sysScheduleJpaRepository.getSysSchedulesByStatus(scheduleState);
    }

}
