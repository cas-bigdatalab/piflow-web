package com.nature.provider;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;

public class PropertyMapperProvider {

	/**
	 * custom sql 自定义sql
	 * 
	 * @param map (内容： 键为pathsList,值为List<Paths>)
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String addPropertyList(Map map) {
		List<Property> propertyList = (List<Property>) map.get("propertyList");
		StringBuffer sql = new StringBuffer();
		if (null != propertyList && propertyList.size() > 0) {
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
			sql.append("name,");
			sql.append("display_name,");
			sql.append("description,");
			sql.append("custom_value,");
			sql.append("allowable_values,");
			sql.append("property_required,");
			sql.append("property_sensitive,");
			sql.append("fk_stops_id");
			sql.append(") ");
			sql.append("values");
			int i = 0;
			for (Property property : propertyList) {
				i++;
				String id = property.getId();
				Date crtDttm = property.getCrtDttm();
				String crtUser = property.getCrtUser();
				Date lastUpdateDttm = property.getLastUpdateDttm();
				String lastUpdateUser = property.getLastUpdateUser();
				Long version = property.getVersion();
				Boolean enableFlag = property.getEnableFlag();
				String name = property.getName();
				String displayName = property.getDisplayName();
				String description = property.getDescription();
				String customValue = property.getCustomValue();
				String allowableValues = property.getAllowableValues();
				boolean required = property.isRequired();
				boolean sensitive = property.isSensitive();
				Stops stops = property.getStops();
				// 拼接时位置顺序不能错
				sql.append("(");
				sql.append(Utils.addSqlStr((id == null ? "" : id)) + ",");
				sql.append(Utils.addSqlStr((crtDttm == null ? "" : DateUtils.dateTimesToStr(crtDttm))) + ",");
				sql.append(Utils.addSqlStr((crtUser == null ? "" : crtUser)) + ",");
				sql.append(Utils.addSqlStr((lastUpdateDttm == null ? "" : DateUtils.dateTimesToStr(lastUpdateDttm)))
						+ ",");
				sql.append(Utils.addSqlStr((lastUpdateUser == null ? "" : lastUpdateUser)) + ",");
				sql.append((version == null ? "" : 0) + ",");
				sql.append((enableFlag == null ? "" : (enableFlag ? 1 : 0)) + ",");
				sql.append(Utils.addSqlStr((name == null ? "" : name)) + ",");
				sql.append(Utils.addSqlStr((displayName == null ? "" : displayName)) + ",");
				sql.append(Utils.addSqlStr((description == null ? "" : description)) + ",");
				sql.append(Utils.addSqlStr((customValue == null ? "" : customValue)) + ",");
				sql.append(Utils.addSqlStr((allowableValues == null ? "" : allowableValues)) + ",");
				sql.append((required ? 1 : 0) + ",");
				sql.append((sensitive ? 1 : 0) + ",");
				sql.append(Utils.addSqlStr((stops == null ? "" : stops.getId())));
				if (i != propertyList.size()) {
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
