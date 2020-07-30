package cn.cnic.component.mxGraph.utils;

import cn.cnic.component.mxGraph.model.MxCell;
import cn.cnic.component.mxGraph.model.MxGeometry;
import cn.cnic.component.mxGraph.model.MxGraphModel;
import cn.cnic.component.mxGraph.vo.MxCellVo;
import cn.cnic.component.mxGraph.vo.MxGeometryVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MxCellUtils {

    public static MxCell mxCellNewNoId(String username) {

        MxCell mxCell = new MxCell();
        // basic properties (required when creating)
        mxCell.setCrtDttm(new Date());
        mxCell.setCrtUser(username);
        // basic properties
        mxCell.setEnableFlag(true);
        mxCell.setLastUpdateUser(username);
        mxCell.setLastUpdateDttm(new Date());
        mxCell.setVersion(0L);
        return mxCell;
    }

    public static MxCell initMxCellBasicPropertiesNoId(MxCell mxCell, String username) {
        if (null == mxCell) {
            return mxCellNewNoId(username);
        }
        // basic properties (required when creating)
        mxCell.setCrtDttm(new Date());
        mxCell.setCrtUser(username);
        // basic properties
        mxCell.setEnableFlag(true);
        mxCell.setLastUpdateUser(username);
        mxCell.setLastUpdateDttm(new Date());
        mxCell.setVersion(0L);
        return mxCell;
    }

    public static List<MxCell> initMxCell(String username, MxGraphModel mxGraphModel) {
        if (StringUtils.isBlank(username) && null == mxGraphModel) {
            return null;
        }
        List<MxCell> pMxCellList = new ArrayList<>();
        MxCell mxCell0 = mxCellNewNoId(username);
        mxCell0.setPageId("0");
        mxCell0.setMxGraphModel(mxGraphModel);
        pMxCellList.add(mxCell0);
        MxCell mxCell1 = mxCellNewNoId(username);
        mxCell1.setParent("0");
        mxCell1.setPageId("1");
        mxCell1.setMxGraphModel(mxGraphModel);
        pMxCellList.add(mxCell1);
        return pMxCellList;
    }

    public static MxCellVo initIconMxCellVo(MxCellVo mxCellVo) {
        if (null == mxCellVo) {
            return new MxCellVo();
        }
        MxCellVo iconMxCellVo = new MxCellVo();
        BeanUtils.copyProperties(mxCellVo, iconMxCellVo);
        iconMxCellVo.setPageId(mxCellVo.getPageId() + ".1");
        iconMxCellVo.setValue(mxCellVo.getPageId());
        iconMxCellVo.setStyle("text;html=1;align=center;verticalAlign=middle;resizable=0;points=[];autosize=1;fontFamily=PiFlow;fontColor=#FFFFFF;fontSize=1;");
        MxGeometryVo iconMxGeometryVo = new MxGeometryVo();
        BeanUtils.copyProperties(mxCellVo.getMxGeometryVo(), iconMxGeometryVo);
        double load_x = Double.parseDouble(iconMxGeometryVo.getX());
        double load_width = Double.parseDouble(iconMxGeometryVo.getWidth());
        iconMxGeometryVo.setHeight("0");
        iconMxGeometryVo.setWidth("0");
        iconMxGeometryVo.setX((load_x + load_width) + "");
        iconMxCellVo.setMxGeometryVo(iconMxGeometryVo);
        return iconMxCellVo;
    }

    public static MxCell initIconMxCell(MxCell mxCell) {
        if (null == mxCell) {
            return new MxCell();
        }
        MxCell iconMxCell = new MxCell();
        BeanUtils.copyProperties(mxCell, iconMxCell);
        iconMxCell.setPageId(mxCell.getPageId() + ".1");
        iconMxCell.setValue(mxCell.getPageId());
        iconMxCell.setStyle("text;html=1;align=center;verticalAlign=middle;resizable=0;points=[];autosize=1;fontFamily=PiFlow;fontColor=#FFFFFF;fontSize=1;");
        MxGeometry iconMxGeometry = new MxGeometry();
        BeanUtils.copyProperties(mxCell.getMxGeometry(), iconMxGeometry);
        double load_x = Double.parseDouble(iconMxGeometry.getX());
        double load_width = Double.parseDouble(iconMxGeometry.getWidth());
        iconMxGeometry.setHeight("0");
        iconMxGeometry.setWidth("0");
        iconMxGeometry.setX((load_x + load_width) + "");
        iconMxCell.setMxGeometry(iconMxGeometry);
        return iconMxCell;
    }

    public static MxCell AddDefaultFlowMxCell(String username, String pageId, String name) {
        MxCell defaultFlowMxCell = mxCellNewNoId(username);
        defaultFlowMxCell.setPageId(pageId);
        defaultFlowMxCell.setParent("1");
        defaultFlowMxCell.setStyle("image;html=1;labelBackgroundColor=#ffffff00;image=/piflow-web/img/flow.png");
        defaultFlowMxCell.setValue(name);
        defaultFlowMxCell.setVertex("1");
        MxGeometry defaultFlowMxCellMxGeometry = MxGeometryUtils.mxGeometryNewNoId(username);
        defaultFlowMxCellMxGeometry.setAs("geometry");
        defaultFlowMxCellMxGeometry.setHeight("66");
        defaultFlowMxCellMxGeometry.setWidth("66");
        defaultFlowMxCellMxGeometry.setX("100");
        defaultFlowMxCellMxGeometry.setY("100");
        defaultFlowMxCellMxGeometry.setMxCell(defaultFlowMxCell);
        defaultFlowMxCell.setMxGeometry(defaultFlowMxCellMxGeometry);
        return defaultFlowMxCell;
    }
}
