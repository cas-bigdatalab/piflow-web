package cn.cnic.component.process.jpa.domain;

import cn.cnic.component.process.entity.ProcessGroupPath;
import cn.cnic.component.process.jpa.repository.ProcessGroupPathJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class ProcessGroupPathDomain {

    @Autowired
    private ProcessGroupPathJpaRepository processGroupPathJpaRepository;

    private Specification<ProcessGroupPath> addEnableFlagParam() {
        Specification<ProcessGroupPath> specification = new Specification<ProcessGroupPath>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ProcessGroupPath> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    public ProcessGroupPath getProcessGroupPathById(String id) {
        ProcessGroupPath processGroupPath = processGroupPathJpaRepository.getOne(id);
        if (null != processGroupPath && !processGroupPath.getEnableFlag()) {
            processGroupPath = null;
        }
        return processGroupPath;
    }

    public List<ProcessGroupPath> getProcessGroupPathList() {
        return processGroupPathJpaRepository.findAll(addEnableFlagParam());
    }

    public ProcessGroupPath saveOrUpdate(ProcessGroupPath processGroupPath) {
        return processGroupPathJpaRepository.save(processGroupPath);
    }

    public List<ProcessGroupPath> saveOrUpdate(List<ProcessGroupPath> processGroupPathList) {
        return processGroupPathJpaRepository.saveAll(processGroupPathList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return processGroupPathJpaRepository.updateEnableFlagById(id, enableFlag);
    }

    public ProcessGroupPath getProcessGroupPathByPageId(String fid, String pageId) {
        return processGroupPathJpaRepository.getProcessGroupPathByPageId(fid, pageId);
    }

}
