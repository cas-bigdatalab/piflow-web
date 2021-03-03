package cn.cnic.component.testData.mapper.provider;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.testData.entity.TestDataSchemaValues;

public class TestDataSchemaValuesMapperProvider {

	private String id;
	private String lastUpdateUser;
	private String lastUpdateDttmStr;
	private long version;
	private int enableFlag;
	private String fieldValue;
	private int dataRow;
	private String testDataSchemaId;

	private boolean preventSQLInjectionTestDataSchemaValues(TestDataSchemaValues testDataSchemaValues) {
		if (null == testDataSchemaValues || StringUtils.isBlank(testDataSchemaValues.getLastUpdateUser())) {
			return false;
		}
		// Mandatory Field
		String lastUpdateDttm = DateUtils.dateTimesToStr(
				null != testDataSchemaValues.getLastUpdateDttm() ? testDataSchemaValues.getLastUpdateDttm()
						: new Date());
		this.id = SqlUtils.preventSQLInjection(testDataSchemaValues.getId());
		this.enableFlag = ((null != testDataSchemaValues.getEnableFlag() && testDataSchemaValues.getEnableFlag()) ? 1
				: 0);
		this.version = (null != testDataSchemaValues.getVersion() ? testDataSchemaValues.getVersion() : 0L);
		this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
		this.lastUpdateUser = SqlUtils.preventSQLInjection(testDataSchemaValues.getLastUpdateUser());

		// Selection field
		this.fieldValue = SqlUtils.preventSQLInjection(testDataSchemaValues.getFieldValue());
		this.dataRow = testDataSchemaValues.getDataRow();
		this.testDataSchemaId = SqlUtils.preventSQLInjection(
				null != testDataSchemaValues.getTestDataSchema() ? testDataSchemaValues.getTestDataSchema().getId()
						: null);

		return true;
	}

	private void resetTestDataSchemaValues() {
		this.id = null;
		this.lastUpdateUser = null;
		this.lastUpdateDttmStr = null;
		this.version = 0L;
		this.enableFlag = 1;
		this.fieldValue = null;
		this.dataRow = 0;
		this.testDataSchemaId = null;
	}

	public String addTestDataSchemaValues(TestDataSchemaValues testDataSchemaValues) {
		String sql = "SELECT 0";
		if (preventSQLInjectionTestDataSchemaValues(testDataSchemaValues)) {
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("INSERT INTO group_schedule ");
			strBuf.append("( ");
			strBuf.append(SqlUtils.baseFieldName() + ", ");
			strBuf.append("field_value, ");
			strBuf.append("data_row, ");
			strBuf.append("fk_test_data_schema_id ");
			strBuf.append(") ");

			strBuf.append("values ");
			strBuf.append("(");
			strBuf.append(SqlUtils.baseFieldValues(testDataSchemaValues) + ", ");
			strBuf.append(this.fieldValue + ", ");
			strBuf.append(this.dataRow + ", ");
			strBuf.append(this.testDataSchemaId + " ");
			strBuf.append(")");
			sql = strBuf.toString();
		}
		this.resetTestDataSchemaValues();
		return sql;
	}

	public String addTestDataSchemaValuesList(List<TestDataSchemaValues> testDataSchemaValuesList) {
		String sql = "SELECT 0";
		if (null == testDataSchemaValuesList || testDataSchemaValuesList.size() <= 0) {
			return "SELECT 0";
		}
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("INSERT INTO group_schedule ");
		strBuf.append("( ");
		strBuf.append(SqlUtils.baseFieldName() + ", ");
		strBuf.append("field_value, ");
		strBuf.append("data_row, ");
		strBuf.append("fk_test_data_schema_id ");
		strBuf.append(") ");
		strBuf.append("values ");
		boolean firstFlag = true;
		for (int i = 0; i < testDataSchemaValuesList.size(); i++) {
			TestDataSchemaValues testDataSchemaValues = testDataSchemaValuesList.get(i);
			if (preventSQLInjectionTestDataSchemaValues(testDataSchemaValues)) {
				if (firstFlag) {
					strBuf.append("(");
				} else {
					strBuf.append(",(");
				}
				strBuf.append(SqlUtils.baseFieldValues(testDataSchemaValues) + ", ");
				strBuf.append(this.fieldValue + ", ");
				strBuf.append(this.dataRow + ", ");
				strBuf.append(this.testDataSchemaId + " ");
				strBuf.append(")");
			}
			this.resetTestDataSchemaValues();
		}
		sql = strBuf.toString();
		return sql;
	}

	public String updateTestDataSchemaValues(TestDataSchemaValues testDataSchemaValues) {
		String sqlStr = "SELECT 0";
		boolean flag = preventSQLInjectionTestDataSchemaValues(testDataSchemaValues);
		if (flag && StringUtils.isNotBlank(this.id)) {
			SQL sql = new SQL();
			// INSERT_INTO brackets is table name
			sql.UPDATE("group_schedule");
			// The first string in the SET is the name of the field corresponding to the
			// table in the database
			sql.SET("last_update_dttm = " + lastUpdateDttmStr);
			sql.SET("last_update_user = " + lastUpdateUser);
			sql.SET("version = " + (version + 1));

			// handle other fields
			sql.SET("enable_flag = " + this.enableFlag);
			sql.SET("field_value = " + this.fieldValue);
			sql.SET("data_row = " + this.dataRow);
			sql.SET("fk_test_data_schema_id = " + this.testDataSchemaId);
			sql.WHERE("version = " + this.version);
			sql.WHERE("id = " + this.id);
			sqlStr = sql.toString();
		}
		this.resetTestDataSchemaValues();
		return sqlStr;
	}

	public static String getTestDataSchemaValuesCustomList(@SuppressWarnings("rawtypes") Map map) {
		if (null == map) {
			return "SELECT 0";
		}
		@SuppressWarnings("unchecked")
		List<Map<String, String>> fieldNameList = (List<Map<String, String>>) map.get("fieldNameList");
		if (null == fieldNameList || fieldNameList.size() <= 0) {
			return "SELECT 0";
		}
		String condition = " ";
		boolean isAdmin = (boolean) map.get("isAdmin");
		String username = (String) map.get("username");

		if (!isAdmin) {
			if (StringUtils.isBlank(username)) {
				return "SELECT 0";
			}
			condition = "AND TDSV.crt_user=" + SqlUtils.preventSQLInjection(username);
		}
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("SELECT TDSV.data_row as 'dataRow' ");
		for (Map<String, String> fieldName : fieldNameList) {
			strBuf.append(",MAX( ");
			strBuf.append("CASE TDSV.fk_test_data_schema_id WHEN ");
			strBuf.append(SqlUtils.preventSQLInjection(fieldName.get("id")) + " ");
			strBuf.append("THEN TDSV.field_value END ");
			strBuf.append(") AS ");
			strBuf.append(SqlUtils.preventSQLInjection(fieldName.get("field_name")) + " ");
		}
		strBuf.append("FROM test_data_schema_values TDSV ");
		strBuf.append("WHERE TDSV.enable_flag=1 ");
		strBuf.append(condition + " ");
		strBuf.append("GROUP BY TDSV.data_row");

		return strBuf.toString();

	}

	public String delTestDataSchemaValuesByTestDataId(boolean isAdmin, String username, String testDataId){
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
        stringBuffer.append("UPDATE test_data_list SET enable_flag=0 WHERE ");
        stringBuffer.append("fk_test_data_id=" + SqlUtils.preventSQLInjection(testDataId));
        stringBuffer.append(condition);
		return  stringBuffer.toString();
	}

}
