package cn.cnic.component.system.mapper;

import cn.cnic.component.system.mapper.provider.SysRoleMapperProvider;
import cn.cnic.component.system.entity.SysRole;
import cn.cnic.component.system.vo.SysUserVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    @Select("select Max(id) from sys_role")
    public long getMaxId();

    /**
     * getSysRoleListBySysUserId
     *
     * @param sysUserId
     * @return
     */
    @SelectProvider(type = SysRoleMapperProvider.class, method = "getSysRoleListBySysUserId")
    public List<SysRole> getSysRoleListBySysUserId(@Param("sysUserId") String sysUserId);

    @InsertProvider(type = SysRoleMapperProvider.class, method = "insertSysRoleList")
    public int insertSysRoleList(@Param("userId") String userId, @Param("roles") List<SysRole> roles);
    
    @SelectProvider(type = SysRoleMapperProvider.class, method = "getSysRoleBySysUserId")
    public SysRole getSysRoleBySysUserId(@Param("sysUserId") String sysUserId);

    @UpdateProvider(type = SysRoleMapperProvider.class, method = "updateRole")
    int updateRole(SysUserVo sysUserVo);

    @Select("select distinct role from sys_role group by role")
    List<SysRole> getAllRole();

}
