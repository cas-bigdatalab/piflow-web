package com.nature.mapper.mxGraph;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.component.mxGraph.model.MxCell;

public class MxCellMapperTest extends ApplicationTests {

	@Autowired
	private MxCellMapper mxCellMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	@Rollback(true)
	public void testAddMxCell() {
		MxCell mxCell = new MxCell();
		mxCell.setId(Utils.getUUID32());
		mxCell.setCrtDttm(new Date());
		mxCell.setCrtUser("Nature");
		mxCell.setEnableFlag(true);
		mxCell.setLastUpdateUser("Nature");
		mxCell.setLastUpdateDttm(new Date());
		mxCell.setVersion(0L);
		mxCell.setPageId("12");
		mxCell.setParent("3");
		mxCell.setStyle("dafd");
		mxCell.setEdge("edge");
		mxCell.setSource("source");
		mxCell.setTarget("target");
		mxCell.setValue("value");
		mxCell.setVertex("vertex");
		int addFlow = mxCellMapper.addMxCell(mxCell);
		logger.info(addFlow + "");
	}

}
