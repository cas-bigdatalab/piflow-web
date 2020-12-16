package cn.cnic.component.stopsComponent.mapper.provider;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.util.SqlUtils;

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

    public String getStopGroupByGroupNameList(@Param("group_name") List<String> groupName){
        String sql = "select * from flow_stops_groups where group_name in (" + SqlUtils.strListToStr(groupName) + ") and enable_flag = 1";
        return  sql;
    }
}
