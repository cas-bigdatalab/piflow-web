package cn.cnic.component.system.mapper;

import cn.cnic.component.system.entity.SysInitRecords;
import cn.cnic.component.system.mapper.provider.SysInitRecordsMapperProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysInitRecordsMapper {

    @InsertProvider(type = SysInitRecordsMapperProvider.class, method = "insertSysInitRecords")
    public Integer insertSysInitRecords(SysInitRecords sysInitRecords);

    @Select("select * from sys_init_records")
    public List<SysInitRecords> getSysInitRecordsList();

    @Select("select * from sys_init_records where id=#{id}")
    public SysInitRecords getSysInitRecordsById(String id);

    @Select("select * from sys_init_records order by init_date desc limit #{limit}")
    public SysInitRecords getSysInitRecordsLastNew(int limit);

}
