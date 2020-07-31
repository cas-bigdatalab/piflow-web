package cn.cnic.component.process.jpa.repository;

import cn.cnic.component.process.entity.Process;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface ProcessJpaRepository extends JpaRepository<Process, String>, JpaSpecificationExecutor<Process>, Serializable {
    /**
     * Paging query
     *
     * @return
     */
    @Query(value = "select c from Process c where c.enableFlag=true and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<Process> getProcessListPage(@Param("param") String param, Pageable pageable);

    /**
     * Paging query
     *
     * @return
     */
    @Query(value = "select c from Process c where c.enableFlag=true and c.crtUser=:userName and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<Process> getProcessListPage(@Param("userName") String userName, @Param("param") String param, Pageable pageable);

    @Query(value = "select s from Process s where s.enableFlag=true and s.processGroup is null and s.appId=:appId")
    Process getProcessNoGroupByAppId(@Param("appId") String appId);

    @Query(value = "select * from flow_process s where s.enable_flag = 1 and s.fk_flow_process_group_id = :fid and s.page_id = :pageId"
            , nativeQuery = true)
    Process getProcessByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Query(value = "select s.id from flow_process s where s.enable_flag = 1 and s.fk_flow_process_group_id = :fid and s.page_id = :pageId"
            , nativeQuery = true)
    String getProcessIdByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Query(value = "select s.id from flow_process s where s.enable_flag = 1 and s.fk_flow_process_group_id = :fid and s.name = :processName"
            , nativeQuery = true)
    String getProcessIdByNameAndProcessGroupId(@Param("fid") String fid, @Param("processName") String processName);

    @Query(value = "select MAX(s.page_id+0) from flow_process s where s.enable_flag = 1 and s.fk_flow_process_group_id = :processGroupId"
            , nativeQuery = true)
    String getMaxStopPageIdByProcessGroupId(@Param("processGroupId") String processGroupId);

    @Query(value = "select app_id from flow_process where enable_flag=1 and app_id is not null and ( ( state!='COMPLETED' and state!='FAILED' and state!='KILLED' ) or state is null )"
            , nativeQuery = true)
    List<String> getRunningProcessAppId();

}
