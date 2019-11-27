package com.nature.domain.flow;

import com.nature.component.flow.model.Stops;
import com.nature.repository.flow.StopsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class StopsDomain {

    @Autowired
    private StopsJpaRepository stopsJpaRepository;

    private Specification<Stops> addEnableFlagParam() {
        Specification<Stops> specification = new Specification<Stops>() {
            @Override
            public Predicate toPredicate(Root<Stops> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    private Specification<Stops> addParam(String key, String value) {
        Specification<Stops> specification = new Specification<Stops>() {
            @Override
            public Predicate toPredicate(Root<Stops> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get(key) means to get the name of the key field
                return criteriaBuilder.equal(root.get(key), value);
            }
        };
        return specification;
    }

    public Stops getStopsById(String id) {
        Stops stops = stopsJpaRepository.getOne(id);
        if (null != stops && !stops.getEnableFlag()) {
            stops = null;
        }
        return stops;
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

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return stopsJpaRepository.updateEnableFlagById(id, enableFlag);
    }

}
