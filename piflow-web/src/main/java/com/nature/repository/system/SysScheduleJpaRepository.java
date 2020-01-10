package com.nature.repository.system;

import com.nature.common.Eunm.ScheduleState;
import com.nature.component.system.model.SysSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

public interface SysScheduleJpaRepository extends JpaRepository<SysSchedule, String>, JpaSpecificationExecutor<SysSchedule>, Serializable {

    @Transactional
    @Modifying
    @Query("update SysSchedule c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

    List<SysSchedule> getSysSchedulesByStatus(@Param("status") ScheduleState status);

}
