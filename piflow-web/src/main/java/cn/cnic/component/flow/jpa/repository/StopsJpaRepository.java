package cn.cnic.component.flow.jpa.repository;

import cn.cnic.component.flow.entity.Stops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface StopsJpaRepository extends JpaRepository<Stops, String>, JpaSpecificationExecutor<Stops>, Serializable {

    @Query("select c from Stops c where c.enableFlag=true and c.id=:id")
    Stops getStopsById(@Param("id") String id);

    @Query(nativeQuery = true, value = "select MAX(page_id+0) from flow_stops where enable_flag=1 and fk_flow_id=:flowId ")
    Integer getMaxStopPageIdByFlowId(@Param("flowId") String flowId);

    @Query(nativeQuery = true, value = "SELECT fs.name from flow_stops fs WHERE fs.enable_flag=1 and fs.fk_flow_id =:flowId")
    String[] getStopNamesByFlowId(@Param("flowId") String flowId);

    @Query(nativeQuery = true, value = "select * from flow_stops where enable_flag=1 and fk_flow_id=:fid and page_id=:stopPageId  limit 1")
    Stops getStopsByPageId(@Param("fid") String fid, @Param("stopPageId") String stopPageId);

    @Query(value = "select id from Stops where enableFlag=true")
    List<String> getStopsIdList();

}
