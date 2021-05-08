package cn.cnic.component.process.jpa.domain;

import cn.cnic.component.process.entity.ProcessPath;
import cn.cnic.component.process.jpa.repository.ProcessPathJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class ProcessPathDomain {

    @Autowired
    private ProcessPathJpaRepository processPathJpaRepository;

    private Specification<ProcessPath> addEnableFlagParam() {
        Specification<ProcessPath> specification = new Specification<ProcessPath>() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Predicate toPredicate(Root<ProcessPath> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    public ProcessPath getProcessPathById(String id) {
        ProcessPath processPath = processPathJpaRepository.getOne(id);
        if (null != processPath && !processPath.getEnableFlag()) {
            processPath = null;
        }
        return processPath;
    }

    public List<ProcessPath> getProcessPathList() {
        return processPathJpaRepository.findAll(addEnableFlagParam());
    }

    public ProcessPath saveOrUpdate(ProcessPath processPath) {
        return processPathJpaRepository.save(processPath);
    }

    public List<ProcessPath> saveOrUpdate(List<ProcessPath> processPathList) {
        return processPathJpaRepository.saveAll(processPathList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return processPathJpaRepository.updateEnableFlagById(id, enableFlag);
    }

}
