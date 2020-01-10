package com.nature.repository.flow;

import com.nature.component.flow.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

public interface PropertyJpaRepository extends JpaRepository<Property, String>, JpaSpecificationExecutor<Property>, Serializable {

    @Transactional
    @Modifying
    @Query("update Property c set c.enableFlag = :enableFlag where c.id = :id")
    int updateEnableFlagById(@Param("id") String id, @Param("enableFlag") boolean enableFlag);

    @Query(value = "select * from flow_stops_property fsp where fsp.enable_flag = 1 and fsp.fk_stops_id = (:stopId)", nativeQuery = true)
    List<Property> getPropertyListByStopsId(@Param("stopId") String stopId);

}
