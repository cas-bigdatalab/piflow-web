package cn.cnic.component.process.jpa.repository;

import cn.cnic.component.process.entity.ProcessPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface ProcessPathJpaRepository extends JpaRepository<ProcessPath, String>, JpaSpecificationExecutor<ProcessPath>, Serializable {

    @Transactional
    @Modifying
    @Query("update ProcessPath c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

}
