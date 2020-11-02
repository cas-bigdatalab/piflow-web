package cn.cnic.component.flow.jpa.domain;

import cn.cnic.component.flow.entity.Property;
import cn.cnic.component.flow.jpa.repository.PropertyJpaRepository;
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
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
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

    public List<Property> getPropertyListByStopsId(String stopId) {
        return propertyJpaRepository.getPropertyListByStopsId(stopId);
    }

    public int deletePropertiesByIsOldDataAndStopsId(String stopId) {
        return propertyJpaRepository.deletePropertiesByIsOldDataAndStopsId(stopId);
    }

}
