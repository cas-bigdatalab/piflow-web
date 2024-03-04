package cn.cnic.component.flow.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowPublishing;
import cn.cnic.component.flow.entity.FlowStopsPublishingProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class FlowStopsPublishingPropertyMapperProvider {

    private Long id;
    private Date lastUpdateDttm;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private Long publishingId;
    private String stopId;
    private String stopName;
    private String stopBundle;
    private String propertyId;
    private String propertyName;
    private String name;
    private Integer type;
    private String allowableValues;
    private String customValue;
    private String description;
    private Long propertySort;
    private String example;

    private boolean preventSQLInjectionFlowStopsPublishingProperty(FlowStopsPublishingProperty flowStopsPublishingProperty) {
        if (null == flowStopsPublishingProperty) {
            return false;
        }

        // Mandatory Field
        Boolean enableFlag = flowStopsPublishingProperty.getEnableFlag();
        Long version = flowStopsPublishingProperty.getVersion();
        this.id = flowStopsPublishingProperty.getId();
        this.lastUpdateUser = SqlUtils.preventSQLInjection(flowStopsPublishingProperty.getLastUpdateUser());
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateDttm = flowStopsPublishingProperty.getLastUpdateDttm();

        // Selection field
        this.publishingId = flowStopsPublishingProperty.getPublishingId();
        this.stopId = SqlUtils.preventSQLInjection(flowStopsPublishingProperty.getStopId());
        this.stopName = SqlUtils.preventSQLInjection(flowStopsPublishingProperty.getStopName());
        this.stopBundle = SqlUtils.preventSQLInjection(flowStopsPublishingProperty.getStopBundle());
        this.propertyId = SqlUtils.preventSQLInjection(flowStopsPublishingProperty.getPropertyId());
        this.propertyName = SqlUtils.preventSQLInjection(flowStopsPublishingProperty.getPropertyName());
        this.name = SqlUtils.preventSQLInjection(flowStopsPublishingProperty.getName());
        this.type = flowStopsPublishingProperty.getType();
        this.allowableValues = SqlUtils.preventSQLInjection(flowStopsPublishingProperty.getAllowableValues());
        this.customValue = SqlUtils.preventSQLInjection(flowStopsPublishingProperty.getCustomValue());
        this.description = SqlUtils.preventSQLInjection(flowStopsPublishingProperty.getDescription());
        this.propertySort =flowStopsPublishingProperty.getPropertySort();
        this.example = SqlUtils.preventSQLInjection(flowStopsPublishingProperty.getExample());
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttm = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.publishingId = null;
        this.stopId = null;
        this.stopName = null;
        this.stopBundle = null;
        this.propertyId = null;
        this.propertyName = null;
        this.name = null;
        this.type = 0;//默认是其他
        this.allowableValues = null;
        this.customValue = null;
        this.description = null;
        this.propertySort = null;
        this.example = null;
    }

    /**
     * add FlowStopsPublishingProperty
     *
     * @param flowStopsPublishingProperty
     * @return
     */
    public String addFlowStopsPublishingProperty(FlowStopsPublishingProperty flowStopsPublishingProperty) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionFlowStopsPublishingProperty(flowStopsPublishingProperty);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO flow_stops_publishing_property ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldName()).append(", ");
            stringBuffer.append("publishing_id, ");
            stringBuffer.append("stop_id, ");
            stringBuffer.append("stop_name, ");
            stringBuffer.append("stop_bundle, ");
            stringBuffer.append("property_id, ");
            stringBuffer.append("property_name, ");
            stringBuffer.append("name, ");
            stringBuffer.append("type, ");
            stringBuffer.append("allowable_values, ");
            stringBuffer.append("custom_value, ");
            stringBuffer.append("description, ");
            stringBuffer.append("property_sort, ");
            stringBuffer.append("example ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValuesWithLongId(flowStopsPublishingProperty)).append(", ");
            // handle other fields
            stringBuffer.append(this.publishingId).append(", ");
            stringBuffer.append(this.stopId).append(", ");
            stringBuffer.append(this.stopName).append(", ");
            stringBuffer.append(this.stopBundle).append(", ");
            stringBuffer.append(this.propertyId).append(", ");
            stringBuffer.append(this.propertyName).append(", ");
            stringBuffer.append(this.name).append(", ");
            stringBuffer.append(this.type).append(", ");
            stringBuffer.append(this.allowableValues).append(", ");
            stringBuffer.append(this.customValue).append(", ");
            stringBuffer.append(this.description).append(", ");
            stringBuffer.append(this.propertySort).append(", ");
            stringBuffer.append(this.example);
            stringBuffer.append(" ) ");
            sqlStr = stringBuffer.toString();
            this.reset();
        }
        return sqlStr;
    }

//    /**
//     * update dataProductType
//     *
//     * @param flow
//     * @return
//     */
//    public String updateFlow(Flow flow) {
//
//        String sqlStr = "";
//        this.preventSQLInjectionFlow(flow);
//        if (null != flow) {
//            SQL sql = new SQL();
//
//            // INSERT_INTO brackets is table name
//            sql.UPDATE("flow");
//            // The first string in the SET is the name of the field corresponding to the table in the database
//            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
//            sql.SET("last_update_user = " + lastUpdateUser);
//            sql.SET("version = " + (version + 1));
//
//            // handle other fields
//            sql.SET("enable_flag = " + enableFlag);
//            sql.SET("description = " + description);
//            sql.SET("name = " + name);
//            sql.SET("uuid = " + uuid);
//            sql.SET("driver_memory = " + driverMemory);
//            sql.SET("executor_cores = " + executorCores);
//            sql.SET("executor_memory = " + executorMemory);
//            sql.SET("executor_number = " + executorNumber);
//            sql.WHERE("version = " + version);
//            sql.WHERE("id = " + id);
//            sqlStr = sql.toString();
//            if (StringUtils.isBlank(id)) {
//                sqlStr = "";
//            }
//        }
//        this.reset();
//        return sqlStr;
//    }
//
//    /**
//     * get flow list
//     *
//     * @return
//     */
//    public String getFlowList() {
//        String sqlStr = "";
//        SQL sql = new SQL();
//        sql.SELECT("*");
//        sql.FROM("flow");
//        sql.WHERE("enable_flag = 1");
//        sql.WHERE("is_example = 0");
//        sql.WHERE("fk_flow_group_id = null ");
//        sql.ORDER_BY(" crt_dttm desc  ");
//        sqlStr = sql.toString();
//        return sqlStr;
//    }

}
