package cn.cnic.component.system.jpa.repository;

import cn.cnic.component.system.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

public interface SysUserJpaRepository extends JpaRepository<SysUser, String>, JpaSpecificationExecutor<SysUser>, Serializable {

    @Query(value = "select username from SysUser where username=:username")
    public String checkUsername(@Param("username") String username);

}
