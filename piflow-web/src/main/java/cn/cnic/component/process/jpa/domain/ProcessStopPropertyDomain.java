package cn.cnic.component.process.jpa.domain;

import cn.cnic.component.process.entity.ProcessStopProperty;
import cn.cnic.component.process.jpa.repository.ProcessStopPropertyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class ProcessStopPropertyDomain {

    @Autowired
    private ProcessStopPropertyJpaRepository processStopPropertyJpaRepository;

    private Specification<ProcessStopProperty> addEnableFlagParam() {
        Specification<ProcessStopProperty> specification = new Specification<ProcessStopProperty>() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Predicate toPredicate(Root<ProcessStopProperty> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    public ProcessStopProperty getProcessStopPropertyById(String id) {
        ProcessStopProperty processStopProperty = processStopPropertyJpaRepository.getOne(id);
        if (null != processStopProperty && !processStopProperty.getEnableFlag()) {
            processStopProperty = null;
        }
        return processStopProperty;
    }

    public List<ProcessStopProperty> getProcessStopPropertyList() {
        return processStopPropertyJpaRepository.findAll(addEnableFlagParam());
    }

    public ProcessStopProperty saveOrUpdate(ProcessStopProperty processStopProperty) {
        return processStopPropertyJpaRepository.save(processStopProperty);
    }

    public List<ProcessStopProperty> saveOrUpdate(List<ProcessStopProperty> processStopPropertyList) {
        return processStopPropertyJpaRepository.saveAll(processStopPropertyList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return processStopPropertyJpaRepository.updateEnableFlagById(id, enableFlag);
    }

}
