package cn.cnic.component.flow.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.flow.entity.FlowStopsPublishing;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FlowStopsPublishingMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String publishingId;
    private String name;

    private boolean preventSQLInjectionFlowStopsPublishing(FlowStopsPublishing flowStopsPublishing) {
        if (null == flowStopsPublishing || StringUtils.isBlank(flowStopsPublishing.getLastUpdateUser()) || StringUtils.isBlank(flowStopsPublishing.getPublishingId())) {
            return false;
        }
        if (null == flowStopsPublishing.getStopsIds() || flowStopsPublishing.getStopsIds().size() <= 0) {
            return false;
        }
        // Mandatory Field
        String id = flowStopsPublishing.getId();
        String lastUpdateUser = flowStopsPublishing.getLastUpdateUser();
        Boolean enableFlag = flowStopsPublishing.getEnableFlag();
        Long version = flowStopsPublishing.getVersion();
        Date lastUpdateDttm = flowStopsPublishing.getLastUpdateDttm();
        this.id = SqlUtils.preventSQLInjection(id);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.publishingId = SqlUtils.preventSQLInjection(flowStopsPublishing.getPublishingId());
        this.name = SqlUtils.preventSQLInjection(flowStopsPublishing.getName());
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.publishingId = null;
        this.name = null;
    }

    private StringBuffer splicingInsert() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("INSERT INTO ");
        stringBuffer.append("flow_stops_publishing ");
        stringBuffer.append("(");
        stringBuffer.append(SqlUtils.baseFieldName());
        stringBuffer.append(",publishing_id");
        stringBuffer.append(",name");
        stringBuffer.append(",stops_id");
        stringBuffer.append(") ");
        stringBuffer.append("VALUES");
        return stringBuffer;
    }

    /**
     * add Stops
     *
     * @param flowStopsPublishing
     * @return
     */
    public String addFlowStopsPublishing(FlowStopsPublishing flowStopsPublishing) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionFlowStopsPublishing(flowStopsPublishing);
        if (flag) {
            StringBuffer stringBuffer = splicingInsert();
            String baseFieldValues = SqlUtils.baseFieldValuesNoId(flowStopsPublishing);
            int i = 0;
            for (String stopsId : flowStopsPublishing.getStopsIds()) {
                i++;
                stringBuffer.append("(");
                stringBuffer.append(SqlUtils.preventSQLInjection(UUIDUtils.getUUID32()));
                stringBuffer.append(",");
                stringBuffer.append(baseFieldValues);
                // handle other fields
                stringBuffer.append(",");
                stringBuffer.append(this.publishingId);
                stringBuffer.append(",");
                stringBuffer.append(this.name);
                stringBuffer.append(",");
                stringBuffer.append(SqlUtils.preventSQLInjection(stopsId));
                if (i != flowStopsPublishing.getStopsIds().size()) {
                    stringBuffer.append("),");
                } else {
                    stringBuffer.append(")");
                }
            }
            sqlStr = stringBuffer.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * updateFlowStopsPublishingName
     *
     * @param username
     * @param publishingId
     * @param name
     * @return
     */
    public String updateFlowStopsPublishingName(String username, String publishingId, String name) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(publishingId)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(name)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();

        sql.UPDATE("flow_stops_publishing");
        String lastUpdateDatetimeStr = DateUtils.dateTimesToStr(new Date());
        sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(lastUpdateDatetimeStr));
        sql.SET("last_update_user = " + username);

        // handle other fields
        sql.SET("name = " + name);
        sql.WHERE("enable_flag = 1");
        sql.WHERE("publishing_id = " + publishingId);
        String sqlStr = sql.toString();
        return sqlStr;
    }

    public String updateFlowStopsPublishingEnableFlagByPublishingId(String username, String publishingId) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(publishingId)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("flow_stops_publishing");
        sql.SET("enable_flag=0");
        sql.SET("last_update_user=" + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm=" + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 0");
        sql.WHERE("publishing_id=" + SqlUtils.preventSQLInjection(publishingId));

        return sql.toString();
    }

    public String updateFlowStopsPublishingEnableFlagByPublishingIdAndStopId(String username, String publishingId, String stopsId) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(publishingId)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(stopsId)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("flow_stops_publishing");
        sql.SET("enable_flag=0");
        sql.SET("last_update_user=" + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm=" + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 0");
        sql.WHERE("publishing_id=" + SqlUtils.preventSQLInjection(publishingId));
        sql.WHERE("stops_id=" + SqlUtils.preventSQLInjection(stopsId));

        return sql.toString();
    }

    /**
     * Query all stops data
     *
     * @return
     */
    public String getFlowStopsPublishingList() {
        return "SELECT * FROM flow_stops_publishing WHERE enable_flag=1";
    }

    /**
     * Query all stops data
     *
     * @return
     */
    public String getFlowStopsPublishingByPublishingId(String publishingId) {
        if (StringUtils.isBlank(publishingId)) {
           return "SELECT 0";
        }
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM flow_stops_publishing WHERE enable_flag=1 and ");
        sql.append("publishing_id=");
        sql.append(SqlUtils.preventSQLInjection(publishingId));
        return sql.toString();
    }

    /**
     * Query all stops data
     *
     * @return
     */
    public String getPublishingStopsIdsByPublishingId(String publishingId) {
        if (StringUtils.isBlank(publishingId)) {
           return "SELECT 0";
        }
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM flow_stops_publishing WHERE enable_flag=1 and ");
        sql.append("publishing_id=");
        sql.append(SqlUtils.preventSQLInjection(publishingId));
        return sql.toString();
    }

    /**
     * Query all stops data
     *
     * @return
     */
    public String getFlowStopsPublishingListByPublishingIdAndStopsId(String publishingId, String stopsId) {
        if (StringUtils.isBlank(publishingId)) {
           return "SELECT 0";
        }
        if (StringUtils.isBlank(stopsId)) {
            return "SELECT 0";
        }
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM flow_stops_publishing WHERE enable_flag=1 ");
        sql.append(" and publishing_id=");
        sql.append(SqlUtils.preventSQLInjection(publishingId));
        sql.append(" and stops_id=");
        sql.append(SqlUtils.preventSQLInjection(stopsId));
        return sql.toString();
    }

    public String getFlowStopsPublishingListByFlowId(String username, String flowId) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(flowId)) {
            return "SELECT 0";
        }
        //SELECT DISTINCT fsp.publishing_id, fsp.name FROM flow_stops_publishing fsp
        //LEFT JOIN flow_stops fs ON fsp.stops_id=fs.id
        //WHERE fsp.enable_flag=1 AND fs.enable_flag=1 AND fsp.crt_user='admin' AND fs.fk_flow_id='6111a00006a44a1e87d112f289d54640';
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT fsp.publishing_id, fsp.name FROM flow_stops_publishing fsp LEFT JOIN flow_stops fs ON fsp.stops_id=fs.id ");
        sql.append(" WHERE fsp.enable_flag=1 AND fs.enable_flag=1 ");
        sql.append(" AND fsp.crt_user=");
        sql.append(SqlUtils.preventSQLInjection(username));
        sql.append(" AND fs.fk_flow_id=");
        sql.append(SqlUtils.preventSQLInjection(flowId));
        return sql.toString();
    }


}
