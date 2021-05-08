package cn.cnic.component.process.jpa.domain;

import cn.cnic.component.process.entity.ProcessStopCustomizedProperty;
import cn.cnic.component.process.jpa.repository.ProcessStopCustomizedPropertyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class ProcessStopCustomizedPropertyDomain {

    @Autowired
    private ProcessStopCustomizedPropertyJpaRepository processStopCustomizedPropertyJpaRepository;

    private Specification<ProcessStopCustomizedProperty> addEnableFlagParam() {
        Specification<ProcessStopCustomizedProperty> specification = new Specification<ProcessStopCustomizedProperty>() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Predicate toPredicate(Root<ProcessStopCustomizedProperty> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    public ProcessStopCustomizedProperty getProcessStopCustomizedPropertyById(String id) {
        ProcessStopCustomizedProperty processStopCustomizedProperty = processStopCustomizedPropertyJpaRepository.getOne(id);
        if (null != processStopCustomizedProperty && !processStopCustomizedProperty.getEnableFlag()) {
            processStopCustomizedProperty = null;
        }
        return processStopCustomizedProperty;
    }

    public List<ProcessStopCustomizedProperty> getProcessStopCustomizedPropertyList() {
        return processStopCustomizedPropertyJpaRepository.findAll(addEnableFlagParam());
    }

    public ProcessStopCustomizedProperty saveOrUpdate(ProcessStopCustomizedProperty processStopCustomizedProperty) {
        return processStopCustomizedPropertyJpaRepository.save(processStopCustomizedProperty);
    }

    public List<ProcessStopCustomizedProperty> saveOrUpdate(List<ProcessStopCustomizedProperty> processStopCustomizedPropertyList) {
        return processStopCustomizedPropertyJpaRepository.saveAll(processStopCustomizedPropertyList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return processStopCustomizedPropertyJpaRepository.updateEnableFlagById(id, enableFlag);
    }

}
