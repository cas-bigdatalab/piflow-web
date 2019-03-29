package com.nature.provider.flow;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class PropertyMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String displayName;
    private String description;
    private String customValue;
    private String allowableValues;
    private Integer required;
    private Integer sensitive;
    private String stopsId;
    private Integer isSelect;

    private void preventSQLInjectionProperty(Property property) {
        if (null != property && StringUtils.isNotBlank(property.getLastUpdateUser())) {
            // Mandatory Field
            String id = property.getId();
            String crtUser = property.getCrtUser();
            String lastUpdateUser = property.getLastUpdateUser();
            Boolean enableFlag = property.getEnableFlag();
            Long version = property.getVersion();
            Date crtDttm = property.getCrtDttm();
            Date lastUpdateDttm = property.getLastUpdateDttm();
            this.id = SqlUtils.preventSQLInjection(id);
            this.crtUser = (null != crtUser ? SqlUtils.preventSQLInjection(crtUser) : null);
            this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
            this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
            this.version = (null != version ? version : 0L);
            String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
            String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
            this.crtDttmStr = (null != crtDttm ? SqlUtils.preventSQLInjection(crtDttmStr) : null);
            this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

            // Selection field
            this.name = SqlUtils.preventSQLInjection(property.getName());
            this.displayName = SqlUtils.preventSQLInjection(property.getDisplayName());
            this.description = SqlUtils.preventSQLInjection(property.getDescription());
            this.customValue = SqlUtils.preventSQLInjection(property.getCustomValue());
            this.allowableValues = SqlUtils.preventSQLInjection(property.getAllowableValues());
            this.required = (null == property.getRequired() ? null : (property.getRequired() ? 1 : 0));
            this.sensitive = (null == property.getSensitive() ? null : (property.getSensitive() ? 1 : 0));
            String stopsIdStr = (null != property.getStops() ? property.getStops().getId() : null);
            this.stopsId = (null != stopsIdStr ? SqlUtils.preventSQLInjection(stopsIdStr) : null);
            this.isSelect = (null == property.getIsSelect() ? null : (property.getIsSelect() ? 1 : 0));
        }
    }

    private void reset() {
        this.id = null;
        this.crtUser = null;
        this.crtDttmStr = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.name = null;
        this.displayName = null;
        this.description = null;
        this.customValue = null;
        this.allowableValues = null;
        this.required = null;
        this.sensitive = null;
        this.stopsId = null;
        this.isSelect = null;
    }

    /**
     * 插入list<Property> 注意拼sql的方法必须用map接 Param内容为键值
     *
     * @param map (内容： 键为propertyList,值为List<Property>)
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
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
            sqlStrBuffer.append("fk_stops_id,");
            sqlStrBuffer.append("is_select");
            sqlStrBuffer.append(") ");
            sqlStrBuffer.append("values");
            int i = 0;
            for (Property property : propertyList) {
                i++;
                this.preventSQLInjectionProperty(property);
                if (null == crtDttmStr) {
                    String crtDttm = DateUtils.dateTimesToStr(new Date());
                    crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
                }
                if (StringUtils.isBlank(crtUser)) {
                    crtUser = SqlUtils.preventSQLInjection("-1");
                }
                // 拼接时位置顺序不能错
                sqlStrBuffer.append("(");
                sqlStrBuffer.append(id + ",");
                sqlStrBuffer.append(crtDttmStr + ",");
                sqlStrBuffer.append(crtUser + ",");
                sqlStrBuffer.append(lastUpdateDttmStr + ",");
                sqlStrBuffer.append(lastUpdateUser + ",");
                sqlStrBuffer.append(version + ",");
                sqlStrBuffer.append(enableFlag + ",");
                sqlStrBuffer.append(name + ",");
                sqlStrBuffer.append(displayName + ",");
                sqlStrBuffer.append(description + ",");
                sqlStrBuffer.append(customValue + ",");
                sqlStrBuffer.append(allowableValues + ",");
                sqlStrBuffer.append(required + ",");
                sqlStrBuffer.append(sensitive + ",");
                sqlStrBuffer.append(stopsId + ",");
                sqlStrBuffer.append(isSelect);
                if (i != propertyList.size()) {
                    sqlStrBuffer.append("),");
                } else {
                    sqlStrBuffer.append(")");
                }
                this.reset();
            }
            sqlStrBuffer.append(";");
        }
        String sqlStr = sqlStrBuffer.toString();
        return sqlStr;
    }

    /**
     * 修改Property
     *
     * @param property
     * @return
     */
    public String updateStopsProperty(Property property) {
        String sqlStr = "";
        this.preventSQLInjectionProperty(property);
        if (null != property) {

            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.UPDATE("flow_stops_property");
            // SET中的第一个字符串为数据库中表对应的字段名

            sql.SET("LAST_UPDATE_DTTM = " + lastUpdateDttmStr);
            sql.SET("LAST_UPDATE_USER = " + lastUpdateUser);
            sql.SET("VERSION = " + (version + 1));

            // 处理其他字段
            sql.SET("ENABLE_FLAG = " +enableFlag);
            sql.SET("description = " + description);
            sql.SET("NAME = " + name);
            sql.SET("allowable_values = " + allowableValues);
            sql.SET("custom_value = " + customValue);
            sql.SET("display_name = " + displayName);
            sql.SET("property_required = " + required);
            sql.SET("property_sensitive = " + sensitive);
            sql.SET("fk_stops_id = " + stopsId);
            sql.WHERE("version = " + version);
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
            if (StringUtils.isBlank(id)) {
                sqlStr = "";
            }
        }
        this.reset();
        return sqlStr;
    }

    /**
     * 逻辑删除
     *
     * @param id
     * @return
     */
    public String updateEnableFlagByStopId(String id) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(id)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_stops_property");
            sql.SET("ENABLE_FLAG = 0");
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.WHERE("ENABLE_FLAG = 1");
            sql.WHERE("ID = " + SqlUtils.preventSQLInjection(id));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * 修改stop属性
     *
     * @param id
     * @return
     */
    public String updatePropertyCustomValue(String content, String id) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(id)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_stops_property");
            sql.SET("custom_value = " + SqlUtils.preventSQLInjection(content));
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.SET("version = " + 1);
            sql.WHERE("ENABLE_FLAG = 1");
            sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

}
