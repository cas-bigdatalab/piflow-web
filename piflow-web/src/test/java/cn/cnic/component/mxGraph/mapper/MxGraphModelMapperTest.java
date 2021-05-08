package cn.cnic.component.mxGraph.mapper;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

public class MxGraphModelMapperTest extends ApplicationTests {

    @Autowired
    private MxGraphModelMapper mxGraphModelMapper;

    Logger logger = LoggerUtil.getLogger();

    @Test
    public void testGetMeCellByMxGraphId() {
        MxGraphModel mxGraphModelById = mxGraphModelMapper.getMxGraphModelById("0bb7410706404b4e9bfd96159e58a713");
        logger.info(mxGraphModelById.toString());
    }

    @Test
    @Rollback(true)
    public void testAddMxGraphModel() {
        MxGraphModel mxGraphModel = new MxGraphModel();
        mxGraphModel.setId(UUIDUtils.getUUID32());
        mxGraphModel.setCrtDttm(new Date());
        mxGraphModel.setCrtUser("Nature");
        mxGraphModel.setEnableFlag(true);
        mxGraphModel.setLastUpdateUser("Nature");
        mxGraphModel.setLastUpdateDttm(new Date());
        mxGraphModel.setDx("dx");
        mxGraphModel.setDy("dy");
        mxGraphModel.setGrid("grid");
        mxGraphModel.setGridSize("gridSize");
        mxGraphModel.setGuides("guides");
        mxGraphModel.setTooltips("tooltips");
        mxGraphModel.setConnect("connect");
        mxGraphModel.setArrows("arrows");
        mxGraphModel.setFold("fold");
        mxGraphModel.setPage("page");
        mxGraphModel.setPageScale("pageScale");
        mxGraphModel.setPageWidth("pageWidth");
        mxGraphModel.setPageHeight("pageHeight");
        mxGraphModel.setBackground("background");
        int addFlow = mxGraphModelMapper.addMxGraphModel(mxGraphModel);
        logger.info(addFlow + "");
    }

}
