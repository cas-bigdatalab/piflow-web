package com.nature.provider;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.FlowInfoDb;

public class FlowMapperProvider {

	/**
	 * @Title 新增Flow
	 * 
	 * @param flow
	 * @return
	 */
	public String addFlow(Flow flow) {
		String sqlStr = "";
		if (null != flow) {
			String id = flow.getId();
			String crtUser = flow.getCrtUser();
			Date crtDttm = flow.getCrtDttm();
			String lastUpdateUser = flow.getLastUpdateUser();
			Date lastUpdateDttm = flow.getLastUpdateDttm();
			Long version = flow.getVersion();
			Boolean enableFlag = flow.getEnableFlag();
			String description = flow.getDescription();
			String name = flow.getName();
			String uuid = flow.getUuid();
			MxGraphModel mxGraphModel = flow.getMxGraphModel();
			FlowInfoDb appId = flow.getAppId();
			SQL sql = new SQL();

			// INSERT_INTO括号中为数据库表名
			sql.INSERT_INTO("flow");
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
			if (StringUtils.isNotBlank(description)) {
				sql.VALUES("description", Utils.addSqlStr(description));
			}
			if (StringUtils.isNotBlank(name)) {
				sql.VALUES("NAME", Utils.addSqlStr(name));
			}
			if (StringUtils.isNotBlank(uuid)) {
				sql.VALUES("UUID", Utils.addSqlStr(uuid));
			}
			if (null != mxGraphModel) {
				String mxGraphModelId = mxGraphModel.getId();
				if (StringUtils.isNotBlank(mxGraphModelId)) {
					sql.VALUES("fk_mx_graph_model_id", Utils.addSqlStr(mxGraphModelId));
				}
			}
			if (null != appId) {
				String app_id = appId.getId();
				if (StringUtils.isNotBlank(app_id)) {
					sql.VALUES("app_id", Utils.addSqlStr(app_id));
				}
			}
			sqlStr = sql.toString() + ";";
		}
		return sqlStr;
	}

	/**
	 * @Title 修改Flow
	 * 
	 * @param flow
	 * @return
	 */
	public String updateFlow(Flow flow) {

		String sqlStr = "";
		if (null != flow) {
			String id = flow.getId();
			String lastUpdateUser = flow.getLastUpdateUser();
			Date lastUpdateDttm = flow.getLastUpdateDttm();
			Long version = flow.getVersion();
			Boolean enableFlag = flow.getEnableFlag();
			String description = flow.getDescription();
			String name = flow.getName();
			String uuid = flow.getUuid();
			MxGraphModel mxGraphModel = flow.getMxGraphModel();
			FlowInfoDb appId = flow.getAppId();
			SQL sql = new SQL();

			// INSERT_INTO括号中为数据库表名
			sql.UPDATE("flow");
			// SET中的第一个字符串为数据库中表对应的字段名
			// 除数字类型的字段外其他类型必须加单引号
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
			if (null != enableFlag) {
				int enableFlagInt = enableFlag ? 1 : 0;
				sql.SET("ENABLE_FLAG = " + enableFlagInt);
			}
			if (StringUtils.isNotBlank(description)) {
				sql.SET("description = " + Utils.addSqlStr(description));
			}
			if (StringUtils.isNotBlank(name)) {
				sql.SET("NAME = " + Utils.addSqlStr(name));
			}
			if (StringUtils.isNotBlank(uuid)) {
				sql.SET("UUID = " + Utils.addSqlStr(uuid));
			}
			if (null != mxGraphModel) {
				String mxGraphModelId = mxGraphModel.getId();
				if (StringUtils.isNotBlank(mxGraphModelId)) {
					sql.SET("fk_mx_graph_model_id = " + Utils.addSqlStr(mxGraphModelId));
				}
			}
			if (null != appId) {
				String appid = appId.getId();
				if (StringUtils.isNotBlank(appid)) {
					sql.SET("app_id = " + Utils.addSqlStr(appid));
				}
			}
			sql.WHERE("id = " + Utils.addSqlStr(id));
			sqlStr = sql.toString() + ";";
			if (StringUtils.isBlank(id)) {
				sqlStr = "";
			}
		}
		return sqlStr;
	}

	/**
	 * @Title 查詢所有工作流
	 * 
	 * @return
	 */
	public String getFlowList() {
		String sqlStr = "";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("flow");
		sql.WHERE("enable_flag = 1");
		sqlStr = sql.toString() + ";";
		return sqlStr;
	}

	/**
	 * @Title 根據工作流Id查詢工作流
	 * 
	 * @param id
	 * @return
	 */
	public String getFlowById(String id) {
		String sqlStr = "";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("flow");
		sql.WHERE("id = " + Utils.addSqlStr(id));
		sql.WHERE("enable_flag = 1");
		sqlStr = sql.toString() + ";";
		return sqlStr;
	}

}
