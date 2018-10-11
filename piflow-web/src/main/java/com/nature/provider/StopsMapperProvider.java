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

	public String addStops(Stops stops) {
		String sql = "";
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
			sql = new SQL() {
				{
					INSERT_INTO("flow_stops");
					if (StringUtils.isNotBlank(id)) {
						VALUES("id", Utils.addSqlStr(id));
					}
					if (null != crtDttm) {
						String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
						if (StringUtils.isNotBlank(crtDttmStr)) {
							VALUES("crt_dttm", Utils.addSqlStr(crtDttmStr));
						}
					}
					if (StringUtils.isNotBlank(crtUser)) {
						VALUES("crt_user", Utils.addSqlStr(crtUser));
					}
					if (null != lastUpdateDttm) {
						String lastUpdateDttmStr = DateUtils.dateTimesToStr(lastUpdateDttm);
						if (StringUtils.isNotBlank(lastUpdateDttmStr)) {
							VALUES("last_update_dttm", Utils.addSqlStr(lastUpdateDttmStr));
						}
					}
					if (StringUtils.isNotBlank(lastUpdateUser)) {
						VALUES("last_update_user", Utils.addSqlStr(lastUpdateUser));
					}
					if (null != version && StringUtils.isNotBlank(version.toString())) {
						VALUES("version", version.toString());
					}
					if (null != enableFlag) {
						int enableFlagInt = enableFlag ? 1 : 0;
						VALUES("ENABLE_FLAG", enableFlagInt + "");
					}
					if (StringUtils.isNotBlank(bundel)) {
						VALUES("bundel", Utils.addSqlStr(bundel));
					}
					if (StringUtils.isNotBlank(bundel)) {
						VALUES("description", Utils.addSqlStr(description));
					}
					if (StringUtils.isNotBlank(bundel)) {
						VALUES("groups", Utils.addSqlStr(groups));
					}
					if (StringUtils.isNotBlank(bundel)) {
						VALUES("name", Utils.addSqlStr(name));
					}
					if (StringUtils.isNotBlank(bundel)) {
						VALUES("number_of_entrances", Utils.addSqlStr(numberOfEntrances));
					}
					if (StringUtils.isNotBlank(bundel)) {
						VALUES("number_of_exports", Utils.addSqlStr(numberOfExports));
					}
					if (StringUtils.isNotBlank(bundel)) {
						VALUES("owner", Utils.addSqlStr(owner));
					}
				}
			}.toString();
		}
		return sql;
	}

	/**
	 * custom sql 自定义sql
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

	public String getStopsAll() {
		String ss = new SQL() {
			{
				SELECT("*");
				FROM("flow_stops");
			}
		}.toString();
		return ss;
	}

}
