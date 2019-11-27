package com.nature.repository.flow;

import com.nature.component.flow.model.Stops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

public interface StopsJpaRepository extends JpaRepository<Stops, String>, JpaSpecificationExecutor<Stops>, Serializable {

    @Modifying
    @Query("update Stops c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

}
