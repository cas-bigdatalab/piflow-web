package cn.cnic.component.system.mapper;

import cn.cnic.component.system.entity.SysLog;
import cn.cnic.component.system.vo.SysLogVo;
import cn.cnic.component.system.mapper.provider.AdminLogMapperProvider;
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
