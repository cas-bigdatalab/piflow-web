package cn.cnic.component.mxGraph.jpa.repository;


import cn.cnic.component.mxGraph.entity.MxGeometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface MxGeometryJpaRepository extends JpaRepository<MxGeometry, String>, JpaSpecificationExecutor<MxGeometry>, Serializable {

    @Transactional
    @Modifying
    @Query("update MxGeometry c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);
}
