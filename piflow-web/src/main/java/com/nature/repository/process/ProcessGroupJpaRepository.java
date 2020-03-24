package com.nature.repository.process;

import com.nature.component.process.model.ProcessGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;

public interface ProcessGroupJpaRepository extends JpaRepository<ProcessGroup, String>, JpaSpecificationExecutor<ProcessGroup>, Serializable {
    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from ProcessGroup c where c.enableFlag=true and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<ProcessGroup> getProcessGroupListPage(@Param("param") String param, Pageable pageable);

    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from ProcessGroup c where c.enableFlag=true and c.crtUser=:userName and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<ProcessGroup> getProcessGroupListPageByUser(@Param("userName") String userName, @Param("param") String param, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update ProcessGroup c set c.enableFlag = :enableFlag, c.lastUpdateUser = :lastUpdateUser, c.lastUpdateDttm = :lastUpdateDttm where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("lastUpdateUser") String lastUpdateUser, @Param("lastUpdateDttm") Date lastUpdateDttm, @Param("enableFlag") boolean enableFlag);

    @Transactional
    @Query(value = "select * from flow_process_group s where s.enable_flag = 1 and s.fk_flow_process_group_id = :fid and s.page_id = :pageId", nativeQuery = true)
    ProcessGroup getProcessGroupByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Query(value = "select s.id from flow_process_group s where s.enable_flag = 1 and s.fk_flow_process_group_id = :fid and s.page_id = :pageId", nativeQuery = true)
    String getProcessGroupIdByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Query(value = "select s from ProcessGroup s where s.enableFlag=true and s.appId=:appId")
    ProcessGroup getProcessGroupByAppId(@Param("appId") String appId);
}
