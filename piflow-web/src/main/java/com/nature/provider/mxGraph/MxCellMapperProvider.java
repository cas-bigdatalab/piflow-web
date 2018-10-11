package com.nature.provider.mxGraph;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;

public class MxCellMapperProvider {

	public String addMxCell(MxCell mxCell) {
		String sql = "";
		if (null != mxCell) {
			String id = mxCell.getId();
			String crtUser = mxCell.getCrtUser();
			Date crtDttm = mxCell.getCrtDttm();
			Boolean enableFlag = mxCell.getEnableFlag();
			Date lastUpdateDttm = mxCell.getLastUpdateDttm();
			String lastUpdateUser = mxCell.getLastUpdateUser();
			Long version = mxCell.getVersion();
			String pageId = mxCell.getPageId();
			String parent = mxCell.getParent();
			String style = mxCell.getStyle();
			String edge = mxCell.getEdge();
			String source = mxCell.getSource();
			String target = mxCell.getTarget();
			String value = mxCell.getValue();
			String vertex = mxCell.getVertex();
			MxGeometry mxGeometry = mxCell.getMxGeometry();
			MxGraphModel mxGraphModel = mxCell.getMxGraphModel();
			SQL NewSQL = new SQL();

			// INSERT_INTO括号中为数据库表名
			NewSQL.INSERT_INTO("mx_cell");
			// value中的第一个字符串为数据库中表对应的字段名
			// 除数字类型的字段外其他类型必须加单引号
			if (StringUtils.isNotBlank(id)) {
				NewSQL.VALUES("ID", Utils.addSqlStr(id));
			}
			if (null != crtDttm) {
				String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
				if (StringUtils.isNotBlank(crtDttmStr)) {
					NewSQL.VALUES("CRT_DTTM", Utils.addSqlStr(crtDttmStr));
				}
			}
			if (StringUtils.isNotBlank(crtUser)) {
				NewSQL.VALUES("CRT_USER", Utils.addSqlStr(crtUser));
			}
			if (null != enableFlag) {
				int enableFlagInt = enableFlag ? 1 : 0;
				NewSQL.VALUES("ENABLE_FLAG", enableFlagInt + "");
			}
			if (null != lastUpdateDttm) {
				String lastUpdateDttmStr = DateUtils.dateTimesToStr(lastUpdateDttm);
				if (StringUtils.isNotBlank(lastUpdateDttmStr)) {
					NewSQL.VALUES("LAST_UPDATE_DTTM", Utils.addSqlStr(lastUpdateDttmStr));
				}
			}
			if (StringUtils.isNotBlank(lastUpdateUser)) {
				NewSQL.VALUES("LAST_UPDATE_USER", Utils.addSqlStr(lastUpdateUser));
			}
			if (null != version && StringUtils.isNotBlank(version.toString())) {
				NewSQL.VALUES("VERSION", version.toString());
			}
			if (StringUtils.isNotBlank(pageId)) {
				NewSQL.VALUES("MX_PAGEID", Utils.addSqlStr(pageId));
			}
			if (StringUtils.isNotBlank(parent)) {
				NewSQL.VALUES("MX_PARENT", Utils.addSqlStr(parent));
			}
			if (StringUtils.isNotBlank(style)) {
				NewSQL.VALUES("MX_STYLE", Utils.addSqlStr(style));
			}
			if (StringUtils.isNotBlank(edge)) {
				NewSQL.VALUES("MX_EDGE", Utils.addSqlStr(edge));
			}
			if (StringUtils.isNotBlank(source)) {
				NewSQL.VALUES("MX_SOURCE", Utils.addSqlStr(source));
			}
			if (StringUtils.isNotBlank(target)) {
				NewSQL.VALUES("MX_TARGET", Utils.addSqlStr(target));
			}
			if (StringUtils.isNotBlank(value)) {
				NewSQL.VALUES("MX_VALUE", Utils.addSqlStr(value));
			}
			if (StringUtils.isNotBlank(vertex)) {
				NewSQL.VALUES("MX_VERTEX", Utils.addSqlStr(vertex));
			}
			if (null != mxGeometry) {
				String mxGeometryId = mxGraphModel.getId();
				if (StringUtils.isNotBlank(mxGeometryId)) {
					NewSQL.VALUES("MX_GEOMETRY_ID", Utils.addSqlStr(mxGeometryId));
				}
			}
			if (null != mxGraphModel) {
				String mxGraphModelId = mxGraphModel.getId();
				if (StringUtils.isNotBlank(mxGraphModelId)) {
					NewSQL.VALUES("FK_MX_GRAPH_ID", Utils.addSqlStr(mxGraphModelId));
				}
			}

			sql = NewSQL.toString() + ";";
		}
		return sql;
	};

}
