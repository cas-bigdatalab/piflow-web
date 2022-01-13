package cn.cnic.component.user.mapper;

import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.system.entity.SysLog;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.vo.SysLogVo;
import cn.cnic.component.system.vo.SysUserVo;
import cn.cnic.component.user.mapper.provider.AdminLogMapperProvider;
import cn.cnic.component.user.mapper.provider.AdminUserMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminLogMapper {

    /**
     * getLogList
     *
     * @param param
     * @return
     */
    @SelectProvider(type = AdminLogMapperProvider.class, method = "getLogList")
    public List<SysLogVo> getLogList(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("param") String param);


    @InsertProvider(type = AdminLogMapperProvider.class, method = "insertSelective")
    public int insertSelective(SysLog record);

}
