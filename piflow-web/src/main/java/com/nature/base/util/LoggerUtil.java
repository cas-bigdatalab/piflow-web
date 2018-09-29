package com.nature.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
	/**
	 * 得到当前class名称，系统所有使用logger的地方直接调用此方法
	 * 
	 * @return
	 */
	public static Logger getLogger() {
		StackTraceElement[] stackEle = new RuntimeException().getStackTrace();
		return LoggerFactory.getLogger(stackEle[1].getClassName());
	}
}
