package com.nature.base.vo;

import com.nature.component.system.model.SysRole;
import com.nature.component.system.vo.SysMenuVo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class UserVo implements UserDetails, Serializable {

    private String username;
    private String password;
    private String name;
    private Integer age;
    private List<SysMenuVo> sysMenuVoList;
    private List<SysRole> roles = new ArrayList<>();
    List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    public void setAuthorities(List<GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }
}
