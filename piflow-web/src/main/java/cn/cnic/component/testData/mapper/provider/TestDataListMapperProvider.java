package cn.cnic.component.testData.mapper.provider;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.testData.entity.TestDataList;

public class TestDataListMapperProvider {

	private String id;
	private String lastUpdateUser;
	private String lastUpdateDttmStr;
	private long version;
	private int enableFlag;
	private String fieldValue;
	private int dataRow;
	private String testDataId;
	private String testDataSchemaId;

	private boolean preventSQLInjectionTestDataList(TestDataList testDataList) {
		if (null == testDataList || StringUtils.isBlank(testDataList.getLastUpdateUser())) {
			return false;
		}
		// Mandatory Field
		String lastUpdateDttm = DateUtils.dateTimesToStr(
				null != testDataList.getLastUpdateDttm() ? testDataList.getLastUpdateDttm() : new Date());
		this.id = SqlUtils.preventSQLInjection(testDataList.getId());
		this.enableFlag = ((null != testDataList.getEnableFlag() && testDataList.getEnableFlag()) ? 1 : 0);
		this.version = (null != testDataList.getVersion() ? testDataList.getVersion() : 0L);
		this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
		this.lastUpdateUser = SqlUtils.preventSQLInjection(testDataList.getLastUpdateUser());

		// Selection field
		this.fieldValue = SqlUtils.preventSQLInjection(testDataList.getFieldValue());
		this.dataRow = testDataList.getDataRow();
		this.testDataId = SqlUtils.preventSQLInjection(null != testDataList.getTestData() ? testDataList.getTestData().getId() : null);
		this.testDataSchemaId = SqlUtils.preventSQLInjection(null != testDataList.getTestDataSchema() ? testDataList.getTestData().getId() : null);

		return true;
	}

	private void resetTestDataList() {
		this.id = null;
		this.lastUpdateUser = null;
		this.lastUpdateDttmStr = null;
		this.version = 0L;
		this.enableFlag = 1;
		this.fieldValue = null;
		this.dataRow = 0;
		this.testDataId = null;
		this.testDataSchemaId = null;
	}

	public String addTestDataList(TestDataList testDataList) {
		String sql = "select 0";
		if (preventSQLInjectionTestDataList(testDataList)) {
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("INSERT INTO group_schedule ");
			strBuf.append("( ");
			strBuf.append(SqlUtils.baseFieldName() + ", ");
			strBuf.append("field_value, ");
			strBuf.append("data_row, ");
			strBuf.append("fk_test_data_id, ");
			strBuf.append("fk_test_data_schema_id ");
			strBuf.append(") ");

			strBuf.append("values ");
			strBuf.append("(");
			strBuf.append(SqlUtils.baseFieldValues(testDataList) + ", ");
			strBuf.append(this.fieldValue + ", ");
			strBuf.append(this.dataRow + ", ");
			strBuf.append(this.testDataId + ", ");
			strBuf.append(this.testDataSchemaId + " ");
			strBuf.append(")");
			sql = strBuf.toString();
		}
		this.resetTestDataList();
		return sql;
	}

	public String addTestDataListList(List<TestDataList> testDataListList) {
		String sql = "select 0";
		if (null == testDataListList || testDataListList.size() <= 0) {
			return "select 0";
		}
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("INSERT INTO group_schedule ");
		strBuf.append("( ");
		strBuf.append(SqlUtils.baseFieldName() + ", ");
		strBuf.append("field_value, ");
		strBuf.append("data_row, ");
		strBuf.append("fk_test_data_id, ");
		strBuf.append("fk_test_data_schema_id ");
		strBuf.append(") ");
		strBuf.append("values ");
		boolean firstFlag = true;
		for (int i = 0; i < testDataListList.size(); i++) {
			TestDataList testDataList = testDataListList.get(i);
			if (preventSQLInjectionTestDataList(testDataList)) {
				if (firstFlag) {
					strBuf.append("(");
				} else {
					strBuf.append(",(");
				}
				strBuf.append(SqlUtils.baseFieldValues(testDataList) + ", ");
				strBuf.append(this.fieldValue + ", ");
				strBuf.append(this.dataRow + ", ");
				strBuf.append(this.testDataId + ", ");
				strBuf.append(this.testDataSchemaId + " ");
				strBuf.append(")");
			}
			this.resetTestDataList();
		}
		sql = strBuf.toString();
		return sql;
	}

	public String updeateTestDataList(TestDataList testDataList) {
		String sqlStr = "select 0";
		boolean flag = preventSQLInjectionTestDataList(testDataList);
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
			sql.SET("fk_test_data_id = " + this.testDataId);
			sql.SET("fk_test_data_schema_id = " + this.testDataSchemaId);
			sql.WHERE("version = " + this.version);
			sql.WHERE("id = " + this.id);
			sqlStr = sql.toString();
		}
		this.resetTestDataList();
		return sqlStr;
	}

}
