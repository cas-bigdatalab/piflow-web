package cn.cnic.component.testData.mapper.provider;

import java.util.Date;
import java.util.List;

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
				null != testDataSchemaValues.getLastUpdateDttm() ? testDataSchemaValues.getLastUpdateDttm() : new Date());
		this.id = SqlUtils.preventSQLInjection(testDataSchemaValues.getId());
		this.enableFlag = ((null != testDataSchemaValues.getEnableFlag() && testDataSchemaValues.getEnableFlag()) ? 1 : 0);
		this.version = (null != testDataSchemaValues.getVersion() ? testDataSchemaValues.getVersion() : 0L);
		this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
		this.lastUpdateUser = SqlUtils.preventSQLInjection(testDataSchemaValues.getLastUpdateUser());

		// Selection field
		this.fieldValue = SqlUtils.preventSQLInjection(testDataSchemaValues.getFieldValue());
		this.dataRow = testDataSchemaValues.getDataRow();
		this.testDataSchemaId = SqlUtils.preventSQLInjection(null != testDataSchemaValues.getTestDataSchema() ? testDataSchemaValues.getTestDataSchema().getId() : null);

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
		String sql = "select 0";
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
		String sql = "select 0";
		if (null == testDataSchemaValuesList || testDataSchemaValuesList.size() <= 0) {
			return "select 0";
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
		String sqlStr = "select 0";
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

}
