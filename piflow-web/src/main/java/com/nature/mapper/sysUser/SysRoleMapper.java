package com.nature.mapper.sysUser;

import com.nature.component.sysUser.model.SysRole;
import com.nature.component.sysUser.model.SysUser;
import com.nature.provider.sysUser.SysRoleMapperProvider;
import com.nature.provider.sysUser.SysUserMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    /**
     * getSysRoleListBySysUserId
     *
     * @param sysUserId
     * @return
     */
    @SelectProvider(type = SysRoleMapperProvider.class, method = "getSysRoleListBySysUserId")
    public List<SysRole> getSysRoleListBySysUserId(@Param("sysUserId") String sysUserId);

    @InsertProvider(type = SysRoleMapperProvider.class, method = "addSysRoleList")
    public int addSysRoleList(@Param("list") List<SysRole> list, @Param("sysUserId") String sysUserId);


}
