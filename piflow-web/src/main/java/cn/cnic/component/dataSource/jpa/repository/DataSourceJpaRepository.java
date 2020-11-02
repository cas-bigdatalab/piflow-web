package cn.cnic.component.dataSource.jpa.repository;

import cn.cnic.component.dataSource.entity.DataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface DataSourceJpaRepository extends JpaRepository<DataSource, String>, JpaSpecificationExecutor<DataSource>, Serializable {

    @Query(value = "select ds from DataSource ds where ds.enableFlag=true and ds.id=:id")
    DataSource getDataSourceById(@Param("id") String id);

    @Query(value = "select ds from DataSource ds where ds.enableFlag=true and ds.id=:id and ds.crtUser=:username")
    DataSource getDataSourceByIdAndCreateUser(@Param("id") String id, @Param("username") String username);

    @Query(value = "select ds from DataSource ds where ds.enableFlag=true and ds.crtUser=:username")
    List<DataSource> getDataSourceListByCreateUser(@Param("username") String username);

    @Query(value = "select ds from DataSource ds where ds.enableFlag=true and ds.isTemplate=true")
    List<DataSource> getDataSourceTemplateList();

    /**
     * Paging query
     *
     * @return
     */
    @Query("select ds from DataSource ds where ds.enableFlag=true and (ds.dataSourceName like CONCAT('%',:param,'%') or ds.dataSourceDescription like CONCAT('%',:param,'%')or ds.dataSourceType like CONCAT('%',:param,'%'))")
    Page<DataSource> getDataSourceListPageByParam(@Param("param") String param, Pageable pageable);

    /**
     * Paging query by user
     *
     * @return
     */
    @Query("select ds from DataSource ds where ds.enableFlag=true and ds.crtUser=:username and (ds.dataSourceName like CONCAT('%',:param,'%') or ds.dataSourceDescription like CONCAT('%',:param,'%')or ds.dataSourceType like CONCAT('%',:param,'%'))")
    Page<DataSource> getDataSourceListPageByParamAndCrtUser(@Param("username") String username, @Param("param") String param, Pageable pageable);

}
