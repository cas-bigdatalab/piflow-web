package com.nature.base.util;

import java.util.UUID;

public class Utils {
	/**
	 * uuid(32位的)
	 * 
	 * @return
	 */
	public static String getUUID32() {
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}

	/**
	 * str(给字符串加单引)
	 * 
	 * @param str
	 * @return
	 */
	public static String addSqlStr(String str) {
		return "'" + str + "'";
	}
}
