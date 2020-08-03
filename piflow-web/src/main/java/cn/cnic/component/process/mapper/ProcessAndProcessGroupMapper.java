package cn.cnic.component.process.mapper;

import cn.cnic.component.process.mapper.provider.ProcessAndProcessGroupMapperProvider;
import cn.cnic.component.process.vo.ProcessAndProcessGroupVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ProcessAndProcessGroupMapper {

    /**
     * query all TemplateDataSource
     *
     * @return
     */
    @SelectProvider(type = ProcessAndProcessGroupMapperProvider.class, method = "getProcessAndProcessGroupList")
    public List<ProcessAndProcessGroupVo> getProcessAndProcessGroupList(String param);

    @SelectProvider(type = ProcessAndProcessGroupMapperProvider.class, method = "getProcessAndProcessGroupListByUser")
    public List<ProcessAndProcessGroupVo> getProcessAndProcessGroupListByUser(String param, String username);


}
