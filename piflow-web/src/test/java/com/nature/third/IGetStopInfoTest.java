package com.nature.third;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.third.inf.IGetStopInfo;

public class IGetStopInfoTest extends ApplicationTests {

	@Resource
	private IGetStopInfo getStopInfoImpl;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testDoGet() {
		String bundle = "cn.piflow.bundle.csv.CsvParser";
		getStopInfoImpl.getStopInfo(bundle);
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void testAddFlow() {
	}
}
