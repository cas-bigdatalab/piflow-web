package cn.cnic.base.vo;

import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.component.system.entity.SysRole;
import cn.cnic.component.system.vo.SysMenuVo;
import lombok.Getter;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class UserVo implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String password;
    private String name;
    private Integer age;
    private List<SysMenuVo> sysMenuVoList;
    private List<SysRole> roles = new ArrayList<>();
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    @SuppressWarnings("unused")
    private Collection<? extends GrantedAuthority> authorities;

    public UserVo() {
    }

    // Write a constructor that can create uservo directly using user
    public UserVo(String id, String username, SysRoleType role, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        role = null == role ? SysRoleType.USER : role;
        authorities = Collections.singleton(new SimpleGrantedAuthority(role.getValue()));
    }

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
