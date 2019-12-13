package com.nature.domain.dataSource;

import com.nature.component.dataSource.model.DataSource;
import com.nature.component.dataSource.model.DataSourceProperty;
import com.nature.repository.dataSource.DataSourcePropertyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class DataSourcePropertyDomain {

    @Autowired
    private DataSourcePropertyJpaRepository dataSourcePropertyJpaRepository;

    private Specification<DataSourceProperty> addEnableFlagParam() {
        Specification<DataSourceProperty> specification = new Specification<DataSourceProperty>() {
            @Override
            public Predicate toPredicate(Root<DataSourceProperty> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    private Specification<DataSourceProperty> addParam(String key, String value) {
        Specification<DataSourceProperty> specification = new Specification<DataSourceProperty>() {
            @Override
            public Predicate toPredicate(Root<DataSourceProperty> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get(key) means to get the name of the key field
                return criteriaBuilder.equal(root.get(key), value);
            }
        };
        return specification;
    }

    public DataSourceProperty getDataSourcePropertyById(String id) {
        DataSourceProperty dataSourceProperty = dataSourcePropertyJpaRepository.getOne(id);
        if (null != dataSourceProperty && !dataSourceProperty.getEnableFlag()) {
            dataSourceProperty = null;
        }
        return dataSourceProperty;
    }

    public List<DataSourceProperty> getDataSourcePropertyList() {
        return dataSourcePropertyJpaRepository.findAll(addEnableFlagParam());
    }

    public DataSourceProperty saveOrUpdate(DataSourceProperty dataSourceProperty) {
        return dataSourcePropertyJpaRepository.save(dataSourceProperty);
    }

    public List<DataSourceProperty> saveOrUpdate(List<DataSourceProperty> dataSourcePropertyList) {
        return dataSourcePropertyJpaRepository.saveAll(dataSourcePropertyList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return dataSourcePropertyJpaRepository.updateEnableFlagById(id, enableFlag);
    }

    public int updateEnableFlagByDatasourceId(DataSource dataSource, boolean enableFlag) {
        return dataSourcePropertyJpaRepository.updateEnableFlagByDatasourceId(dataSource, enableFlag);
    }

    public void delete(DataSourceProperty dataSourceProperty) {
        dataSourcePropertyJpaRepository.delete(dataSourceProperty);
    }
    public void delete(List<DataSourceProperty> dataSourcePropertyList) {
        dataSourcePropertyJpaRepository.deleteAll(dataSourcePropertyList);
    }
}
