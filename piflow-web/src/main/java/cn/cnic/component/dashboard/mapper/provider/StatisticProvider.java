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
        String sql = "select count(*) as count from flow where enable_flag = 1 and fk_flow_group_id is null and is_example != 1";
        return  sql;
    }

    public String getGroupProcessStatisticInfo(){
        String sql = "select state, count(*) as count from flow_process_group where enable_flag = 1 and fk_flow_process_group_id is null GROUP BY state";
        return  sql;
    }

    public String getGroupCount(){
        String sql = "select count(*) as count from flow_group where enable_flag = 1 and fk_flow_group_id is null and is_example != 1";
        return  sql;
    }

    public String getScheduleStatisticInfo(){
        String sql = "select status, count(*) as count from group_schedule where enable_flag = 1 GROUP BY status";
        return  sql;
    }


    public String getTemplateCount(){
        String sql = "select count(*) as count from flow_template where enable_flag = 1";
        return  sql;
    }

    public String getDataSourceCount(){
        String sql = "select count(*) as count from data_source where enable_flag = 1 and is_template = 0";
        return  sql;
    }

    public String getStopsHubCount(){
        String sql = "select count(*) as count from stops_hub where enable_flag = 1";
        return  sql;
    }

    public String getStopsCount(){
        String sql = "select count(*) as count from flow_stops_template";
        return  sql;
    }

    public String getStopsGroupCount(){
        String sql = "select count(*) as count from flow_stops_groups";
        return  sql;
    }
}
