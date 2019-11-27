package com.nature.repository.process;

import com.nature.component.process.model.ProcessPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

public interface ProcessPathJpaRepository extends JpaRepository<ProcessPath, String>, JpaSpecificationExecutor<ProcessPath>, Serializable {

    @Modifying
    @Query("update ProcessPath c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

}
