package com.nature.repository.dataSource;

import com.nature.component.dataSource.model.DataSource;
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

}
