package com.nature.repository.mxGraph;


import com.nature.component.mxGraph.model.MxCell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface MxCellJpaRepository extends JpaRepository<MxCell, String>, JpaSpecificationExecutor<MxCell>, Serializable {

    @Transactional
    @Modifying
    @Query("update MxCell c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);
}
