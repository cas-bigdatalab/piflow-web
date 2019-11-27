package com.nature.repository.DataSource;

import com.nature.component.dataSource.model.DataSource;
import com.nature.component.dataSource.model.DataSourceProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

public interface DataSourcePropertyJpaRepository extends JpaRepository<DataSourceProperty, String>, JpaSpecificationExecutor<DataSourceProperty>, Serializable {

    @Modifying
    @Query("update DataSourceProperty c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

    @Modifying
    @Query("update DataSourceProperty dsp set dsp.dataSource = null,dsp.enableFlag = :enableFlag where dsp.dataSource = :dataSource")
    int updateEnableFlagByDatasourceId(@Param("dataSource") DataSource dataSource, @Param("enableFlag") boolean enableFlag);


}
