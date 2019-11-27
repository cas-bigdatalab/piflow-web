package com.nature.domain.flow;

import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.repository.flow.FlowJpaRepository;
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
public class FlowDomain {

    @Autowired
    private FlowJpaRepository flowJpaRepository;

    private Specification<Flow> addEnableFlagParam() {
        Specification<Flow> specification = new Specification<Flow>() {
            @Override
            public Predicate toPredicate(Root<Flow> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //Root.get("enableFlag") means to get the name of the enableFlag field.
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    private Specification<Flow> addParam(String key, String value) {
        Specification<Flow> specification = new Specification<Flow>() {
            @Override
            public Predicate toPredicate(Root<Flow> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //Root.get(key) means to get the name of the key field.
                return criteriaBuilder.equal(root.get(key), value);
            }
        };
        return specification;
    }

    public Flow getFlowById(String id) {
        Flow flow = flowJpaRepository.getOne(id);
        if (null != flow && !flow.getEnableFlag()) {
            flow = null;
        }
        return flow;
    }

    public Page<Flow> getFlowListPage(int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        boolean isAdmin = SessionUserUtil.isAdmin();
        if (isAdmin) {
            return flowJpaRepository.getFlowListPage(null == param ? "" : param, pageRequest);
        } else {
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            return flowJpaRepository.getFlowListPage(currentUser.getUsername(), null == param ? "" : param, pageRequest);
        }
    }

    public Flow saveOrUpdate(Flow flow) {
        return flowJpaRepository.save(flow);
    }

    public List<Flow> saveOrUpdate(List<Flow> flowList) {
        return flowJpaRepository.saveAll(flowList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return flowJpaRepository.updateEnableFlagById(id, enableFlag);
    }

    public Flow getFlowByPageId(String fid, String pageId) {
        return flowJpaRepository.getFlowByPageId(fid, pageId);
    }

    public String getFlowIdByNameAndFlowGroupId(String fid, String flowName) {
        return flowJpaRepository.getFlowIdByNameAndFlowGroupId(fid, flowName);
    }

    public String getMaxStopPageIdByFlowGroupId(String flowGroupId) {
        return flowJpaRepository.getMaxStopPageIdByFlowGroupId(flowGroupId);
    }


}
