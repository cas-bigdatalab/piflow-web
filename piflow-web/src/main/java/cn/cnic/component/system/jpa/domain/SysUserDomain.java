package cn.cnic.component.system.jpa.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.jpa.repository.SysUserJpaRepository;

@Component
public class SysUserDomain {

    @Autowired
    private SysUserJpaRepository sysUserJpaRepository;

    public SysUser saveOrUpdate(SysUser sysUser) {
        return sysUserJpaRepository.save(sysUser);
    }

    public List<SysUser> saveOrUpdate(List<SysUser> sysUserList) {
        return sysUserJpaRepository.saveAll(sysUserList);
    }

    public void delete(String id){
        sysUserJpaRepository.deleteById(id);
    }

    public String checkUsername(String username){
        return sysUserJpaRepository.checkUsername(username);
    }

}
