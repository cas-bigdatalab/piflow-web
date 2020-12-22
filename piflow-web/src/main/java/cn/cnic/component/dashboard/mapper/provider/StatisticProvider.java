package cn.cnic.component.dashboard.mapper.provider;

public class StatisticProvider {

    public String getFlowProcessStatisticInfo(){
        String sql = "SELECT state, count(*) AS count FROM flow_process WHERE enable_flag=1 AND app_id IS NOT NULL AND fk_flow_process_group_id IS NULL GROUP BY state";
        return  sql;
    }

    public String getFlowCount(){
        String sql = "SELECT count(*) AS count FROM flow where enable_flag = 1 AND fk_flow_group_id IS NULL AND is_example != 1";
        return  sql;
    }

    public String getGroupProcessStatisticInfo(){
        String sql = "SELECT state, count(*) as count FROM flow_process_group WHERE enable_flag=1 AND app_id IS NOT NULL AND fk_flow_process_group_id IS NULL GROUP BY state;";
        return  sql;
    }

    public String getGroupCount(){
        String sql = "SELECT count(*) as count FROM flow_group where enable_flag = 1 AND fk_flow_group_id IS NULL AND is_example != 1";
        return  sql;
    }

    public String getScheduleStatisticInfo(){
        String sql = "SELECT status, count(*) as count FROM group_schedule where enable_flag = 1 GROUP BY status";
        return  sql;
    }


    public String getTemplateCount(){
        String sql = "SELECT count(*) as count FROM flow_template where enable_flag = 1";
        return  sql;
    }

    public String getDataSourceCount(){
        String sql = "SELECT count(*) as count FROM data_source where enable_flag = 1 AND is_template = 0";
        return  sql;
    }

    public String getStopsHubCount(){
        String sql = "SELECT count(*) as count FROM stops_hub where enable_flag = 1";
        return  sql;
    }

    public String getStopsCount(){
        String sql = "SELECT count(*) as count FROM flow_stops_template";
        return  sql;
    }

    public String getStopsGroupCount(){
        String sql = "SELECT count(*) as count FROM flow_stops_groups";
        return  sql;
    }
}
