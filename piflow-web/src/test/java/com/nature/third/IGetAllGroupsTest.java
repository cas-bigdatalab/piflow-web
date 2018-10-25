package com.nature.third;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.third.inf.IGetAllGroups;

public class IGetAllGroupsTest extends ApplicationTests {

	@Resource
	private IGetAllGroups getAllGroupsImpl;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testDoGet() {
		getAllGroupsImpl.getAllGroup();
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void testAddFlow() {
	}

}
