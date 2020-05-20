package com.nature.repository.dataSource;

import com.nature.component.dataSource.model.DataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface DataSourceJpaRepository extends JpaRepository<DataSource, String>, JpaSpecificationExecutor<DataSource>, Serializable {

    @Transactional
    @Modifying
    @Query("update DataSource c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

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
