package com.nature.mapper.system;

import com.nature.component.system.model.SysUser;
import com.nature.provider.system.SysUserMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface SysUserMapper {
    @SelectProvider(type = SysUserMapperProvider.class, method = "findUserByNameLike")
    public List<SysUser> findUserByNameLike(@Param("name") String name);

    @SelectProvider(type = SysUserMapperProvider.class, method = "findUserByName")
    public List<SysUser> findUserByName(String name);

    @SelectProvider(type = SysUserMapperProvider.class, method = "findUserByUserName")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "roles", many = @Many(select = "com.nature.mapper.system.SysRoleMapper.getSysRoleListBySysUserId", fetchType = FetchType.EAGER)),

    })
    public SysUser findUserByUserName(String userName);

    @Select("select * from sys_user")
    public List<SysUser> getUserList();

    @InsertProvider(type = SysUserMapperProvider.class, method = "addSysUser")
    public int addSysUser(SysUser sysUser);

    @UpdateProvider(type = SysUserMapperProvider.class, method = "updateSysUser")
    public int updateSysUser(SysUser sysUser);

    @Delete("delete from sys_user where id=#{id}")
    public int deleteUser(String id);
}
