package cn.cnic.component.system.mapper;

import cn.cnic.component.stopsComponent.mapper.provider.StopsComponentMapperProvider;
import cn.cnic.component.system.model.SysUser;
import cn.cnic.component.system.mapper.provider.SysUserMapperProvider;
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
            @Result(column = "id", property = "roles", many = @Many(select = "cn.cnic.component.system.mapper.SysRoleMapper.getSysRoleListBySysUserId", fetchType = FetchType.EAGER)),

    })
    public SysUser findUserByUserName(String userName);

    @Select("select * from sys_user")
    public List<SysUser> getUserList();

    @InsertProvider(type = SysUserMapperProvider.class, method = "insertSysUser")
    public int insertSysUser(SysUser sysUser);

}
