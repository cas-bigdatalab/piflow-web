package cn.cnic.component.flow.jpa.domain;

import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.jpa.repository.StopsJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class StopsDomain {

    @Resource
    private StopsJpaRepository stopsJpaRepository;

    private Specification<Stops> addEnableFlagParam() {
        Specification<Stops> specification = new Specification<Stops>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Stops> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    public Stops getStopsById(String id) {
        return stopsJpaRepository.getStopsById(id);
    }

    public List<Stops> getStopsList() {
        return stopsJpaRepository.findAll(addEnableFlagParam());
    }

    public Stops saveOrUpdate(Stops stops) {
        return stopsJpaRepository.save(stops);
    }

    public List<Stops> saveOrUpdate(List<Stops> stopsList) {
        return stopsJpaRepository.saveAll(stopsList);
    }

    public Integer getMaxStopPageIdByFlowId(String flowId) {
        return stopsJpaRepository.getMaxStopPageIdByFlowId(flowId);
    }

    public String[] getStopNamesByFlowId(String flowId) {
        return stopsJpaRepository.getStopNamesByFlowId(flowId);
    }

    public Stops getStopsByPageId(String fid, String stopPageId) {
        return stopsJpaRepository.getStopsByPageId(fid, stopPageId);
    }

    public List<String> getStopsIdList() {
        return stopsJpaRepository.getStopsIdList();
    }

}
