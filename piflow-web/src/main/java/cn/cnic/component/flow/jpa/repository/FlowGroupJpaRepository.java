package cn.cnic.component.flow.jpa.repository;

import cn.cnic.component.flow.entity.FlowGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface FlowGroupJpaRepository extends JpaRepository<FlowGroup, String>, JpaSpecificationExecutor<FlowGroup>, Serializable {

    @Query("select c from  FlowGroup c where c.enableFlag=:enableFlag and c.id=:id")
    FlowGroup getFlowGroupByIdAndEnAndEnableFlag(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

    @Query(value = "select name from  flow_group c where c.enable_flag=:enableFlag and  c.name=:name and c.fk_flow_group_id=:flowGroupId", nativeQuery = true)
    String[] getFlowGroupNamesByNameAndEnableFlagInGroup(@Param("flowGroupId") String flowGroupId, @Param("name") String name,@Param("enableFlag") boolean enableFlag);

    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from FlowGroup c where c.enableFlag=true and c.isExample<>true and c.flowGroup is null and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<FlowGroup> getFlowGroupListPage(@Param("param") String param, Pageable pageable);

    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from FlowGroup c where c.enableFlag=true and c.isExample<>true and c.flowGroup is null and c.crtUser=:userName and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<FlowGroup> getFlowGroupListPageByCrtUser(@Param("userName") String userName, @Param("param") String param, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update FlowGroup c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

    @Query(nativeQuery = true, value = "select * from flow_group s where s.enable_flag = 1 and s.fk_flow_group_id = :fid and s.page_id = :pageId")
    FlowGroup getFlowGroupByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Query(nativeQuery = true, value = "select s.id from flow_group s where s.enable_flag = 1 and s.fk_flow_group_id = :fid and s.page_id = :pageId")
    String getFlowGroupIdByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Query(nativeQuery = true, value = "select s.id from flow_group s where s.enable_flag = 1 and s.fk_flow_group_id = :fid and s.name = :flowGroupName")
    String getFlowGroupIdByNameAndFid(@Param("fid") String fid, @Param("flowGroupName") String flowGroupName);
}
