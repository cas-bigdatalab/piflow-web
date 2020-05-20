package com.nature.repository.mxGraph;


import com.nature.component.mxGraph.model.MxCell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface MxCellJpaRepository extends JpaRepository<MxCell, String>, JpaSpecificationExecutor<MxCell>, Serializable {

    @Query(nativeQuery = true, value = "select MAX(s.mx_pageid+0) from mx_cell s where s.enable_flag=1 and s.fk_mx_graph_id=:mxGraphModelId")
    Integer getMaxPageIdByMxGraphModelId(@Param("mxGraphModelId") String mxGraphModelId);
}
