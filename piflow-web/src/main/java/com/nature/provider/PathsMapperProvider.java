package com.nature.provider;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Paths;

public class PathsMapperProvider {

	/**
	 * @Title 插入list<Paths> 注意拼sql的方法必须用map接 Param内容为键值
	 * 
	 * @param map (内容： 键为pathsList,值为List<Paths>)
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String addPathsList(Map map) {
		List<Paths> pathsList = (List<Paths>) map.get("pathsList");
		StringBuffer sql = new StringBuffer();
		if (null != pathsList && pathsList.size() > 0) {
			sql.append("insert into ");
			sql.append("flow_path ");
			sql.append("(");
			sql.append("id,");
			sql.append("crt_dttm,");
			sql.append("crt_user,");
			sql.append("last_update_dttm,");
			sql.append("last_update_user,");
			sql.append("version,");
			sql.append("enable_flag,");
			sql.append("line_from,");
			sql.append("line_to,");
			sql.append("line_outport,");
			sql.append("line_inport,");
			sql.append("line_port,");
			sql.append("fk_flow_id");
			sql.append(") ");
			sql.append("values");
			int i = 0;
			for (Paths paths : pathsList) {
				i++;
				String id = paths.getId();
				Date crtDttm = paths.getCrtDttm();
				String crtUser = paths.getCrtUser();
				Date lastUpdateDttm = paths.getLastUpdateDttm();
				String lastUpdateUser = paths.getLastUpdateUser();
				Long version = paths.getVersion();
				Boolean enableFlag = paths.getEnableFlag();
				String from = paths.getFrom();
				String to = paths.getTo();
				String outport = paths.getOutport();
				String inport = paths.getInport();
				String port = paths.getPort();
				Flow flow = paths.getFlow();

				sql.append("(");
				sql.append(Utils.addSqlStr((id == null ? "" : id)) + ",");
				sql.append(Utils.addSqlStr((crtDttm == null ? "" : DateUtils.dateTimesToStr(crtDttm))) + ",");
				sql.append(Utils.addSqlStr((crtUser == null ? "" : crtUser)) + ",");
				sql.append(Utils.addSqlStr((lastUpdateDttm == null ? "" : DateUtils.dateTimesToStr(lastUpdateDttm)))
						+ ",");
				sql.append(Utils.addSqlStr((lastUpdateUser == null ? "" : lastUpdateUser)) + ",");
				sql.append((version == null ? "" : 0) + ",");
				sql.append((enableFlag == null ? "" : (enableFlag ? 1 : 0)) + ",");
				sql.append(Utils.addSqlStr((from == null ? "" : from)) + ",");
				sql.append(Utils.addSqlStr((to == null ? "" : to)) + ",");
				sql.append(Utils.addSqlStr((outport == null ? "" : outport)) + ",");
				sql.append(Utils.addSqlStr((inport == null ? "" : inport)) + ",");
				sql.append(Utils.addSqlStr((port == null ? "" : port)) + ",");
				sql.append(Utils.addSqlStr((flow == null ? "" : flow.getId())));
				if (i != pathsList.size()) {
					sql.append("),");
				} else {
					sql.append(")");
				}
			}
			sql.append(";");
		}
		return sql.toString();
	}

	/**
	 * @Title 新增paths
	 * @param paths
	 * @return
	 */
	public String addPaths(Paths paths) {
		String sqlStr = "";
		if (null != paths) {
			String id = paths.getId();
			Date crtDttm = paths.getCrtDttm();
			String crtUser = paths.getCrtUser();
			Date lastUpdateDttm = paths.getLastUpdateDttm();
			String lastUpdateUser = paths.getLastUpdateUser();
			Long version = paths.getVersion();
			Boolean enableFlag = paths.getEnableFlag();
			String from = paths.getFrom();
			String to = paths.getTo();
			String outport = paths.getOutport();
			String inport = paths.getInport();
			String port = paths.getPort();
			Flow flow = paths.getFlow();
			SQL sql = new SQL();
			sql.INSERT_INTO("flow_path");
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
			if (StringUtils.isNotBlank(from)) {
				sql.VALUES("line_from", Utils.addSqlStr(from));
			}
			if (StringUtils.isNotBlank(to)) {
				sql.VALUES("line_to", Utils.addSqlStr(to));
			}
			if (StringUtils.isNotBlank(outport)) {
				sql.VALUES("line_outport", Utils.addSqlStr(outport));
			}
			if (StringUtils.isNotBlank(inport)) {
				sql.VALUES("line_inport", Utils.addSqlStr(inport));
			}
			if (StringUtils.isNotBlank(port)) {
				sql.VALUES("line_port", Utils.addSqlStr(port));
			}
			if (null != flow) {
				String flowId = flow.getId();
				if (StringUtils.isNotBlank(flowId)) {
					sql.VALUES("fk_flow_id", Utils.addSqlStr(flowId));
				}
			}
			sqlStr = sql.toString() + ";";

		}
		return sqlStr;
	}

	/**
	 * @Title 修改paths
	 * 
	 * @param paths
	 * @return
	 */
	public String updatePaths(Paths paths) {
		String sqlStr = "";
		if (null != paths) {
			String id = paths.getId();
			Date lastUpdateDttm = paths.getLastUpdateDttm();
			String lastUpdateUser = paths.getLastUpdateUser();
			Long version = paths.getVersion();
			Boolean enableFlag = paths.getEnableFlag();
			String from = paths.getFrom();
			String to = paths.getTo();
			String outport = paths.getOutport();
			String inport = paths.getInport();
			String port = paths.getPort();
			SQL sql = new SQL();
			sql.UPDATE("flow_path");
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
			if (StringUtils.isNotBlank(from)) {
				sql.SET("line_from = " + Utils.addSqlStr(from));
			}
			if (StringUtils.isNotBlank(to)) {
				sql.SET("line_to = " + Utils.addSqlStr(to));
			}
			if (StringUtils.isNotBlank(outport)) {
				sql.SET("line_outport = " + Utils.addSqlStr(outport));
			}
			if (StringUtils.isNotBlank(inport)) {
				sql.SET("line_inport = " + Utils.addSqlStr(inport));
			}
			if (StringUtils.isNotBlank(port)) {
				sql.SET("line_port = " + Utils.addSqlStr(port));
			}
			sql.WHERE("id = " + Utils.addSqlStr(id));
			sqlStr = sql.toString() + ";";

		}
		return sqlStr;
	}

	/**
	 * @Title 根据flowId查询
	 * @param flowId
	 * @return
	 */
	public String getPathsListByFlowId(String flowId) {
		String sqlStr = "";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("flow_path");
		sql.WHERE("enable_flag = 1");
		sql.WHERE("fk_flow_id = " + Utils.addSqlStr(flowId));
		sqlStr = sql.toString() + ";";
		return sqlStr;
	}

}
