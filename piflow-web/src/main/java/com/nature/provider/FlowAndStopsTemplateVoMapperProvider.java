package com.nature.provider;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SqlUtils;
import com.nature.common.Eunm.PortType;
import com.nature.component.flow.model.Template;
import com.nature.component.template.model.FlowTemplateModel;
import com.nature.component.template.model.PropertyTemplateModel;
import com.nature.component.template.model.StopTemplateModel;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class FlowAndStopsTemplateVoMapperProvider {

    /**
     * 插入list<Property> 注意拼sql的方法必须用map接 Param内容为键值
     *
     * @param map (内容： 键为propertyList,值为List<Property>)
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String addPropertyList(Map map) {
        List<PropertyTemplateModel> propertyList = (List<PropertyTemplateModel>) map.get("propertyList");
        StringBuffer sqlStrBuffer = new StringBuffer();
        if (null != propertyList && propertyList.size() > 0) {
            sqlStrBuffer.append("insert into ");
            sqlStrBuffer.append("property_template ");
            sqlStrBuffer.append("(");
            sqlStrBuffer.append("id,");
            sqlStrBuffer.append("crt_dttm,");
            sqlStrBuffer.append("enable_flag,");
            sqlStrBuffer.append("name,");
            sqlStrBuffer.append("display_name,");
            sqlStrBuffer.append("description,");
            sqlStrBuffer.append("custom_value,");
            sqlStrBuffer.append("allowable_values,");
            sqlStrBuffer.append("property_required,");
            sqlStrBuffer.append("property_sensitive,");
            sqlStrBuffer.append("version,");
            sqlStrBuffer.append("fk_stops_id,");
            sqlStrBuffer.append("is_select,");
            sqlStrBuffer.append("crt_user");
            sqlStrBuffer.append(") ");
            sqlStrBuffer.append("values");
            int i = 0;
            for (PropertyTemplateModel property : propertyList) {
                i++;
                String id = property.getId();
                Date crtDttm = property.getCrtDttm();
                Boolean enableFlag = property.getEnableFlag();
                String name = property.getName();
                String displayName = property.getDisplayName();
                String description = property.getDescription().equals("null") ? null : property.getDisplayName();
                String customValue = property.getCustomValue();
                String allowableValues = property.getAllowableValues();
                Boolean required = property.getRequired();
                Boolean sensitive = property.getSensitive();
                Long version = property.getVersion();
                StopTemplateModel stops = property.getStopsVo();
                Boolean isSelect = property.getIsSelect();
                String crtUser = property.getCrtUser();
                // 拼接时位置顺序不能错
                sqlStrBuffer.append("(");
                sqlStrBuffer.append(SqlUtils.addSqlStr(SqlUtils.replaceString(id)) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStr((crtDttm == null ? "" : DateUtils.dateTimesToStr(crtDttm))) + ",");
                sqlStrBuffer.append((enableFlag == null ? "1" : (enableFlag ? 1 : 0)) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStr((SqlUtils.replaceString(name))) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStr((SqlUtils.replaceString(displayName))) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStr((SqlUtils.replaceString(description))) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStr((SqlUtils.replaceString(customValue))) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStr((SqlUtils.replaceString(allowableValues))) + ",");
                sqlStrBuffer.append((required ? 1 : 0) + ",");
                sqlStrBuffer.append((sensitive ? 1 : 0) + ",");
                sqlStrBuffer.append((version == null ? "0" : version) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStr((stops == null ? "" : stops.getId())) + "," );
                sqlStrBuffer.append((isSelect == null ? 0 : isSelect) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStr((SqlUtils.replaceString(crtUser))));
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

    /**
     * 新增FlowTemplateVo
     *
     * @param flow
     * @return
     */
    public String addFlow(FlowTemplateModel flow) {
        String sqlStr = "";
        if (null != flow) {
            String id = flow.getId();
            String description = flow.getDescription();
            String name = flow.getName();
            SQL sql = new SQL();
            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("flow_template");
            // value中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            sql.VALUES("ID", SqlUtils.addSqlStr(id));
            if (StringUtils.isNotBlank(description)) {
                sql.VALUES("description", SqlUtils.addSqlStr(description));
            }
            if (StringUtils.isNotBlank(name)) {
                sql.VALUES("NAME", SqlUtils.addSqlStr(name));
            }
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * 新增StopTemplateVo
     *
     * @param stops
     * @return
     */
    public String addStops(StopTemplateModel stops) {
        String sqlStr = "";
        if (null != stops) {
            String id = stops.getId();
            String bundel = stops.getBundel();
            String description = stops.getDescription().equals("null") ? null : stops.getDescription();
            String name = stops.getName();
            String inports = stops.getInports();
            PortType inPortType = stops.getInPortType();
            String outports = stops.getOutports();
            PortType outPortType = stops.getOutPortType();
            String owner = stops.getOwner();
            String pageId = stops.getPageId();
            Template flow = stops.getTemplate();
            Boolean enableFlag = stops.getEnableFlag();
            Date crtDttm = stops.getCrtDttm();
            Long version = stops.getVersion();
            Boolean checkpoint = stops.getIsCheckpoint();
            String groups = stops.getGroups();
            String crtUser = stops.getCrtUser();
            SQL sql = new SQL();
            sql.INSERT_INTO("stops_template");

            //先处理修改必填字段
            if (null == crtDttm) {
                crtDttm = new Date();
            }
            if (null == version) {
                version = 0L;
            }
            if (null == enableFlag) {
                enableFlag = true;
            }
            sql.VALUES("id", SqlUtils.addSqlStr(id));
            String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
            sql.VALUES("crt_dttm", SqlUtils.addSqlStr(crtDttmStr));
            sql.VALUES("version", (version + 1) + "");
            int enableFlagInt = enableFlag ? 1 : 0;
            sql.VALUES("ENABLE_FLAG", enableFlagInt + "");

            // 处理其他字段
            if (StringUtils.isNotBlank(bundel)) {
                sql.VALUES("bundel", SqlUtils.addSqlStr(bundel));
            }
            if (StringUtils.isNotBlank(description)) {
                sql.VALUES("description", SqlUtils.addSqlStr(description));
            }
            if (StringUtils.isNotBlank(name)) {
                sql.VALUES("name", SqlUtils.addSqlStr(name));
            }
            if (StringUtils.isNotBlank(inports)) {
                sql.VALUES("inports", SqlUtils.addSqlStr(inports));
            }
            if (null != inPortType) {
                sql.VALUES("in_port_type", SqlUtils.addSqlStr(inPortType.name()));
            }
            if (StringUtils.isNotBlank(outports)) {
                sql.VALUES("outports", SqlUtils.addSqlStr(outports));
            }
            if (null != outPortType) {
                sql.VALUES("out_port_type", SqlUtils.addSqlStr(outPortType.name()));
            }
            if (StringUtils.isNotBlank(owner)) {
                sql.VALUES("owner", SqlUtils.addSqlStr(owner));
            }
            if (StringUtils.isNotBlank(pageId)) {
                sql.VALUES("page_id", SqlUtils.addSqlStr(pageId));
            }
            if (StringUtils.isNotBlank(crtUser)) {
            	sql.VALUES("crt_user", SqlUtils.addSqlStr(crtUser));
            }
            if (null != flow) {
                String flowId = flow.getId();
                if (StringUtils.isNotBlank(flowId)) {
                    sql.VALUES("FK_template_ID", SqlUtils.addSqlStr(flowId));
                }
            }
            if (null != checkpoint) {
                sql.VALUES("is_checkpoint", (checkpoint ? 1 : 0) + "");
            }
            if (StringUtils.isNotBlank(groups)) {
                sql.VALUES("groups", SqlUtils.addSqlStr(groups));
            }

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
