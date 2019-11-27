package com.nature.repository.flow;

import com.nature.component.flow.model.FlowGroupPaths;
import com.nature.component.flow.model.Paths;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

public interface FlowGroupPathsJpaRepository extends JpaRepository<FlowGroupPaths, String>, JpaSpecificationExecutor<FlowGroupPaths>, Serializable {

    @Modifying
    @Query("update FlowGroupPaths c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

}
