package com.nature.mapper.sysUser;

import com.nature.component.sysUser.model.SysUser;
import com.nature.provider.sysUser.SysUserMapperProvider;
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
            @Result(column = "id", property = "roles", many = @Many(select = "com.nature.mapper.sysUser.SysRoleMapper.getSysRoleListBySysUserId", fetchType = FetchType.EAGER)),

    })
    public SysUser findUserByUserName(String userName);

    @Select("select * from SYS_USER")
    public List<SysUser> getUserList();

    @InsertProvider(type = SysUserMapperProvider.class, method = "addSysUser")
    public int addSysUser(SysUser sysUser);

    @UpdateProvider(type = SysUserMapperProvider.class, method = "updateSysUser")
    public int updateSysUser(SysUser sysUser);

    @Delete("delete from SYS_USER where id=#{id}")
    public int deleteUser(String id);
}
