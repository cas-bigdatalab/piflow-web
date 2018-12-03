package com.nature.provider;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Template;

public class TemplateMapperProvider {

	/**
	 * 新增Template
	 * 
	 * @param template
	 * @return
	 */
	public String addTemplate(Template template) {
		String sqlStr = "";
		if (null != template) {
			String id = template.getId();
			String crtUser = template.getCrtUser();
			Date crtDttm = template.getCrtDttm();
			String lastUpdateUser = template.getLastUpdateUser();
			Date lastUpdateDttm = template.getLastUpdateDttm();
			Long version = template.getVersion();
			Boolean enableFlag = template.getEnableFlag();
			String name = template.getName();
			Flow flow = template.getFlow();
			String path = template.getPath();
			String value = template.getValue();
			String description = template.getDescription();
	 
			SQL sql = new SQL();

			// INSERT_INTO括号中为数据库表名
			sql.INSERT_INTO("flow_template");
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
			if (StringUtils.isNotBlank(description)) {
				sql.VALUES("description", Utils.addSqlStr(description));
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
			if (null != enableFlag) {
				int enableFlagInt = enableFlag ? 1 : 0;
				sql.VALUES("ENABLE_FLAG", enableFlagInt + "");
			}
			if (StringUtils.isNotBlank(name)) {
				sql.VALUES("NAME", Utils.addSqlStr(name));
			}
			if (StringUtils.isNotBlank(value)) {
				sql.VALUES("value", Utils.addSqlStr(value));
			}
			if (StringUtils.isNotBlank(path)) {
				sql.VALUES("path", Utils.addSqlStr(path));
			}
			if (null != flow) {
				sql.VALUES("fk_flow_id", Utils.addSqlStr(flow.getId()));
			} 
			sqlStr = sql.toString() + ";";
		}
		return sqlStr;
	}
	
	
}
