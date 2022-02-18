package cn.cnic.component.system.mapper;

import java.util.List;

import cn.cnic.component.system.vo.SysUserVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import cn.cnic.component.system.mapper.provider.SysUserMapperProvider;
import cn.cnic.component.system.entity.SysUser;

@Mapper
public interface SysUserMapper {

    @InsertProvider(type = SysUserMapperProvider.class, method = "insertSysUser")
    public int insertSysUser(SysUser sysUser);

    @InsertProvider(type = SysUserMapperProvider.class, method = "updateSysUser")
    public int updateSysUser(SysUser user);

    @SelectProvider(type = SysUserMapperProvider.class, method = "getSysUserById")
    public SysUser getSysUserById(boolean isAdmin,String username, @Param("id") String id);

    @SelectProvider(type = SysUserMapperProvider.class, method = "getSysUserById")
    public SysUserVo getSysUserVoById(boolean isAdmin, String username, @Param("id") String id);

    @SelectProvider(type = SysUserMapperProvider.class, method = "getSysUserVoList")
    @Results({
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "id", property = "role", many = @Many(select = "cn.cnic.component.system.mapper.SysRoleMapper.getSysRoleBySysUserId", fetchType = FetchType.EAGER))
    })
    public List<SysUserVo> getSysUserVoList(@Param("isAdmin") boolean isAdmin,@Param("username") String username, @Param("param") String param);

    @SelectProvider(type = SysUserMapperProvider.class, method = "findUserByNameLike")
    public List<SysUser> findUserByNameLike(@Param("name") String name);

    @SelectProvider(type = SysUserMapperProvider.class, method = "findUserByName")
    public List<SysUser> findUserByName(String name);

    @SelectProvider(type = SysUserMapperProvider.class, method = "findUserByUserName")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "roles", many = @Many(select = "cn.cnic.component.system.mapper.SysRoleMapper.getSysRoleListBySysUserId", fetchType = FetchType.EAGER))
    })
    public SysUser findUserByUserName(String userName);

    @Delete("DELETE FROM sys_init_records WHERE id=#{id}")
    public int deleteUserById(String id);

    @Select("select username from sys_user where username=#{username}")
    public String checkUsername (String username);

}
