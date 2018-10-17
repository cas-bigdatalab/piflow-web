package com.nature.provider;

import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.Utils;

public class StopsTemplateMapperProvider {

	/**
	 * @Title 查詢所有stops模板
	 * 
	 * @return
	 */
	public String getStopsTemplateList() {
		String sqlStr = "";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("flow_stops_template");
		sql.WHERE("enable_flag = 1 ");
		sqlStr = sql.toString() + ";";
		return sqlStr;
	}

	/**
	 * @Title 根據stops模板id查詢模板
	 * 
	 * @param id
	 * @return
	 */
	public String getStopsTemplateById(String id) {
		String sqlStr = "";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("flow_stops_template");
		sql.WHERE("enable_flag = 1 ");
		sql.WHERE("id = " + Utils.addSqlStr(id));
		sqlStr = sql.toString() + ";";
		return sqlStr;
	}

	/**
	 * @Title 根據stops模板的id查詢stops模板(包括屬性list)
	 * 
	 * @param id
	 * @return
	 */
	public String getStopsPropertyById(String id) {
		String sqlStr = "";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("flow_stops_template");
		sql.WHERE("enable_flag = 1 ");
		sql.WHERE("id = " + Utils.addSqlStr(id));
		sqlStr = sql.toString() + ";";
		return sqlStr;
	}

	/**
	 * @Title 根据stopsName查询StopsTemplate
	 * 
	 * @param stopsName
	 * @return
	 */
	public String getStopsTemplateByName(String stopsName) {
		String sqlStr = "";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("flow_stops_template");
		sql.WHERE("enable_flag = 1 ");
		sql.WHERE("name = " + Utils.addSqlStr(stopsName));
		sqlStr = sql.toString() + ";";
		return sqlStr;
	};

}
