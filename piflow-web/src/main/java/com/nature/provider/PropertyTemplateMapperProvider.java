package com.nature.provider;

import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.Utils;

public class PropertyTemplateMapperProvider {

	/**
	 * @Title 根據stops模板id查詢對應的stops的所有屬性
	 * 
	 * @param stopsId
	 * @return
	 */
	public String getPropertyTemplateBySotpsId(String stopsId) {

		String sqlStr = "select fspt.* from  fspt where fspt. = #{stopsId}";
		if (null != stopsId) {
			SQL sql = new SQL();
			sql.SELECT("*");
			sql.FROM("flow_stops_property_template");
			sql.WHERE("fk_stops_id = " + Utils.addSqlStr(stopsId));
			sql.WHERE("enable_flag = 1");
			sqlStr = sql.toString() + ";";
		}
		return sqlStr;
	}

}
