package cn.cnic.domain.template;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.base.vo.UserVo;
import cn.cnic.component.template.model.FlowGroupTemplate;
import cn.cnic.repository.template.FlowGroupTemplateJpaRepository;
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
public class FlowGroupTemplateDomain {

    @Resource
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
        boolean isAdmin = SessionUserUtil.isAdmin();
        if (isAdmin) {
            return flowGroupTemplateJpaRepository.findAll(addEnableFlagParam());
        } else {
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            return flowGroupTemplateJpaRepository.getFlowGroupTemplateByCrtUser(currentUser.getUsername());
        }
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

    public Page<FlowGroupTemplate> getFlowGroupTemplateListPage(int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        boolean isAdmin = SessionUserUtil.isAdmin();
        if (isAdmin) {
            return flowGroupTemplateJpaRepository.getFlowGroupTemplateListPageByParam(null == param ? "" : param, pageRequest);
        } else {
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            return flowGroupTemplateJpaRepository.getFlowGroupTemplateListPageByParamAndCrtUser(currentUser.getUsername(), null == param ? "" : param, pageRequest);
        }
    }

}
