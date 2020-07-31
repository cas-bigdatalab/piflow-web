package cn.cnic.component.process.jpa.repository;

import cn.cnic.component.process.entity.ProcessGroupPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface ProcessGroupPathJpaRepository extends JpaRepository<ProcessGroupPath, String>, JpaSpecificationExecutor<ProcessGroupPath>, Serializable {

    @Transactional
    @Modifying
    @Query("update ProcessGroupPath c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

    @Transactional
    @Query(nativeQuery = true, value = "select * from  flow_process_group_path where enable_flag = 1 and fk_flow_process_group_id = :fid and page_id = :pageId")
    ProcessGroupPath getProcessGroupPathByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

}
