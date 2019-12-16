package com.nature.repository.system;

import com.nature.component.system.model.SysInitRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

public interface SysInitRecordsJpaRepository extends JpaRepository<SysInitRecords, String>, JpaSpecificationExecutor<SysInitRecords>, Serializable {
}
