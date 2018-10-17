package com.nature.mapper.mxGraph;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.component.mxGraph.model.MxGeometry;

public class MxGeometryMapperTest extends ApplicationTests {

	@Autowired
	private MxGeometryMapper mxGeometryMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetMxGeometryById() {
		MxGeometry mxGeometryById = mxGeometryMapper.getMxGeometryById("fadb97a91beb43c9a0d74023c32e249d");
		logger.info(mxGeometryById.toString());
	}

	@Test
	@Rollback(true)
	public void testAddMxGeometry() {
		MxGeometry mxGeometry = new MxGeometry();
		mxGeometry.setId(Utils.getUUID32());
		mxGeometry.setCrtDttm(new Date());
		mxGeometry.setCrtUser("Nature");
		mxGeometry.setEnableFlag(true);
		mxGeometry.setLastUpdateUser("Nature");
		mxGeometry.setLastUpdateDttm(new Date());
		mxGeometry.setVersion(0L);
		mxGeometry.setAs("as");
		mxGeometry.setHeight("111");
		mxGeometry.setRelative("relative");
		mxGeometry.setWidth("111");
		mxGeometry.setX("111");
		mxGeometry.setY("111");
		int addFlow = mxGeometryMapper.addMxGeometry(mxGeometry);
		logger.info(addFlow + "");
	}

}
