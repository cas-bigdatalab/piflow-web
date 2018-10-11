package com.nature.provider;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Paths;

public class PathsMapperProvider {

	/**
	 * custom sql 自定义sql
	 * 
	 * @param map (内容： 键为pathsList,值为List<Paths>)
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String addStopsList(Map map) {
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
	};

}
