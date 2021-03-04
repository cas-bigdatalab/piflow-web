package cn.cnic.component.flow.jpa.domain;

import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.jpa.repository.FlowGroupJpaRepository;
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
public class FlowGroupDomain {

    @Autowired
    private FlowGroupJpaRepository flowGroupJpaRepository;

    private Specification<FlowGroup> addEnableFlagParam() {
        Specification<FlowGroup> specification = new Specification<FlowGroup>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<FlowGroup> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    private Specification<FlowGroup> queryParam(String param) {
        if (null == param) {
            param = "";
        }
        final String search = param;
        Specification<FlowGroup> specification = new Specification<FlowGroup>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<FlowGroup> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("country")表示获取country这个字段名称,like表示执行like查询,%param%表示值
                Predicate p1 = criteriaBuilder.like(root.get("name"), "%" + search + "%");
                Predicate p2 = criteriaBuilder.like(root.get("description"), "%" + search + "%");
                Predicate p3 = criteriaBuilder.like(root.get("city"), "%" + search + "%");
                //Return the Predicate object after combining the two query conditions
                return criteriaBuilder.or(p1, p2, p3);
            }
        };
        return specification;
    }

    public FlowGroup getFlowGroupById(String id) {
        return flowGroupJpaRepository.getFlowGroupByIdAndEnAndEnableFlag(id, true);
    }

     public String[] getFlowGroupNameByNameInGroup(String fId, String name) {

        return flowGroupJpaRepository.getFlowGroupNamesByNameAndEnableFlagInGroup(fId, name, true);

    }

    public Page<FlowGroup> userGetFlowGroupListPage(int page, int size, String param,String username) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        return flowGroupJpaRepository.getFlowGroupListPageByCrtUser(username, null == param ? "" : param, pageRequest);
    }

    public Page<FlowGroup> adminGetFlowGroupListPage(int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        return flowGroupJpaRepository.getFlowGroupListPage(null == param ? "" : param, pageRequest);
    }

    public List<FlowGroup> getFlowGroupList(String param) {
        return flowGroupJpaRepository.findAll(Specification.where(addEnableFlagParam()).and(queryParam(param)));
    }

    public FlowGroup saveOrUpdate(FlowGroup flowGroup) {
        return flowGroupJpaRepository.save(flowGroup);
    }

    public List<FlowGroup> saveOrUpdate(List<FlowGroup> flowGroupList) {
        return flowGroupJpaRepository.saveAll(flowGroupList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return flowGroupJpaRepository.updateEnableFlagById(id, enableFlag);
    }

    public FlowGroup getFlowGroupByPageId(String fid, String pageId) {
        return flowGroupJpaRepository.getFlowGroupByPageId(fid, pageId);
    }

    public String getFlowGroupIdByPageId(String fid, String pageId) {
        return flowGroupJpaRepository.getFlowGroupIdByPageId(fid, pageId);
    }

    public String getFlowIdByNameAndFlowGroupId(String fid, String flowGroupName) {
        return flowGroupJpaRepository.getFlowGroupIdByNameAndFid(fid, flowGroupName);
    }
}
