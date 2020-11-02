package cn.cnic.component.dataSource.jpa.repository;

import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.entity.DataSourceProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface DataSourcePropertyJpaRepository extends JpaRepository<DataSourceProperty, String>, JpaSpecificationExecutor<DataSourceProperty>, Serializable {

    @Transactional
    @Modifying
    @Query("update DataSourceProperty c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

    @Transactional
    @Modifying
    @Query("update DataSourceProperty dsp set dsp.dataSource = null,dsp.enableFlag = :enableFlag where dsp.dataSource = :dataSource")
    int updateEnableFlagByDatasourceId(@Param("dataSource") DataSource dataSource, @Param("enableFlag") boolean enableFlag);


}
