package cn.cnic.component.flow.jpa.repository;

import cn.cnic.component.flow.entity.Flow;
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
    @Query("select c from Flow c where c.enableFlag=true and c.isExample<>true and c.flowGroup is null and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<Flow> getFlowListPage(@Param("param") String param, Pageable pageable);

    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from Flow c where c.enableFlag=true and c.isExample<>true and c.flowGroup is null and c.crtUser=:userName and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<Flow> getFlowListPage(@Param("userName") String userName, @Param("param") String param, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Flow c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

    @Query(nativeQuery = true, value = "select * from flow s where s.enable_flag=1 and s.fk_flow_group_id=:fid and s.page_id=:pageId")
    Flow getFlowByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Query(nativeQuery = true, value = "select s.id from flow s where s.enable_flag=1 and s.fk_flow_group_id=:fid and s.page_id=:pageId")
    String getFlowIdByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Query(nativeQuery = true, value = "select s.id from flow s where s.enable_flag=1 and s.fk_flow_group_id=:fid and s.name=:flowName")
    String getFlowIdByNameAndFlowGroupId(@Param("fid") String fid, @Param("flowName") String flowName);

    @Query(nativeQuery = true, value = "select MAX(s.page_id+0) from flow s where s.enable_flag=1 and s.fk_flow_group_id=:flowGroupId")
    Integer getMaxFlowPageIdByFlowGroupId(@Param("flowGroupId") String flowGroupId);

    @Query(nativeQuery = true, value = "select f.name from flow f WHERE f.enable_flag=1 and f.fk_flow_group_id=:flowGroupId and f.name=:flowName")
    String[] getFlowNameByFlowGroupId(@Param("flowGroupId") String flowGroupId, @Param("flowName") String flowName);

    @Query(nativeQuery = true, value = "select name from ( " +
            "select f.name from flow f WHERE f.enable_flag=1 and f.fk_flow_group_id=:flowGroupId " +
            "UNION ALL " +
            "select fg.name from flow_group fg where fg.enable_flag and fg.fk_flow_group_id=:flowGroupId " +
            ") as re ")
    String[] getFlowAndGroupNamesByFlowGroupId(@Param("flowGroupId") String flowGroupId);

}
