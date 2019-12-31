package com.nature.repository.flow;

import com.nature.component.flow.model.Paths;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

public interface PathsJpaRepository extends JpaRepository<Paths, String>, JpaSpecificationExecutor<Paths>, Serializable {

    @Modifying
    @Query("update Paths c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

}
