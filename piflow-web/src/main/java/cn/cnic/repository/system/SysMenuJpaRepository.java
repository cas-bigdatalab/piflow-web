package cn.cnic.repository.system;

import cn.cnic.component.system.model.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface SysMenuJpaRepository extends JpaRepository<SysMenu, String>, JpaSpecificationExecutor<SysMenu>, Serializable {

}
