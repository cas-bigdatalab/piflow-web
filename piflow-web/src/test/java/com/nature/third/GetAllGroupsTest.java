package com.nature.third;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;

public class GetAllGroupsTest extends ApplicationTests {

	@Resource
	private GetAllGroups getAllGroups;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testDoGet() {
		getAllGroups.get();
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void testAddFlow() {
	}

}
