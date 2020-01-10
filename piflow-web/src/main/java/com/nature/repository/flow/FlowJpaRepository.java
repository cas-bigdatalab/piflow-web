package com.nature.repository.flow;

import com.nature.component.flow.model.Flow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface FlowJpaRepository extends JpaRepository<Flow, String>, JpaSpecificationExecutor<Flow>, Serializable {
    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from Flow c where c.enableFlag=1 and c.isExample<>1 and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<Flow> getFlowListPage(@Param("param") String param, Pageable pageable);

    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from Flow c where c.enableFlag=1 and c.isExample<>1 and c.crtUser=:userName and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<Flow> getFlowListPage(@Param("userName") String userName, @Param("param") String param, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Flow c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

    @Query(value = "select * from flow s where s.enable_flag = 1 and s.fk_flow_group_id = :fid and s.page_id = :pageId", nativeQuery = true)
    Flow getFlowByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Query(value = "select s.id from flow s where s.enable_flag = 1 and s.fk_flow_group_id = :fid and s.name = :flowName", nativeQuery = true)
    String getFlowIdByNameAndFlowGroupId(@Param("fid") String fid, @Param("flowName") String flowName);

    @Query(value = "select MAX(s.page_id) from flow s where s.enable_flag = 1 and s.fk_flow_group_id = :flowGroupId", nativeQuery = true)
    String getMaxStopPageIdByFlowGroupId(@Param("flowGroupId") String flowGroupId);

}
