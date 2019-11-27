package com.nature.domain.flow;

import com.nature.component.flow.model.Property;
import com.nature.repository.flow.PropertyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class PropertyDomain {

    @Autowired
    private PropertyJpaRepository propertyJpaRepository;

    private Specification<Property> addEnableFlagParam() {
        Specification<Property> specification = new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    private Specification<Property> addParam(String key, String value) {
        Specification<Property> specification = new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get(key) means to get the name of the key field
                return criteriaBuilder.equal(root.get(key), value);
            }
        };
        return specification;
    }

    public Property getPropertyById(String id) {
        Property property = propertyJpaRepository.getOne(id);
        if (null != property && !property.getEnableFlag()) {
            property = null;
        }
        return property;
    }

    public List<Property> getPropertyList() {
        return propertyJpaRepository.findAll(addEnableFlagParam());
    }

    public Property saveOrUpdate(Property property) {
        return propertyJpaRepository.save(property);
    }

    public List<Property> saveOrUpdate(List<Property> propertyList) {
        return propertyJpaRepository.saveAll(propertyList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return propertyJpaRepository.updateEnableFlagById(id, enableFlag);
    }

    public List<Property> getPropertyListByStopsId(String stopId) {
        return propertyJpaRepository.getPropertyListByStopsId(stopId);
    }

}
