package com.nature.mapper.Statistics;

import com.nature.component.Statistics.model.Statistics;
import com.nature.provider.Statistics.StatisticsMapperProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StatisticsMapper {
    /**
     * addStatistics
     *
     * @param statistics
     * @return
     */
    @InsertProvider(type = StatisticsMapperProvider.class, method = "addStatistics")
    public int addStatistics(Statistics statistics);


}
