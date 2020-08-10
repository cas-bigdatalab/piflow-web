package cn.cnic.component.dashboard.mapper.provider;

import cn.cnic.base.util.SqlUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public class StatisticProvider {

    public String getFlowProcessStatisticInfo(){
        String sql = "select state, count(*) as count from flow_process where enable_flag = 1 and fk_flow_process_group_id is null GROUP BY state";
        return  sql;
    }

    public String getFlowCount(){
        String sql = "select count(*) as count from flow where enable_flag = 1 and fk_flow_group_id is null";
        return  sql;
    }

    public String getGroupProcessStatisticInfo(){
        String sql = "select state, count(*) as count from flow_process where enable_flag = 1 and fk_flow_process_group_id is null GROUP BY state";
        return  sql;
    }

    public String getGroupCount(){
        String sql = "select count(*) as count from flow_group where enable_flag = 1 and fk_flow_group_id is null";
        return  sql;
    }
}
