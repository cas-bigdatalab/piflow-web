package com.nature.repository.mxGraph;


import com.nature.component.mxGraph.model.MxGraphModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

public interface MxGraphModelJpaRepository extends JpaRepository<MxGraphModel, String>, JpaSpecificationExecutor<MxGraphModel>, Serializable {
    @Modifying
    @Query("update MxGraphModel c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

    @Query(value = "select * from mx_graph_model s where s.enable_flag = 1 and s.fk_flow_group_id = :flowGroupId", nativeQuery = true)
    MxGraphModel getMxGraphModelByFlowGroupId(@Param("flowGroupId") String flowGroupId);
}
