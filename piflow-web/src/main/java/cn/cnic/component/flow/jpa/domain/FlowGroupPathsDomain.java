package cn.cnic.component.flow.jpa.domain;

import cn.cnic.component.flow.entity.FlowGroupPaths;
import cn.cnic.component.flow.jpa.repository.FlowGroupPathsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class FlowGroupPathsDomain {

    @Autowired
    private FlowGroupPathsJpaRepository flowGroupPathsJpaRepository;

    private Specification<FlowGroupPaths> addEnableFlagParam() {
        Specification<FlowGroupPaths> specification = new Specification<FlowGroupPaths>() {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public Predicate toPredicate(Root<FlowGroupPaths> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    public FlowGroupPaths getPathsById(String id) {
        FlowGroupPaths flowGroupPaths = flowGroupPathsJpaRepository.getOne(id);
        if (null != flowGroupPaths && !flowGroupPaths.getEnableFlag()) {
            flowGroupPaths = null;
        }
        return flowGroupPaths;
    }

    public List<FlowGroupPaths> getFlowGroupPathsList() {
        return flowGroupPathsJpaRepository.findAll(addEnableFlagParam());
    }

    public FlowGroupPaths saveOrUpdate(FlowGroupPaths flowGroupPaths) {
        return flowGroupPathsJpaRepository.save(flowGroupPaths);
    }

    public List<FlowGroupPaths> saveOrUpdate(List<FlowGroupPaths> pathsList) {
        return flowGroupPathsJpaRepository.saveAll(pathsList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return flowGroupPathsJpaRepository.updateEnableFlagById(id, enableFlag);
    }

}
