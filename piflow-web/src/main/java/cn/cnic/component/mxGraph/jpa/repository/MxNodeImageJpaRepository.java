package cn.cnic.component.mxGraph.jpa.repository;


import cn.cnic.component.mxGraph.entity.MxNodeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

public interface MxNodeImageJpaRepository extends JpaRepository<MxNodeImage, String>, JpaSpecificationExecutor<MxNodeImage>, Serializable {

    @Modifying
    @Query("update MxNodeImage c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);
}
