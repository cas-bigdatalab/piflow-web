package cn.cnic.mapper.system;

import cn.cnic.component.system.model.SysRole;
import cn.cnic.provider.system.SysRoleMapperProvider;
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

}
