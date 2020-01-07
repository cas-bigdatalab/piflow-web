package com.nature.domain.system;

import com.nature.component.system.model.SysRole;
import com.nature.repository.system.SysRoleJpaRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SysRoleDomain {

    @Resource
    private SysRoleJpaRepository sysRoleJpaRepository;

    public SysRole saveOrUpdate(SysRole sysRole) {
        return sysRoleJpaRepository.save(sysRole);
    }

    public List<SysRole> saveOrUpdate(List<SysRole> sysRoleList) {
        return sysRoleJpaRepository.saveAll(sysRoleList);
    }

    public void delete(String id){
        sysRoleJpaRepository.deleteById(id);
    }

}
