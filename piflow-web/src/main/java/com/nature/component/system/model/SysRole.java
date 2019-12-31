package com.nature.component.system.model;

import com.nature.common.Eunm.SysRoleType;
import com.nature.component.system.model.SysUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "SYS_ROLE")
public class SysRole implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private SysRoleType role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_SYS_USER_ID")
    private SysUser sysUser;
}
