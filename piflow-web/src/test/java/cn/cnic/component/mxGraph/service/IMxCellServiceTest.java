package cn.cnic.component.mxGraph.service;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.mxGraph.entity.MxCell;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class IMxCellServiceTest extends ApplicationTests {

    @Autowired
    private IMxCellService mxCellServiceImpl;

    Logger logger = LoggerUtil.getLogger();

    @Test
    public void testGetMeCellById() {
        MxCell meCell = mxCellServiceImpl.getMeCellById("0bb7410706404b4e9bfd96159e58a713");
        logger.info(meCell.toString());
    }

}
