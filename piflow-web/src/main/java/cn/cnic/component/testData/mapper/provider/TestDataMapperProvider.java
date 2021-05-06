package cn.cnic.component.testData.mapper.provider;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.testData.entity.TestData;

public class TestDataMapperProvider {

    private String id;
    private String lastUpdateUser;
    private String lastUpdateDttmStr;
    private long version;
    private int enableFlag;
    private String name;
    private String description;

    private boolean preventSQLInjectionTestData(TestData testData) {
        if (null == testData || StringUtils.isBlank(testData.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String lastUpdateDttm = DateUtils
                .dateTimesToStr(null != testData.getLastUpdateDttm() ? testData.getLastUpdateDttm() : new Date());
        this.id = SqlUtils.preventSQLInjection(testData.getId());
        this.enableFlag = ((null != testData.getEnableFlag() && testData.getEnableFlag()) ? 1 : 0);
        this.version = (null != testData.getVersion() ? testData.getVersion() : 0L);
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(testData.getLastUpdateUser());

        // Selection field
        this.name = SqlUtils.preventSQLInjection(testData.getName());
        this.description = SqlUtils.preventSQLInjection(testData.getDescription());

        return true;
    }

    private void resetTestData() {
        this.id = null;
        this.lastUpdateUser = null;
        this.lastUpdateDttmStr = null;
        this.version = 0L;
        this.enableFlag = 1;
        this.name = null;
        this.description = null;
    }

    /**
     * add TestData
     * 
     * @param testData
     * @return String
     */
    public String addTestData(TestData testData) {
        String sql = "SELECT 0";
        if (preventSQLInjectionTestData(testData)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO test_data ");
            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("name, ");
            strBuf.append("description ");
            strBuf.append(") ");

            strBuf.append("values ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(testData) + ", ");
            strBuf.append(this.name + ", ");
            strBuf.append(this.description + " ");
            strBuf.append(")");
            sql = strBuf.toString();
        }
        this.resetTestData();
        return sql;
    }

    /**
     * update TestData
     * 
     * @param testData
     * @return String
     */
    public String updateTestData(TestData testData) {
        String sqlStr = "SELECT 0";
        boolean flag = preventSQLInjectionTestData(testData);
        if (flag && StringUtils.isNotBlank(this.id)) {
            SQL sql = new SQL();
            // INSERT_INTO brackets is table name
            sql.UPDATE("test_data");
            // The first string in the SET is the name of the field corresponding to the
            // table in the database
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + this.enableFlag);
            sql.SET("name = " + this.name);
            sql.SET("description = " + this.description);
            sql.WHERE("version = " + this.version);
            sql.WHERE("id = " + this.id);
            sqlStr = sql.toString();
        }
        this.resetTestData();
        return sqlStr;
    }

    /**
     * update TestData enable_flag
     * 
     * @param isAdmin
     * @param username
     * @param id
     * @return String
     */
    public String delTestDataById(boolean isAdmin, String username, String id){
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        String condition = "";
        if (!isAdmin) {
            if (StringUtils.isBlank(username)) {
                return "SELECT 0";
            }
            condition = " AND crt_user=" + SqlUtils.preventSQLInjection(username);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("UPDATE test_data SET enable_flag=0 WHERE ");
        stringBuffer.append("id=" + SqlUtils.preventSQLInjection(id));
        stringBuffer.append(condition);
        return  stringBuffer.toString();
    }

    /**
     * search TestData List
     * 
     * @param isAdmin
     * @param username
     * @param param
     * @return String
     */
    public String getTestDataList(boolean isAdmin, String username, String param) {
        StringBuffer stringBuf = new StringBuffer();
        stringBuf.append("SELECT * FROM test_data WHERE enable_flag=1 ");
        if (!isAdmin) {
            if (StringUtils.isBlank(username)) {
                return "SELECT 0";
            }
            stringBuf.append("AND crt_user=" + SqlUtils.preventSQLInjection(username) + " ");
        }
        if (StringUtils.isNotBlank(param)) {
            stringBuf.append("crt_dttm LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            stringBuf.append("last_update_dttm LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            stringBuf.append("name LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            stringBuf.append("description LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
        }
        stringBuf.append("ORDER BY  last_update_dttm DESC ");
        return stringBuf.toString();
    }


}
