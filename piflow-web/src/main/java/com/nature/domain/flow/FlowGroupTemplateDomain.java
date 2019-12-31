package com.nature.domain.flow;

import com.nature.component.template.model.FlowGroupTemplate;
import com.nature.repository.flow.FlowGroupTemplateJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class FlowGroupTemplateDomain {

    @Autowired
    private FlowGroupTemplateJpaRepository flowGroupTemplateJpaRepository;

    private Specification<FlowGroupTemplate> addEnableFlagParam() {
        Specification<FlowGroupTemplate> specification = new Specification<FlowGroupTemplate>() {
        	
        	private static final long serialVersionUID = 1L;
        	
            @Override
            public Predicate toPredicate(Root<FlowGroupTemplate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    public FlowGroupTemplate getFlowGroupTemplateById(String id) {
        FlowGroupTemplate flowGroupTemplate = flowGroupTemplateJpaRepository.getOne(id);
        if (null != flowGroupTemplate && !flowGroupTemplate.getEnableFlag()) {
            flowGroupTemplate = null;
        }
        return flowGroupTemplate;
    }

    public List<FlowGroupTemplate> getFlowGroupTemplateList() {
        return flowGroupTemplateJpaRepository.findAll(addEnableFlagParam());
    }

    public FlowGroupTemplate saveOrUpdate(FlowGroupTemplate flowGroupTemplate) {
        return flowGroupTemplateJpaRepository.save(flowGroupTemplate);
    }

    public List<FlowGroupTemplate> saveOrUpdate(List<FlowGroupTemplate> pathsList) {
        return flowGroupTemplateJpaRepository.saveAll(pathsList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return flowGroupTemplateJpaRepository.updateEnableFlagById(id, enableFlag);
    }

}
