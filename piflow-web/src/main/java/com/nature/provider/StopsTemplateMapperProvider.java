package com.nature.provider;

import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.Utils;

public class StopsTemplateMapperProvider {

	/**
	 * custom sql 自定义sql
	 * 
	 * @param stopsName stops的名字
	 * @return
	 */
	public String getStopsTemplateByName(String stopsName) {
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("flow_stops_template");
		sql.WHERE("name = " + Utils.addSqlStr(stopsName));
		return sql.toString();
	};

}
