package cn.cnic.component.template.jpa.domain;

import cn.cnic.component.template.entity.FlowTemplate;
import cn.cnic.component.template.jpa.repository.FlowTemplateJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class FlowTemplateDomain {

    @Resource
    private FlowTemplateJpaRepository flowTemplateJpaRepository;

    private Specification<FlowTemplate> addEnableFlagParam() {
        Specification<FlowTemplate> specification = new Specification<FlowTemplate>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<FlowTemplate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    public FlowTemplate getFlowTemplateById(String id) {
        FlowTemplate flowTemplate = flowTemplateJpaRepository.getOne(id);
        if (null != flowTemplate && !flowTemplate.getEnableFlag()) {
            flowTemplate = null;
        }
        return flowTemplate;
    }

    public List<FlowTemplate> getFlowTemplateList(String username, boolean isAdmin) {
        if (isAdmin) {
            return flowTemplateJpaRepository.findAll(addEnableFlagParam(), Sort.by(Sort.Order.desc("crtDttm")));
        } else {
            return flowTemplateJpaRepository.getFlowTemplateByCrtUser(username);
        }
    }

    public FlowTemplate saveOrUpdate(FlowTemplate flowTemplate) {
        return flowTemplateJpaRepository.save(flowTemplate);
    }

    public List<FlowTemplate> saveOrUpdate(List<FlowTemplate> pathsList) {
        return flowTemplateJpaRepository.saveAll(pathsList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return flowTemplateJpaRepository.updateEnableFlagById(id, enableFlag);
    }

    public Page<FlowTemplate> getFlowTemplateListPage(String username, boolean isAdmin, int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        if (isAdmin) {
            return flowTemplateJpaRepository.getFlowTemplateListPageByParam(null == param ? "" : param, pageRequest);
        } else {
            return flowTemplateJpaRepository.getFlowTemplateListPageByParamAndCrtUser(username, null == param ? "" : param, pageRequest);
        }
    }

}
