package com.nature.repository.process;

import com.nature.component.process.model.ProcessGroupPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface ProcessGroupPathJpaRepository extends JpaRepository<ProcessGroupPath, String>, JpaSpecificationExecutor<ProcessGroupPath>, Serializable {

    @Transactional
    @Modifying
    @Query("update ProcessGroupPath c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

}
