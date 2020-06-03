package cn.cnic.component.stopsComponent.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

public class StopsComponentGroupProvider {

    /**
     * 查詢所有組
     *
     * @return
     */
    public String getStopGroupList() {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops_groups");
        sql.WHERE("enable_flag = 1");
        sql.ORDER_BY(" group_name ");
        sqlStr = sql.toString();
        return sqlStr;
    }
}
