package cn.cnic.component.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.FetchType;

import cn.cnic.component.system.mapper.provider.SysUserMapperProvider;
import cn.cnic.component.system.entity.SysUser;

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
