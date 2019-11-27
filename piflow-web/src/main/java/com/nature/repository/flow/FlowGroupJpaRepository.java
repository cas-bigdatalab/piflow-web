package com.nature.repository.flow;

import com.nature.component.flow.model.FlowGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

public interface FlowGroupJpaRepository extends JpaRepository<FlowGroup, String>, JpaSpecificationExecutor<FlowGroup>, Serializable {
    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from FlowGroup c where c.enableFlag=1 and c.isExample<>1 and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<FlowGroup> getFlowGroupListPage(@Param("param") String param, Pageable pageable);

    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from FlowGroup c where c.enableFlag=1 and c.isExample<>1 and c.crtUser=:userName and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<FlowGroup> getFlowGroupListPage(@Param("userName") String userName, @Param("param") String param, Pageable pageable);

    @Modifying
    @Query("update FlowGroup c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);
}
