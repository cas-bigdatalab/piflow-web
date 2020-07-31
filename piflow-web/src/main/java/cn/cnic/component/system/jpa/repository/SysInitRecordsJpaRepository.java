package cn.cnic.component.system.jpa.repository;

import cn.cnic.component.system.entity.SysInitRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface SysInitRecordsJpaRepository extends JpaRepository<SysInitRecords, String>, JpaSpecificationExecutor<SysInitRecords>, Serializable {
}
