package com.nature.repository.mxGraph;


import com.nature.component.mxGraph.model.MxGeometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

public interface MxGeometryJpaRepository extends JpaRepository<MxGeometry, String>, JpaSpecificationExecutor<MxGeometry>, Serializable {
    @Modifying
    @Query("update MxGeometry c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);
}
