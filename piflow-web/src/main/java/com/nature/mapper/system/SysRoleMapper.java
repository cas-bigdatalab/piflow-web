package com.nature.mapper.system;

import com.nature.component.system.model.SysRole;
import com.nature.provider.system.SysRoleMapperProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

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
