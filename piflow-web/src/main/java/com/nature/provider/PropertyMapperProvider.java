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
	 * @Title 插入list<Property> 注意拼sql的方法必须用map接 Param内容为键值
	 * 
	 * @param map (内容： 键为propertyList,值为List<Property>)
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String addPropertyList(Map map) {
		List<Property> propertyList = (List<Property>) map.get("propertyList");
        StringBuffer sqlStrBuffer = new StringBuffer();
        if (null != propertyList && propertyList.size() > 0) {
            sqlStrBuffer.append("insert into ");
            sqlStrBuffer.append("flow_stops_property ");
            sqlStrBuffer.append("(");
            sqlStrBuffer.append("id,");
            sqlStrBuffer.append("crt_dttm,");
            sqlStrBuffer.append("crt_user,");
            sqlStrBuffer.append("last_update_dttm,");
            sqlStrBuffer.append("last_update_user,");
            sqlStrBuffer.append("version,");
            sqlStrBuffer.append("enable_flag,");
            sqlStrBuffer.append("name,");
            sqlStrBuffer.append("display_name,");
            sqlStrBuffer.append("description,");
            sqlStrBuffer.append("custom_value,");
            sqlStrBuffer.append("allowable_values,");
            sqlStrBuffer.append("property_required,");
            sqlStrBuffer.append("property_sensitive,");
            sqlStrBuffer.append("fk_stops_id");
            sqlStrBuffer.append(") ");
            sqlStrBuffer.append("values");
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
                sqlStrBuffer.append("(");
                sqlStrBuffer.append(Utils.addSqlStr(Utils.replaceString(id)) + ",");
                sqlStrBuffer.append(Utils.addSqlStr((crtDttm == null ? "" : DateUtils.dateTimesToStr(crtDttm))) + ",");
                sqlStrBuffer.append(Utils.addSqlStr((Utils.replaceString(crtUser))) + ",");
                sqlStrBuffer.append(Utils.addSqlStr((lastUpdateDttm == null ? "" : DateUtils.dateTimesToStr(lastUpdateDttm))) + ",");
                sqlStrBuffer.append(Utils.addSqlStr((Utils.replaceString(lastUpdateUser))) + ",");
                sqlStrBuffer.append((version == null ? "" : 0) + ",");
                sqlStrBuffer.append((enableFlag == null ? "" : (enableFlag ? 1 : 0)) + ",");
                sqlStrBuffer.append(Utils.addSqlStr((Utils.replaceString(name))) + ",");
                sqlStrBuffer.append(Utils.addSqlStr((Utils.replaceString(displayName))) + ",");
                sqlStrBuffer.append(Utils.addSqlStr((Utils.replaceString(description))) + ",");
                sqlStrBuffer.append(Utils.addSqlStr((Utils.replaceString(customValue))) + ",");
                sqlStrBuffer.append(Utils.addSqlStr((Utils.replaceString(allowableValues))) + ",");
                sqlStrBuffer.append((required ? 1 : 0) + ",");
                sqlStrBuffer.append((sensitive ? 1 : 0) + ",");
                sqlStrBuffer.append(Utils.addSqlStr((stops == null ? "" : stops.getId())));
                if (i != propertyList.size()) {
                    sqlStrBuffer.append("),");
                } else {
                    sqlStrBuffer.append(")");
                }
            }
            sqlStrBuffer.append(";");
        }
		String sqlStr = sqlStrBuffer.toString();
		return sqlStr;
	}
}
