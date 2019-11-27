package com.nature.domain.process;

import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.process.model.ProcessStop;
import com.nature.repository.process.ProcessStopJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class ProcessStopDomain {

    @Autowired
    private ProcessStopJpaRepository processStopJpaRepository;

    private Specification<ProcessStop> addEnableFlagParam() {
        Specification<ProcessStop> specification = new Specification<ProcessStop>() {
            @Override
            public Predicate toPredicate(Root<ProcessStop> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //Root.get("enableFlag") means to get the name of the enableFlag field.
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    private Specification<ProcessStop> addParam(String key, String value) {
        Specification<ProcessStop> specification = new Specification<ProcessStop>() {
            @Override
            public Predicate toPredicate(Root<ProcessStop> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //Root.get(key) means to get the name of the key field.
                return criteriaBuilder.equal(root.get(key), value);
            }
        };
        return specification;
    }

    public ProcessStop getProcessStopById(String id) {
        ProcessStop processStop = processStopJpaRepository.getOne(id);
        if (null == processStop || !processStop.getEnableFlag()) {
            processStop = null;
        }
        return processStop;
    }

    public Page<ProcessStop> getProcessStopListPage(int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        boolean isAdmin = SessionUserUtil.isAdmin();
        if (isAdmin) {
            return processStopJpaRepository.getProcessStopListPage(null == param ? "" : param, pageRequest);
        } else {
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            return processStopJpaRepository.getProcessStopListPage(currentUser.getUsername(), null == param ? "" : param, pageRequest);
        }
    }

    public ProcessStop saveOrUpdate(ProcessStop processStop) {
        return processStopJpaRepository.save(processStop);
    }

    public List<ProcessStop> saveOrUpdate(List<ProcessStop> processStopList) {
        return processStopJpaRepository.saveAll(processStopList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return processStopJpaRepository.updateEnableFlagById(id, enableFlag);
    }



}
