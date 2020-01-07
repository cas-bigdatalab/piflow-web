package com.nature.repository.system;

import com.nature.component.system.model.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface SysRoleJpaRepository extends JpaRepository<SysRole, String>, JpaSpecificationExecutor<SysRole>, Serializable {

}
