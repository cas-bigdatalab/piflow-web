package com.nature.mapper.sysUser;

import com.nature.component.sysUser.model.SysUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysUserMapper {
    @Select("select * from SYS_USER where name like #{name}")
    public List<SysUser> findUserByName(@Param("name") String name);

    @Select("select * from SYS_USER where name = #{name}")
    public SysUser findUserByNames(String name);

    @Select("select * from SYS_USER where username = #{userName}")
    public SysUser findUserByUserName(String userName);

    @Select("select * from SYS_USER")
    public List<SysUser> getUserList();

    @Insert("insert into SYS_USER(id,name,age) values (#{id},#{name},#{age})")
    public int addUser(SysUser user);

    @Update("update SYS_USER set name=#{name},age=#{age} where id=#{id}")
    public int saveOrUpdate(SysUser user);

    @Delete("delete from SYS_USER where id=#{id}")
    public int deleteUser(int id);
}
