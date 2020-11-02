package cn.cnic.component.process.jpa.domain;

import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.jpa.repository.ProcessGroupJpaRepository;
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
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class ProcessGroupDomain {

    @Autowired
    private ProcessGroupJpaRepository processGroupJpaRepository;

    private Specification<ProcessGroup> addEnableFlagParam() {
        Specification<ProcessGroup> specification = new Specification<ProcessGroup>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ProcessGroup> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    private Specification<ProcessGroup> addParam(String key, String value) {
        Specification<ProcessGroup> specification = new Specification<ProcessGroup>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ProcessGroup> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get(key) means to get the name of the key field
                return criteriaBuilder.equal(root.get(key), value);
            }
        };
        return specification;
    }


    public ProcessGroup getProcessGroupById(String id) {
        ProcessGroup processGroup = processGroupJpaRepository.getOne(id);
        if (null != processGroup && !processGroup.getEnableFlag()) {
            processGroup = null;
        }
        return processGroup;
    }

    public ProcessGroup getProcessGroupByGroupId(String groupId) {
        ProcessGroup processGroup = null;
        List<ProcessGroup> processGroupList = processGroupJpaRepository.findAll(Specification.where(addEnableFlagParam()).and(addParam("appId", groupId)));
        if (null != processGroupList && processGroupList.size() == 1) {
            processGroup = processGroupList.get(0);
        }
        return processGroup;
    }

    public Page<ProcessGroup> getProcessGroupListPage(String username, boolean isAdmin, int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        if (isAdmin) {
            return processGroupJpaRepository.getProcessGroupListPage(null == param ? "" : param, pageRequest);
        } else {
            return processGroupJpaRepository.getProcessGroupListPageByUser(username, null == param ? "" : param, pageRequest);
        }
    }

    public List<ProcessGroup> getProcessGroupList(String param) {
        return processGroupJpaRepository.findAll(addEnableFlagParam());
    }

    public ProcessGroup saveOrUpdate(String username, ProcessGroup processGroup) {
        processGroup.setLastUpdateDttm(new Date());
        processGroup.setLastUpdateUser(username);
        return processGroupJpaRepository.save(processGroup);
    }

    public ProcessGroup saveOrUpdateSyncTask(ProcessGroup processGroup) {
        processGroup.setLastUpdateUser("syncTask");
        return processGroupJpaRepository.save(processGroup);
    }

    @Transactional
    public ProcessGroup getProcessGroupByPageId(String fid, String pageId) {
        return processGroupJpaRepository.getProcessGroupByPageId(fid, pageId);
    }

    public String getProcessIdGroupByPageId(String fid, String pageId) {
        return processGroupJpaRepository.getProcessGroupIdByPageId(fid, pageId);
    }

    public ProcessGroup getProcessGroupByAppId(String appId) {
        return processGroupJpaRepository.getProcessGroupByAppId(appId);
    }

    public List<Map<String, Object>> getProcessGroupNamesAndPageIdsByPageIds(String fid, List<String> pageIds) {
        return processGroupJpaRepository.getProcessGroupNamesAndPageIdsByPageIds(fid, pageIds);
    }

    public List<String> getRunningProcessGroupAppId() {
        return processGroupJpaRepository.getRunningProcessGroupAppId();
    }

}
