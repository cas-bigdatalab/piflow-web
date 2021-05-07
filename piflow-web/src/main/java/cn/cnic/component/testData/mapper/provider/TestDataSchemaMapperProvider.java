package cn.cnic.component.testData.mapper.provider;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.testData.entity.TestDataSchema;

public class TestDataSchemaMapperProvider {

    private String id;
    private String lastUpdateUser;
    private String lastUpdateDttmStr;
    private long version;
    private int enableFlag;
    private String fieldName;
    private String fieldType;
    private String fieldDescription;
    private int fieldSoft;
    private String testDataId;

    private boolean preventSQLInjectionTestDataSchema(TestDataSchema testDataSchema) {
        if (null == testDataSchema || StringUtils.isBlank(testDataSchema.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String lastUpdateDttm = DateUtils.dateTimesToStr(
                null != testDataSchema.getLastUpdateDttm() ? testDataSchema.getLastUpdateDttm() : new Date());
        this.id = SqlUtils.preventSQLInjection(testDataSchema.getId());
        this.enableFlag = ((null != testDataSchema.getEnableFlag() && testDataSchema.getEnableFlag()) ? 1 : 0);
        this.version = (null != testDataSchema.getVersion() ? testDataSchema.getVersion() : 0L);
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(testDataSchema.getLastUpdateUser());

        // Selection field
        this.fieldName = SqlUtils.preventSQLInjection(testDataSchema.getFieldName());
        this.fieldType = SqlUtils.preventSQLInjection(testDataSchema.getFieldType());
        this.fieldDescription = SqlUtils.preventSQLInjection(testDataSchema.getFieldDescription());
        this.fieldSoft = testDataSchema.getFieldSoft();
        this.testDataId = SqlUtils.preventSQLInjection(
                null != testDataSchema.getTestData() ? testDataSchema.getTestData().getId() : null);

        return true;
    }

    private void resetTestDataSchema() {
        this.id = null;
        this.lastUpdateUser = null;
        this.lastUpdateDttmStr = null;
        this.version = 0L;
        this.enableFlag = 1;
        this.fieldName = null;
        this.fieldType = null;
        this.fieldDescription = null;
        this.fieldSoft = 0;
        this.testDataId = null;
    }

    /**
     * add TestDataSchema
     * 
     * @param testDataSchema
     * @return Integer
     */
    public String addTestDataSchema(TestDataSchema testDataSchema) {
        String sql = "SELECT 0";
        if (preventSQLInjectionTestDataSchema(testDataSchema)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO test_data_schema ");
            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("field_name, ");
            strBuf.append("field_type, ");
            strBuf.append("field_description, ");
            strBuf.append("field_soft, ");
            strBuf.append("fk_test_data_id ");
            strBuf.append(") ");

            strBuf.append("values ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(testDataSchema) + ", ");
            strBuf.append(this.fieldName + ", ");
            strBuf.append(this.fieldType + ", ");
            strBuf.append(this.fieldDescription + ", ");
            strBuf.append(this.fieldSoft + ", ");
            strBuf.append(this.testDataId + " ");
            strBuf.append(")");
            sql = strBuf.toString();
        }
        this.resetTestDataSchema();
        return sql;
    }

    /**
     * add TestDataSchema List
     * 
     * @param testDataSchemaList
     * @return
     */
    public String addTestDataSchemaList(List<TestDataSchema> testDataSchemaList) {
        String sql = "SELECT 0";
        if (null == testDataSchemaList || testDataSchemaList.size() <= 0) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("INSERT INTO test_data_schema ");
        strBuf.append("( ");
        strBuf.append(SqlUtils.baseFieldName() + ", ");
        strBuf.append("field_name, ");
        strBuf.append("field_type, ");
        strBuf.append("field_description, ");
        strBuf.append("field_soft, ");
        strBuf.append("fk_test_data_id ");
        strBuf.append(") ");
        strBuf.append("values ");
        boolean firstFlag = true;
        for (int i = 0; i < testDataSchemaList.size(); i++) {
            TestDataSchema testDataSchema = testDataSchemaList.get(i);
            if (preventSQLInjectionTestDataSchema(testDataSchema)) {
                if (firstFlag) {
                    strBuf.append("(");
                } else {
                    strBuf.append(",(");
                }
                strBuf.append(SqlUtils.baseFieldValues(testDataSchema) + ", ");
                strBuf.append(this.fieldName + ", ");
                strBuf.append(this.fieldType + ", ");
                strBuf.append(this.fieldDescription + ", ");
                strBuf.append(this.fieldSoft + ", ");
                strBuf.append(this.testDataId + " ");
                strBuf.append(")");
            }
            this.resetTestDataSchema();
        }
        sql = strBuf.toString();
        return sql;
    }

    /**
     * update TestDataSchema
     * 
     * @param testDataSchema
     * @return
     */
    public String updateTestDataSchema(TestDataSchema testDataSchema) {
        String sqlStr = "SELECT 0";
        boolean flag = preventSQLInjectionTestDataSchema(testDataSchema);
        if (flag && StringUtils.isNotBlank(this.id)) {
            SQL sql = new SQL();
            // INSERT_INTO brackets is table name
            sql.UPDATE("test_data_schema");
            // The first string in the SET is the name of the field corresponding to the
            // table in the database
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + this.enableFlag);
            sql.SET("field_name = " + this.fieldName);
            sql.SET("field_type = " + this.fieldType);
            sql.SET("field_description = " + this.fieldDescription);
            sql.SET("field_soft = " + this.fieldSoft);
            sql.SET("fk_test_data_id = " + this.testDataId);
            sql.WHERE("version = " + this.version);
            sql.WHERE("id = " + this.id);
            sqlStr = sql.toString();
        }
        this.resetTestDataSchema();
        return sqlStr;
    }

    /**
     * update TestDataSchema enable_flag
     * 
     * @param isAdmin
     * @param username
     * @param testDataId
     * @return
     */
    public String delTestDataSchemaByTestDataId(boolean isAdmin, String username, String testDataId){
        if (StringUtils.isBlank(testDataId)) {
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
        stringBuffer.append("UPDATE test_data_schema SET enable_flag=0 WHERE ");
        stringBuffer.append("fk_test_data_id=" + SqlUtils.preventSQLInjection(testDataId));
        stringBuffer.append(condition);
        return  stringBuffer.toString();
    }

    /**
     * update TestDataSchema enable_flag
     *
     * @param isAdmin
     * @param username
     * @param schemaId
     * @return
     */
    public String delTestDataSchemaById(boolean isAdmin, String username, String schemaId){
        if (StringUtils.isBlank(schemaId)) {
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
        stringBuffer.append("UPDATE test_data_schema SET enable_flag=0 WHERE ");
        stringBuffer.append("id=" + SqlUtils.preventSQLInjection(schemaId));
        stringBuffer.append(condition);
        return  stringBuffer.toString();
    }

    /**
     * get TestDataSchema by id
     * 
     * @param id
     * @return
     */
    public String getTestDataSchemaById(String id){
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * FROM test_data_schema WHERE enable_flag=1 ");
        stringBuffer.append("AND id=" + SqlUtils.preventSQLInjection(id));
        return  stringBuffer.toString();
    }
    
    /**
     * get TestDataSchema list by testDataId search
     * 
     * @param isAdmin
     * @param username
     * @param param
     * @param testDataId
     * @return
     */
    public String getTestDataSchemaListByTestDataIdSearch(boolean isAdmin, String username, String param, String testDataId){
        if (StringUtils.isBlank(testDataId)) {
            return "SELECT 0";
        }
        StringBuffer stringBuf = new StringBuffer();
        stringBuf.append("SELECT * FROM test_data_schema WHERE enable_flag=1 ");
        stringBuf.append("AND fk_test_data_id=" + SqlUtils.preventSQLInjection(testDataId) + " ");
        if (!isAdmin) {
            if (StringUtils.isBlank(username)) {
                return "SELECT 0";
            }
            stringBuf.append("AND crt_user=" + SqlUtils.preventSQLInjection(username) + " ");
        }
        if (StringUtils.isNotBlank(param)) {
            stringBuf.append("crt_dttm LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            stringBuf.append("last_update_dttm LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            stringBuf.append("field_name LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            stringBuf.append("field_type LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
        }
        stringBuf.append("ORDER BY  field_soft ASC ");
        return stringBuf.toString();
    }

}
