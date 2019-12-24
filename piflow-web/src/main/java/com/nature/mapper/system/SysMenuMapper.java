package com.nature.mapper.system;

import com.nature.component.system.model.SysMenu;
import com.nature.provider.system.SysMenuMapperProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface SysMenuMapper {

    /**
     * getSysMenuList
     *
     * @param role
     * @return
     */
    @SelectProvider(type = SysMenuMapperProvider.class, method = "getSysMenuList")
    public List<SysMenu> getSysMenuList(@Param("role") String role);


}
