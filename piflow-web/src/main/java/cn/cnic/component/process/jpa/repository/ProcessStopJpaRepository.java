package cn.cnic.component.process.jpa.repository;

import cn.cnic.component.process.entity.ProcessStop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface ProcessStopJpaRepository extends JpaRepository<ProcessStop, String>, JpaSpecificationExecutor<ProcessStop>, Serializable {
    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from ProcessStop c where c.enableFlag=true and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<ProcessStop> getProcessStopListPage(@Param("param") String param, Pageable pageable);

    /**
     * Paging query
     *
     * @return
     */
    @Query("select c from ProcessStop c where c.enableFlag=true and c.crtUser=:userName and (c.name like CONCAT('%',:param,'%') or c.description like CONCAT('%',:param,'%'))")
    Page<ProcessStop> getProcessStopListPage(@Param("userName") String userName, @Param("param") String param, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update ProcessStop c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

}
