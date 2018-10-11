package com.nature.provider.mxGraph;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.mxGraph.model.MxGeometry;

public class MxGeometryMapperProvider {

	public String addMxGeometry(MxGeometry mxGeometry) {
		String sql = "";
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
			sql = new SQL() {
				{
					// INSERT_INTO括号中为数据库表名
					INSERT_INTO("mx_geometry");
					// value中的第一个字符串为数据库中表对应的字段名
					// 除数字类型的字段外其他类型必须加单引号
					if (StringUtils.isNotBlank(id)) {
						VALUES("ID", Utils.addSqlStr(id));
					}
					if (null != crtDttm) {
						String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
						if (StringUtils.isNotBlank(crtDttmStr)) {
							VALUES("CRT_DTTM", Utils.addSqlStr(crtDttmStr));
						}
					}
					if (StringUtils.isNotBlank(crtUser)) {
						VALUES("CRT_USER", Utils.addSqlStr(crtUser));
					}
					if (null != enableFlag) {
						int enableFlagInt = enableFlag ? 1 : 0;
						VALUES("ENABLE_FLAG", enableFlagInt + "");
					}
					if (null != lastUpdateDttm) {
						String lastUpdateDttmStr = DateUtils.dateTimesToStr(lastUpdateDttm);
						if (StringUtils.isNotBlank(lastUpdateDttmStr)) {
							VALUES("LAST_UPDATE_DTTM", Utils.addSqlStr(lastUpdateDttmStr));
						}
					}
					if (StringUtils.isNotBlank(lastUpdateUser)) {
						VALUES("LAST_UPDATE_USER", Utils.addSqlStr(lastUpdateUser));
					}
					if (null != version && StringUtils.isNotBlank(version.toString())) {
						VALUES("VERSION", version.toString());
					}
					if (StringUtils.isNotBlank(as)) {
						VALUES("MX_AS", Utils.addSqlStr(as));
					}
					if (StringUtils.isNotBlank(relative)) {
						VALUES("MX_RELATIVE", Utils.addSqlStr(relative));
					}
					if (StringUtils.isNotBlank(height)) {
						VALUES("MX_HEIGHT", Utils.addSqlStr(height));
					}
					if (StringUtils.isNotBlank(width)) {
						VALUES("MX_WIDTH", Utils.addSqlStr(width));
					}
					if (StringUtils.isNotBlank(x)) {
						VALUES("MX_X", Utils.addSqlStr(x));
					}
					if (StringUtils.isNotBlank(y)) {
						VALUES("MX_Y", Utils.addSqlStr(y));
					}
				}
			}.toString() + ";";
		}
		return sql;
	};

}
