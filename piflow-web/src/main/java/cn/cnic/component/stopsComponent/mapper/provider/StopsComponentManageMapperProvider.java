package cn.cnic.component.stopsComponent.mapper.provider;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.stopsComponent.model.StopsComponentManage;

public class StopsComponentManageMapperProvider {

    private String id;
    private String lastUpdateUser;
    private String lastUpdateDttmStr;
    private Long version;
    private String bundle;
    private String stopsGroups;
    private int isShow = 0;
    

    private boolean preventSQLInjectionStopsManage(StopsComponentManage stopsComponentManage) {
        if (null == stopsComponentManage || StringUtils.isBlank(stopsComponentManage.getLastUpdateUser())) {
            return true;
        }
        // Mandatory Field
        this.id = SqlUtils.preventSQLInjection(stopsComponentManage.getId());
        this.lastUpdateUser = SqlUtils.preventSQLInjection(stopsComponentManage.getLastUpdateUser());
        this.version = (null != stopsComponentManage.getVersion() ? stopsComponentManage.getVersion() : 0L);
        Date lastUpdateDttm = stopsComponentManage.getLastUpdateDttm();
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);
        
        // Selection field
        this.bundle = SqlUtils.preventSQLInjection(stopsComponentManage.getBundle());
        this.stopsGroups = SqlUtils.preventSQLInjection(stopsComponentManage.getStopsGroups());
        this.isShow = stopsComponentManage.getIsShow()?1:0;
        return true;
    }

    private void reset() {
        this.bundle = null;
        this.stopsGroups = null;
        this.isShow = 0;
    }

    public String insertStopsComponentManage(StopsComponentManage stopsComponentManage) {
        boolean flag = this.preventSQLInjectionStopsManage(stopsComponentManage);
        if (!flag) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("INSERT INTO flow_stops_template_manage ");

        strBuf.append("( ");
        strBuf.append(SqlUtils.baseFieldName() + ", ");
        strBuf.append("bundle, stops_groups, is_show ");
        strBuf.append(") ");

        strBuf.append("values ");
        strBuf.append("(");
        strBuf.append(SqlUtils.baseFieldValues(stopsComponentManage) + ", ");
        strBuf.append(bundle + "," + stopsGroups + "," + isShow);
        strBuf.append(")");
        this.reset();
        return strBuf.toString() + ";";
    }
    
    public String updateStopsComponentManage(StopsComponentManage stopsComponentManage) {
        boolean flag = this.preventSQLInjectionStopsManage(stopsComponentManage);
        if (!flag) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }

        SQL sql = new SQL();

        // INSERT_INTO brackets is table name
        sql.UPDATE("flow_stops_template_manage");
        // The first string in the SET is the name of the field corresponding to the table in the database
        sql.SET("last_update_dttm = " + lastUpdateDttmStr);
        sql.SET("last_update_user = " + lastUpdateUser);
        sql.SET("version = " + (version + 1));

        // handle other fields
        sql.SET("bundle = " + bundle);
        sql.SET("stops_groups = " + stopsGroups);
        sql.SET("is_show = " + isShow);
        sql.WHERE("version = " + version);
        sql.WHERE("id = " + id);
        String sqlStr = sql.toString();
        this.reset();
        return sqlStr;
    }
    
    public String getStopsComponentManageByBundleAndGroup(String bundle, String stopsGroups) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * FROM flow_stops_template_manage ");
        stringBuffer.append("WHERE ");
        stringBuffer.append("bundle= " + SqlUtils.preventSQLInjection(bundle) + " ");
        stringBuffer.append("AND stops_groups= " + SqlUtils.preventSQLInjection(stopsGroups));
        return stringBuffer.toString();
    }

}

