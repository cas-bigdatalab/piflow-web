package com.nature.provider;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.FlowInfoDb;

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
	
	/**
	 * @Title 修改FlowInfo
	 * 
	 * @param flow
	 * @return
	 */
	public String updateFlowInfo(FlowInfoDb flow) {

		String sqlStr = "";
		if (null != flow) {
			String id = flow.getId();
			String lastUpdateUser = flow.getLastUpdateUser();
			Date lastUpdateDttm = flow.getLastUpdateDttm();
			Long version = flow.getVersion();
			Boolean enableFlag = flow.getEnableFlag();
			String name = flow.getName();
			String state = flow.getState();
			String progress = flow.getProgress();
			Date endTime = flow.getEndTime();
			Date startTime = flow.getStartTime();
			SQL sql = new SQL();
			// INSERT_INTO括号中为数据库表名
			sql.UPDATE("flow_info");
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
			if (StringUtils.isNotBlank(state)) {
				sql.SET("state = " + Utils.addSqlStr(state));
			}
			if (StringUtils.isNotBlank(name)) {
				sql.SET("NAME = " + Utils.addSqlStr(name));
			}
			if (StringUtils.isNotBlank(progress)) {
				sql.SET("progress = " + Utils.addSqlStr(progress));
			}
			if (null != endTime) {
				String endTimeStr = DateUtils.dateTimesToStr(endTime);
				if (StringUtils.isNotBlank(endTimeStr)) {
					sql.SET("end_time = " + Utils.addSqlStr(endTimeStr));
				}
			}
			if (null != startTime) {
				String startTimeStr = DateUtils.dateTimesToStr(startTime);
				if (StringUtils.isNotBlank(startTimeStr)) {
					sql.SET("start_time = " + Utils.addSqlStr(startTimeStr));
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

}
