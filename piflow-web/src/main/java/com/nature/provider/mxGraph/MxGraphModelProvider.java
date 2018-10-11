package com.nature.provider.mxGraph;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.mxGraph.model.MxGraphModel;

public class MxGraphModelProvider {

	public String addMxGraphModel(MxGraphModel mxGraphModel) {
		String sql = "";
		if (null != mxGraphModel) {
			String id = mxGraphModel.getId();
			String crtUser = mxGraphModel.getCrtUser();
			Date crtDttm = mxGraphModel.getCrtDttm();
			Date lastUpdateDttm = mxGraphModel.getLastUpdateDttm();
			String lastUpdateUser = mxGraphModel.getLastUpdateUser();
			Boolean enableFlag = mxGraphModel.getEnableFlag();
			Long version = mxGraphModel.getVersion();
			String dx = mxGraphModel.getDx();
			String dy = mxGraphModel.getDy();
			String grid = mxGraphModel.getGrid();
			String gridSize = mxGraphModel.getGridSize();
			String guides = mxGraphModel.getGuides();
			String tooltips = mxGraphModel.getTooltips();
			String connect = mxGraphModel.getConnect();
			String arrows = mxGraphModel.getArrows();
			String fold = mxGraphModel.getFold();
			String page = mxGraphModel.getPage();
			String pageScale = mxGraphModel.getPageScale();
			String pageWidth = mxGraphModel.getPageWidth();
			String pageHeight = mxGraphModel.getPageHeight();
			String background = mxGraphModel.getBackground();
			sql = new SQL() {
				{
					// INSERT_INTO括号中为数据库表名
					INSERT_INTO("mx_graph_model");
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
					if (StringUtils.isNotBlank(dx)) {
						VALUES("MX_DX", Utils.addSqlStr(dx));
					}
					if (StringUtils.isNotBlank(dy)) {
						VALUES("MX_DY", Utils.addSqlStr(dy));
					}
					if (StringUtils.isNotBlank(grid)) {
						VALUES("MX_GRID", Utils.addSqlStr(grid));
					}
					if (StringUtils.isNotBlank(gridSize)) {
						VALUES("MX_GRIDSIZE", Utils.addSqlStr(gridSize));
					}
					if (StringUtils.isNotBlank(guides)) {
						VALUES("MX_GUIDES", Utils.addSqlStr(guides));
					}
					if (StringUtils.isNotBlank(tooltips)) {
						VALUES("MX_TOOLTIPS", Utils.addSqlStr(tooltips));
					}
					if (StringUtils.isNotBlank(connect)) {
						VALUES("MX_CONNECT", Utils.addSqlStr(connect));
					}
					if (StringUtils.isNotBlank(arrows)) {
						VALUES("MX_ARROWS", Utils.addSqlStr(arrows));
					}
					if (StringUtils.isNotBlank(fold)) {
						VALUES("MX_FOLD", Utils.addSqlStr(fold));
					}
					if (StringUtils.isNotBlank(page)) {
						VALUES("MX_PAGE", Utils.addSqlStr(page));
					}
					if (StringUtils.isNotBlank(pageScale)) {
						VALUES("MX_PAGESCALE", Utils.addSqlStr(pageScale));
					}
					if (StringUtils.isNotBlank(pageWidth)) {
						VALUES("MX_PAGEWIDTH", Utils.addSqlStr(pageWidth));
					}
					if (StringUtils.isNotBlank(pageHeight)) {
						VALUES("MX_PAGEHEIGHT", Utils.addSqlStr(pageHeight));
					}
					if (StringUtils.isNotBlank(background)) {
						VALUES("MX_BACKGROUND", Utils.addSqlStr(background));
					}
				}
			}.toString() + ";";
		}
		return sql;
	};

}
