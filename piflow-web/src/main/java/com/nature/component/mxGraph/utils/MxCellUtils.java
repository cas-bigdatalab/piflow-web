package com.nature.component.mxGraph.utils;

import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGraphModel;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MxCellUtils {

    public static List<MxCell> initMxCell(String username, MxGraphModel mxGraphModel) {
        if (StringUtils.isBlank(username) && null == mxGraphModel) {
            return null;
        }
        List<MxCell> pMxCellList = new ArrayList<>();
        MxCell mxCell0 = new MxCell();
        mxCell0.setCrtDttm(new Date());
        mxCell0.setCrtUser(username);
        mxCell0.setEnableFlag(true);
        mxCell0.setLastUpdateDttm(new Date());
        mxCell0.setLastUpdateUser(username);
        mxCell0.setVersion(0L);
        mxCell0.setPageId("0");
        mxCell0.setMxGraphModel(mxGraphModel);
        pMxCellList.add(mxCell0);
        MxCell mxCell1 = new MxCell();
        mxCell1.setCrtDttm(new Date());
        mxCell1.setCrtUser(username);
        mxCell1.setEnableFlag(true);
        mxCell1.setLastUpdateDttm(new Date());
        mxCell1.setLastUpdateUser(username);
        mxCell1.setVersion(0L);
        mxCell1.setParent("0");
        mxCell1.setPageId("1");
        mxCell1.setMxGraphModel(mxGraphModel);
        pMxCellList.add(mxCell1);
        return pMxCellList;
    }
}
