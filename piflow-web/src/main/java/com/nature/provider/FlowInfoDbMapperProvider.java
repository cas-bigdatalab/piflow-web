package com.nature.provider;

import org.apache.ibatis.jdbc.SQL;

public class FlowInfoDbMapperProvider {
	/**
	 * @Title 根据flowId查询appId 信息
	 * @param flowId
	 * @return
	 */
	public String getFlowInfoDbListByFlowId(String flowId) {
		String sqlStr = "";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("flow_info");
		sql.WHERE("enable_flag = 1");
		sql.WHERE("id =  (SELECT app_id from flow where id = '"+flowId+"')" );
		sqlStr = sql.toString() + ";";
		return sqlStr;
	}

}
