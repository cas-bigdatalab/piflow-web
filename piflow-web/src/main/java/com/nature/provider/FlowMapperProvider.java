package com.nature.provider;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.workFlow.model.Flow;

public class FlowMapperProvider {

	public String addFlow(Flow flow) {

		String sql = "";
		if (null != flow) {
			String id = flow.getId();
			String crtUser = flow.getCrtUser();
			Date crtDttm = flow.getCrtDttm();
			String lastUpdateUser = flow.getLastUpdateUser();
			Date lastUpdateDttm = flow.getLastUpdateDttm();
			Long version = flow.getVersion();
			Boolean enableFlag = flow.getEnableFlag();
			String appId = flow.getAppId();
			String name = flow.getName();
			String uuid = flow.getUuid();
			MxGraphModel mxGraphModel = flow.getMxGraphModel();
			sql = new SQL() {
				{
					// INSERT_INTO括号中为数据库表名
					INSERT_INTO("flow");
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
					if (null != enableFlag) {
						int enableFlagInt = enableFlag ? 1 : 0;
						VALUES("ENABLE_FLAG", enableFlagInt + "");
					}
					if (StringUtils.isNotBlank(appId)) {
						VALUES("APP_ID", Utils.addSqlStr(appId));
					}
					if (StringUtils.isNotBlank(name)) {
						VALUES("NAME", Utils.addSqlStr(name));
					}
					if (StringUtils.isNotBlank(uuid)) {
						VALUES("UUID", Utils.addSqlStr(uuid));
					}
					if (null != mxGraphModel) {
						String mxGraphModelId = mxGraphModel.getId();
						if (StringUtils.isNotBlank(mxGraphModelId)) {
							VALUES("MX_GRAPH_MODEL_ID", Utils.addSqlStr(mxGraphModelId));
						}
					}
				}
			}.toString() + ";";
		}
		return sql;
	};

}
