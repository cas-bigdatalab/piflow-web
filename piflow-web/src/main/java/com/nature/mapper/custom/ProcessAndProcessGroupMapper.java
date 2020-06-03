package com.nature.mapper.custom;

import com.nature.component.process.vo.ProcessAndProcessGroupVo;
import com.nature.provider.custom.ProcessAndProcessGroupMapperProvider;
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
