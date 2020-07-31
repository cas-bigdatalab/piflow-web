package cn.cnic.component.dataSource.jpa.domain;

import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.jpa.repository.DataSourceJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class DataSourceDomain {

    @Resource
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
        return dataSourceJpaRepository.getDataSourceById(id);
    }

    public DataSource getDataSourceByIdAndCreateUser(String id,String username) {
        return dataSourceJpaRepository.getDataSourceByIdAndCreateUser(id,username);
    }

    public List<DataSource> getDataSourceList() {
        return dataSourceJpaRepository.findAll(addEnableFlagParam());
    }

    public List<DataSource> getDataSourceListByCreateUser(String username) {
        return dataSourceJpaRepository.getDataSourceListByCreateUser(username);
    }

    public DataSource saveOrUpdate(DataSource dataSource) {
        return dataSourceJpaRepository.save(dataSource);
    }

    public List<DataSource> saveOrUpdate(List<DataSource> dataSourceList) {
        return dataSourceJpaRepository.saveAll(dataSourceList);
    }

    public List<DataSource> getDataSourceTemplateList() {
        return dataSourceJpaRepository.getDataSourceTemplateList();
    }

    public Page<DataSource> userGetDataSourceListPage(String username, int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        return dataSourceJpaRepository.getDataSourceListPageByParamAndCrtUser(username, null == param ? "" : param, pageRequest);
    }

    public Page<DataSource> adminGetDataSourceListPage(int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        return dataSourceJpaRepository.getDataSourceListPageByParam(null == param ? "" : param, pageRequest);
    }


}
