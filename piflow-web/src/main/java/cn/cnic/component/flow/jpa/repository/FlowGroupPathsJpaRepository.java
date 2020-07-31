package cn.cnic.component.flow.jpa.repository;

import cn.cnic.component.flow.entity.FlowGroupPaths;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface FlowGroupPathsJpaRepository extends JpaRepository<FlowGroupPaths, String>, JpaSpecificationExecutor<FlowGroupPaths>, Serializable {

    @Transactional
    @Modifying
    @Query("update FlowGroupPaths c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

}
