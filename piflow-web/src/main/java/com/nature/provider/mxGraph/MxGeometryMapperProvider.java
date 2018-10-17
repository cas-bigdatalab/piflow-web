package com.nature.provider.mxGraph;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.mxGraph.model.MxGeometry;

public class MxGeometryMapperProvider {

	/**
	 * @Title 新增MxGeometry
	 * 
	 * @param mxGeometry
	 * @return
	 */
	public String addMxGeometry(MxGeometry mxGeometry) {
		String sqlStr = "";
		if (null != mxGeometry) {
			String id = mxGeometry.getId();
			String crtUser = mxGeometry.getCrtUser();
			Date crtDttm = mxGeometry.getCrtDttm();
			Boolean enableFlag = mxGeometry.getEnableFlag();
			Date lastUpdateDttm = mxGeometry.getLastUpdateDttm();
			String lastUpdateUser = mxGeometry.getLastUpdateUser();
			Long version = mxGeometry.getVersion();
			String as = mxGeometry.getAs();
			String relative = mxGeometry.getRelative();
			String height = mxGeometry.getHeight();
			String width = mxGeometry.getWidth();
			String x = mxGeometry.getX();
			String y = mxGeometry.getY();
			SQL sql = new SQL();

			// INSERT_INTO括号中为数据库表名
			sql.INSERT_INTO("mx_geometry");
			// value中的第一个字符串为数据库中表对应的字段名
			// 除数字类型的字段外其他类型必须加单引号
			if (StringUtils.isNotBlank(id)) {
				sql.VALUES("ID", Utils.addSqlStr(id));
			}
			if (null != crtDttm) {
				String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
				if (StringUtils.isNotBlank(crtDttmStr)) {
					sql.VALUES("CRT_DTTM", Utils.addSqlStr(crtDttmStr));
				}
			}
			if (StringUtils.isNotBlank(crtUser)) {
				sql.VALUES("CRT_USER", Utils.addSqlStr(crtUser));
			}
			if (null != enableFlag) {
				int enableFlagInt = enableFlag ? 1 : 0;
				sql.VALUES("ENABLE_FLAG", enableFlagInt + "");
			}
			if (null != lastUpdateDttm) {
				String lastUpdateDttmStr = DateUtils.dateTimesToStr(lastUpdateDttm);
				if (StringUtils.isNotBlank(lastUpdateDttmStr)) {
					sql.VALUES("LAST_UPDATE_DTTM", Utils.addSqlStr(lastUpdateDttmStr));
				}
			}
			if (StringUtils.isNotBlank(lastUpdateUser)) {
				sql.VALUES("LAST_UPDATE_USER", Utils.addSqlStr(lastUpdateUser));
			}
			if (null != version && StringUtils.isNotBlank(version.toString())) {
				sql.VALUES("VERSION", version.toString());
			}
			if (StringUtils.isNotBlank(as)) {
				sql.VALUES("MX_AS", Utils.addSqlStr(as));
			}
			if (StringUtils.isNotBlank(relative)) {
				sql.VALUES("MX_RELATIVE", Utils.addSqlStr(relative));
			}
			if (StringUtils.isNotBlank(height)) {
				sql.VALUES("MX_HEIGHT", Utils.addSqlStr(height));
			}
			if (StringUtils.isNotBlank(width)) {
				sql.VALUES("MX_WIDTH", Utils.addSqlStr(width));
			}
			if (StringUtils.isNotBlank(x)) {
				sql.VALUES("MX_X", Utils.addSqlStr(x));
			}
			if (StringUtils.isNotBlank(y)) {
				sql.VALUES("MX_Y", Utils.addSqlStr(y));
			}
			sqlStr = sql.toString() + ";";
		}
		return sqlStr;
	}

	/**
	 * @Title 修改MxGeometry
	 * 
	 * @param mxGeometry
	 * @return
	 */
	public String updateMxGeometry(MxGeometry mxGeometry) {
		String sqlStr = "";
		if (null != mxGeometry) {
			String id = mxGeometry.getId();
			Boolean enableFlag = mxGeometry.getEnableFlag();
			Date lastUpdateDttm = mxGeometry.getLastUpdateDttm();
			String lastUpdateUser = mxGeometry.getLastUpdateUser();
			Long version = mxGeometry.getVersion();
			String as = mxGeometry.getAs();
			String relative = mxGeometry.getRelative();
			String height = mxGeometry.getHeight();
			String width = mxGeometry.getWidth();
			String x = mxGeometry.getX();
			String y = mxGeometry.getY();
			SQL sql = new SQL();
			// UPDATE括号中为数据库表名
			sql.UPDATE("mx_geometry");
			// SET中的第一个字符串为数据库中表对应的字段名
			// 除数字类型的字段外其他类型必须加单引号
			if (null != enableFlag) {
				int enableFlagInt = enableFlag ? 1 : 0;
				sql.SET("ENABLE_FLAG = " + enableFlagInt);
			}
			if (null != lastUpdateDttm) {
				String lastUpdateDttmStr = DateUtils.dateTimesToStr(lastUpdateDttm);
				if (StringUtils.isNotBlank(lastUpdateDttmStr)) {
					sql.SET("LAST_UPDATE_DTTM = " + Utils.addSqlStr(lastUpdateDttmStr));
				}
			}
			if (StringUtils.isNotBlank(lastUpdateUser)) {
				sql.SET("LAST_UPDATE_USER = " + Utils.addSqlStr(lastUpdateUser));
			}
			if (null != version && StringUtils.isNotBlank(version.toString())) {
				sql.SET("VERSION = " + version.toString());
			}
			if (StringUtils.isNotBlank(as)) {
				sql.SET("MX_AS = " + Utils.addSqlStr(as));
			}
			if (StringUtils.isNotBlank(relative)) {
				sql.SET("MX_RELATIVE = " + Utils.addSqlStr(relative));
			}
			if (StringUtils.isNotBlank(height)) {
				sql.SET("MX_HEIGHT = " + Utils.addSqlStr(height));
			}
			if (StringUtils.isNotBlank(width)) {
				sql.SET("MX_WIDTH = " + Utils.addSqlStr(width));
			}
			if (StringUtils.isNotBlank(x)) {
				sql.SET("MX_X = " + Utils.addSqlStr(x));
			}
			if (StringUtils.isNotBlank(y)) {
				sql.SET("MX_Y = " + Utils.addSqlStr(y));
			}
			sql.WHERE("id = " + Utils.addSqlStr(id));

			sqlStr = sql.toString() + ";";

			if (StringUtils.isBlank(id)) {
				sqlStr = "";
			}
		}
		return sqlStr;
	}

	public String getMxGeometryById(String id) {
		String sqlStr = "";
		if (StringUtils.isNotBlank(id)) {
			SQL sql = new SQL();
			sql.SELECT("*");
			sql.FROM("mx_geometry");
			sql.WHERE("id = " + Utils.addSqlStr(id));
			sql.WHERE("ENABLE_FLAG = 1");
			sqlStr = sql.toString() + ";";
		}

		return sqlStr;
	}
}
