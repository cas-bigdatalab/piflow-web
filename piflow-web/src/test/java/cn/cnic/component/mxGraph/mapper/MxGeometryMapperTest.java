package cn.cnic.component.mxGraph.mapper;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.mxGraph.entity.MxGeometry;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.Date;


public class MxGeometryMapperTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private MxGeometryMapper mxGeometryMapper;

    @Test
    public void testGetMxGeometryById() {
        MxGeometry mxGeometryById = mxGeometryMapper.getMxGeometryById("fadb97a91beb43c9a0d74023c32e249d");
        logger.info(mxGeometryById.toString());
    }

    @Test
    @Rollback(true)
    public void testAddMxGeometry() {
        MxGeometry mxGeometry = new MxGeometry();
        mxGeometry.setId(UUIDUtils.getUUID32());
        mxGeometry.setCrtDttm(new Date());
        mxGeometry.setCrtUser("Nature");
        mxGeometry.setEnableFlag(true);
        mxGeometry.setLastUpdateUser("Nature");
        mxGeometry.setLastUpdateDttm(new Date());
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
