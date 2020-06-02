package cn.cnic.domain.template;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.base.vo.UserVo;
import cn.cnic.component.template.model.FlowTemplate;
import cn.cnic.repository.template.FlowTemplateJpaRepository;
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

    public List<FlowTemplate> getFlowTemplateList() {
        boolean isAdmin = SessionUserUtil.isAdmin();
        if (isAdmin) {
            return flowTemplateJpaRepository.findAll(addEnableFlagParam(), Sort.by(Sort.Order.desc("crtDttm")));
        } else {
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            return flowTemplateJpaRepository.getFlowTemplateByCrtUser(currentUser.getUsername());
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

    public Page<FlowTemplate> getFlowTemplateListPage(int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        boolean isAdmin = SessionUserUtil.isAdmin();
        if (isAdmin) {
            return flowTemplateJpaRepository.getFlowTemplateListPageByParam(null == param ? "" : param, pageRequest);
        } else {
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            return flowTemplateJpaRepository.getFlowTemplateListPageByParamAndCrtUser(currentUser.getUsername(), null == param ? "" : param, pageRequest);
        }
    }

}
