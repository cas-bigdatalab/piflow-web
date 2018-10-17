package com.nature.provider;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.Stops;

public class StopsMapperProvider {
	/**
	 * @Title 新增Stops
	 * @param stops
	 * @return
	 */
	public String addStops(Stops stops) {
		String sqlStr = "";
		if (null != stops) {
			String id = stops.getId();
			Date crtDttm = stops.getCrtDttm();
			String crtUser = stops.getCrtUser();
			Date lastUpdateDttm = stops.getLastUpdateDttm();
			String lastUpdateUser = stops.getLastUpdateUser();
			Long version = stops.getVersion();
			Boolean enableFlag = stops.getEnableFlag();
			String bundel = stops.getBundel();
			String description = stops.getDescription();
			String groups = stops.getGroups();
			String name = stops.getName();
			String numberOfEntrances = stops.getNumberOfEntrances();
			String numberOfExports = stops.getNumberOfExports();
			String owner = stops.getOwner();
			SQL sql = new SQL();

			sql.INSERT_INTO("flow_stops");
			if (StringUtils.isNotBlank(id)) {
				sql.VALUES("id", Utils.addSqlStr(id));
			}
			if (null != crtDttm) {
				String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
				if (StringUtils.isNotBlank(crtDttmStr)) {
					sql.VALUES("crt_dttm", Utils.addSqlStr(crtDttmStr));
				}
			}
			if (StringUtils.isNotBlank(crtUser)) {
				sql.VALUES("crt_user", Utils.addSqlStr(crtUser));
			}
			if (null != lastUpdateDttm) {
				String lastUpdateDttmStr = DateUtils.dateTimesToStr(lastUpdateDttm);
				if (StringUtils.isNotBlank(lastUpdateDttmStr)) {
					sql.VALUES("last_update_dttm", Utils.addSqlStr(lastUpdateDttmStr));
				}
			}
			if (StringUtils.isNotBlank(lastUpdateUser)) {
				sql.VALUES("last_update_user", Utils.addSqlStr(lastUpdateUser));
			}
			if (null != version && StringUtils.isNotBlank(version.toString())) {
				sql.VALUES("version", version.toString());
			}
			if (null != enableFlag) {
				int enableFlagInt = enableFlag ? 1 : 0;
				sql.VALUES("ENABLE_FLAG", enableFlagInt + "");
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.VALUES("bundel", Utils.addSqlStr(bundel));
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.VALUES("description", Utils.addSqlStr(description));
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.VALUES("groups", Utils.addSqlStr(groups));
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.VALUES("name", Utils.addSqlStr(name));
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.VALUES("number_of_entrances", Utils.addSqlStr(numberOfEntrances));
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.VALUES("number_of_exports", Utils.addSqlStr(numberOfExports));
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.VALUES("owner", Utils.addSqlStr(owner));
			}

			sqlStr = sql.toString() + ";";
		}
		return sqlStr;
	}

