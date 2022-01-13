package cn.cnic.component.user.mapper;

import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.vo.SysUserVo;
import cn.cnic.component.user.mapper.provider.AdminUserMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper

public interface AdminUserMapper {

    @InsertProvider(type = AdminUserMapperProvider.class, method = "insert")
    public int insert(SysUser user);

    @InsertProvider(type = AdminUserMapperProvider.class, method = "update")
    public int update(SysUser user);

    @SelectProvider(type = AdminUserMapperProvider.class, method = "getUserById")
    public SysUser getUserById(boolean isAdmin,String username, @Param("id") String id);

    @SelectProvider(type = AdminUserMapperProvider.class, method = "getUserById")
    public SysUserVo getUserVoById(boolean isAdmin,String username, @Param("id") String id);

    @SelectProvider(type = AdminUserMapperProvider.class, method = "getUserList")
    public List<SysUserVo> getUserList(@Param("isAdmin") boolean isAdmin,@Param("username") String username, @Param("param") String param);

}
