package com.nature.domain.dataSource;

import com.nature.component.dataSource.model.DataSource;
import com.nature.repository.dataSource.DataSourceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class DataSourceDomain {

    @Autowired
    private DataSourceJpaRepository dataSourceJpaRepository;

    private Specification<DataSource> addEnableFlagParam() {
        Specification<DataSource> specification = new Specification<DataSource>() {
 
			private static final long serialVersionUID = 1L;

			@Override
            public Predicate toPredicate(Root<DataSource> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    public DataSource getDataSourceById(String id) {
        DataSource dataSource = dataSourceJpaRepository.getOne(id);
        if (null != dataSource && !dataSource.getEnableFlag()) {
            dataSource = null;
        }
        return dataSource;
    }

    public List<DataSource> getDataSourceList() {
        return dataSourceJpaRepository.findAll(addEnableFlagParam());
    }

    public DataSource saveOrUpdate(DataSource dataSource) {
        return dataSourceJpaRepository.save(dataSource);
    }

    public List<DataSource> saveOrUpdate(List<DataSource> dataSourceList) {
        return dataSourceJpaRepository.saveAll(dataSourceList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return dataSourceJpaRepository.updateEnableFlagById(id, enableFlag);
    }

}