	/**
	 * @Title 插入list<Stops> 注意拼sql的方法必须用map接 Param内容为键值
	 * 
	 * @param map (内容： 键为stopsList,值为List<Stops>)
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String addStopsList(Map map) {
		List<Stops> stopsList = (List<Stops>) map.get("stopsList");
		StringBuffer sql = new StringBuffer();
		if (null != stopsList && stopsList.size() > 0) {
			sql.append("insert into ");
			sql.append("flow_stops ");
			sql.append("(");
			sql.append("id,");
			sql.append("crt_dttm,");
			sql.append("crt_user,");
			sql.append("last_update_dttm,");
			sql.append("last_update_user,");
			sql.append("version,");
			sql.append("enable_flag,");
			sql.append("bundel,");
			sql.append("description,");
			sql.append("groups,");
			sql.append("name,");
			sql.append("number_of_entrances,");
			sql.append("number_of_exports,");
			sql.append("owner");
			sql.append(") ");
			sql.append("values");
			int i = 0;
			for (Stops stops : stopsList) {
				i++;
				String id = stops.getId();
				Date crtDttm = stops.getCrtDttm();
				String crtUser = stops.getCrtUser();
				Date lastUpdateDttm = stops.getLastUpdateDttm();
				String lastUpdateUser = stops.getLastUpdateUser();
				Long version = stops.getVersion();
				Boolean enableFlag = stops.getEnableFlag();
				String bundel = stops.getBundel();
				String description = stops.getDescription();
				String groups = stops.getGroups();
				String name = stops.getName();
				String numberOfEntrances = stops.getNumberOfEntrances();
				String numberOfExports = stops.getNumberOfExports();
				String owner = stops.getOwner();
				if (null == crtUser) {
					crtUser = "";
				}
				sql.append("(");
				sql.append(Utils.addSqlStr((id == null ? "" : id)) + ",");
				sql.append(Utils.addSqlStr((crtDttm == null ? "" : DateUtils.dateTimesToStr(crtDttm))) + ",");
				sql.append(Utils.addSqlStr((crtUser == null ? "" : crtUser)) + ",");
				sql.append(Utils.addSqlStr((lastUpdateDttm == null ? "" : DateUtils.dateTimesToStr(lastUpdateDttm)))
						+ ",");
				sql.append(Utils.addSqlStr((lastUpdateUser == null ? "" : lastUpdateUser)) + ",");
				sql.append((version == null ? "" : 0) + ",");
				sql.append((enableFlag == null ? "" : (enableFlag ? 1 : 0)) + ",");
				sql.append(Utils.addSqlStr((bundel == null ? "" : bundel)) + ",");
				sql.append(Utils.addSqlStr((description == null ? "" : description)) + ",");
				sql.append(Utils.addSqlStr((groups == null ? "" : groups)) + ",");
				sql.append(Utils.addSqlStr((name == null ? "" : name)) + ",");
				sql.append(Utils.addSqlStr((numberOfEntrances == null ? "" : numberOfEntrances)) + ",");
				sql.append(Utils.addSqlStr((numberOfExports == null ? "" : numberOfExports)) + ",");
				sql.append(Utils.addSqlStr((owner == null ? "" : owner)));
				if (i != stopsList.size()) {
					sql.append("),");
				} else {
					sql.append(")");
				}
			}
			sql.append(";");
		}
		return sql.toString();
	};

	/**
	 * @Title 修改stops
	 * @param stops
	 * @return
	 */
	public String updateStops(Stops stops) {
		String sqlStr = "";
		if (null != stops) {
			String id = stops.getId();
			Date lastUpdateDttm = stops.getLastUpdateDttm();
			String lastUpdateUser = stops.getLastUpdateUser();
			Long version = stops.getVersion();
			Boolean enableFlag = stops.getEnableFlag();
			String bundel = stops.getBundel();
			String description = stops.getDescription();
			String groups = stops.getGroups();
			String name = stops.getName();
			String numberOfEntrances = stops.getNumberOfEntrances();
			String numberOfExports = stops.getNumberOfExports();
			String owner = stops.getOwner();
			SQL sql = new SQL();

			sql.UPDATE("flow_stops");
			if (null != lastUpdateDttm) {
				String lastUpdateDttmStr = DateUtils.dateTimesToStr(lastUpdateDttm);
				if (StringUtils.isNotBlank(lastUpdateDttmStr)) {
					sql.SET("last_update_dttm = " + Utils.addSqlStr(lastUpdateDttmStr));
				}
			}
			if (StringUtils.isNotBlank(lastUpdateUser)) {
				sql.SET("last_update_user = " + Utils.addSqlStr(lastUpdateUser));
			}
			if (null != version && StringUtils.isNotBlank(version.toString())) {
				sql.SET("version = " + version.toString());
			}
			if (null != enableFlag) {
				int enableFlagInt = enableFlag ? 1 : 0;
				sql.SET("ENABLE_FLAG = " + enableFlagInt);
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.SET("bundel = " + Utils.addSqlStr(bundel));
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.SET("description = " + Utils.addSqlStr(description));
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.SET("groups = " + Utils.addSqlStr(groups));
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.SET("name = " + Utils.addSqlStr(name));
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.SET("number_of_entrances = " + Utils.addSqlStr(numberOfEntrances));
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.SET("number_of_exports = " + Utils.addSqlStr(numberOfExports));
			}
			if (StringUtils.isNotBlank(bundel)) {
				sql.SET("owner = " + Utils.addSqlStr(owner));
			}
			sql.WHERE("id = " + id);
			sqlStr = sql.toString() + ";";
			if (StringUtils.isBlank(id)) {
				sqlStr = "";
			}
		}
		return sqlStr;
	}

	/**
	 * @Title 查询所有的stops数据
	 * 
	 * @return
	 */
	public String getStopsAll() {
		String sqlStr = "";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("flow_stops");
		sql.WHERE("enable_flag = 1");
		sqlStr = sql.toString() + ";";
		return sqlStr;
	}

	/**
	 * @Title 根据flowId查询StopsList
	 * @param flowId
	 * @return
	 */
	public String getStopsListByFlowId(String flowId) {
		String sqlStr = "";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("flow_stops");
		sql.WHERE("enable_flag = 1");
		sql.WHERE("fk_flow_id = " + Utils.addSqlStr(flowId));
		sqlStr = sql.toString() + ";";
		return sqlStr;
	}
}
