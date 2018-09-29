package com.nature.base.util;

import java.util.UUID;

public class UUIDUtils {
	// 得到32位的uuid
	public static String getUUID32() {

		return UUID.randomUUID().toString().replace("-", "").toLowerCase();

	}
}
