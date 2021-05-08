package cn.cnic.component.dataSource.jpa.domain;

import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.entity.DataSourceProperty;
import cn.cnic.component.dataSource.jpa.repository.DataSourcePropertyJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class DataSourcePropertyDomain {

    @Resource
    private DataSourcePropertyJpaRepository dataSourcePropertyJpaRepository;

    private Specification<DataSourceProperty> addEnableFlagParam() {
        Specification<DataSourceProperty> specification = new Specification<DataSourceProperty>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<DataSourceProperty> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
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
