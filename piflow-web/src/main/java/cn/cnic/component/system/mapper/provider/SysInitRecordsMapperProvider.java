package cn.cnic.component.system.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.system.entity.SysInitRecords;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class SysInitRecordsMapperProvider {

    private String id;
    private String initDate;
    private int isSucceed;


    private boolean preventSQLInjection(SysInitRecords sysInitRecords) {

        if (null == sysInitRecords) {
            return false;
        }
        String id = StringUtils.isBlank(sysInitRecords.getId()) ? UUIDUtils.getUUID32() : sysInitRecords.getId();
        Date initDate = sysInitRecords.getInitDate();
        String initDateStr = DateUtils.dateTimesToStr(null != initDate ? initDate : new Date());
        this.id = SqlUtils.preventSQLInjection(id);
        this.initDate = SqlUtils.preventSQLInjection(initDateStr);
        this.isSucceed = ((null != sysInitRecords.getIsSucceed() && sysInitRecords.getIsSucceed()) ? 1 : 0);
        return true;

    }

    private void reset() {
        this.id = null;
        this.initDate = null;
        this.isSucceed = 0;
    }


    /**
     * insertSysInitRecords
     *
     * @param sysInitRecords
     * @return
     */
    public String insertSysInitRecords(SysInitRecords sysInitRecords) {
        String sqlStr = "select 0";
        if (preventSQLInjection(sysInitRecords)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO sys_init_records ");
            strBuf.append("( ");
            strBuf.append("id, ");
            strBuf.append("init_date, ");
            strBuf.append("is_succeed ");
            strBuf.append(") ");
            strBuf.append("values ");
            strBuf.append("(");
            strBuf.append(this.id + ", ");
            strBuf.append(this.initDate + ", ");
            strBuf.append(this.isSucceed + " ");
            strBuf.append(") ");
            sqlStr = strBuf.toString();
        }
        this.reset();
        return sqlStr;
    }

}
