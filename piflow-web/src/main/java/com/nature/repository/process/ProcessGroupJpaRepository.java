package com.nature.repository.process;

import com.nature.component.process.model.ProcessGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.Date;

public interface ProcessGroupJpaRepository extends JpaRepository<ProcessGroup, String>, JpaSpecificationExecutor<ProcessGroup>, Serializable {
    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from ProcessGroup c where c.enableFlag=1 and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<ProcessGroup> getProcessGroupListPage(@Param("param") String param, Pageable pageable);

    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from ProcessGroup c where c.enableFlag=1 and c.crtUser=:userName and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<ProcessGroup> getProcessGroupListPageByUser(@Param("userName") String userName, @Param("param") String param, Pageable pageable);

    @Modifying
    @Query("update ProcessGroup c set c.enableFlag = :enableFlag, c.lastUpdateUser = :lastUpdateUser, c.lastUpdateDttm = :lastUpdateDttm where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("lastUpdateUser") String lastUpdateUser, @Param("lastUpdateDttm") Date lastUpdateDttm, @Param("enableFlag") boolean enableFlag);
}
