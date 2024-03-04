package cn.cnic.component.system.mapper;

import cn.cnic.component.system.entity.Role;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.mapper.provider.SysUserMapperProvider;
import cn.cnic.component.system.vo.SysUserVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select("select * from role, user_role as ur where ur.username = #{username} and ur.role_id = role.id ")
    List<Role> getListByUsername(String username);
}
