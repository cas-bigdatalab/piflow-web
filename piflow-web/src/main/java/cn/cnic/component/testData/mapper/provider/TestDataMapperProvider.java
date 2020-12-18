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

	public String addTestData(TestData testData) {
		String sql = "select 0";
		if (preventSQLInjectionTestData(testData)) {
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("INSERT INTO group_schedule ");
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

	public String updateTestData(TestData testData) {
		String sqlStr = "select 0";
		boolean flag = preventSQLInjectionTestData(testData);
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
			sql.SET("schedule_id = " + this.name);
			sql.SET("type = " + this.description);
			sql.WHERE("version = " + this.version);
			sql.WHERE("id = " + this.id);
			sqlStr = sql.toString();
		}
		this.resetTestData();
		return sqlStr;
	}

}
