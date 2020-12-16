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
	}

	public String addTestDataSchema(TestDataSchema testDataSchema) {
		String sql = "select 0";
		if (preventSQLInjectionTestDataSchema(testDataSchema)) {
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("INSERT INTO group_schedule ");
			strBuf.append("( ");
			strBuf.append(SqlUtils.baseFieldName() + ", ");
			strBuf.append("field_name, ");
			strBuf.append("field_type, ");
			strBuf.append("field_description, ");
			strBuf.append("field_soft ");
			strBuf.append(") ");

			strBuf.append("values ");
			strBuf.append("(");
			strBuf.append(SqlUtils.baseFieldValues(testDataSchema) + ", ");
			strBuf.append(this.fieldName + ", ");
			strBuf.append(this.fieldType + ", ");
			strBuf.append(this.fieldDescription + ", ");
			strBuf.append(this.fieldSoft + " ");
			strBuf.append(")");
			sql = strBuf.toString();
		}
		this.resetTestDataSchema();
		return sql;
	}

	public String addTestDataSchemaList(List<TestDataSchema> testDataSchemaList) {
		String sql = "select 0";
		if (null == testDataSchemaList || testDataSchemaList.size() <= 0) {
			return "select 0";
		}
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("INSERT INTO group_schedule ");
		strBuf.append("( ");
		strBuf.append(SqlUtils.baseFieldName() + ", ");
		strBuf.append("field_name, ");
		strBuf.append("field_type, ");
		strBuf.append("field_description, ");
		strBuf.append("field_soft ");
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
				strBuf.append(this.fieldSoft + " ");
				strBuf.append(")");
			}
			this.resetTestDataSchema();
		}
		sql = strBuf.toString();
		return sql;
	}

	public String updeateTestDataSchema(TestDataSchema testDataSchema) {
		String sqlStr = "select 0";
		boolean flag = preventSQLInjectionTestDataSchema(testDataSchema);
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
			sql.SET("field_name = " + this.fieldName);
			sql.SET("field_type = " + this.fieldType);
			sql.SET("field_description = " + this.fieldDescription);
			sql.SET("field_soft = " + this.fieldSoft);
			sql.WHERE("version = " + this.version);
			sql.WHERE("id = " + this.id);
			sqlStr = sql.toString();
		}
		this.resetTestDataSchema();
		return sqlStr;
	}

}
