package com.nature.component.workFlow.utils;

import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class MxGraphModelUtil {
    /**
     * mxGraphModel实体转Vo
     *
     * @param mxGraphModel
     * @return
     */
    public static MxGraphModelVo mxGraphModelPoToVo(MxGraphModel mxGraphModel){
        MxGraphModelVo mxGraphModelVo = null;
        if(null!=mxGraphModel){
            mxGraphModelVo = new MxGraphModelVo();
            BeanUtils.copyProperties(mxGraphModel, mxGraphModelVo);
            List<MxCell> root = mxGraphModel.getRoot();
            mxGraphModelVo.setRootVo(mxCellVoListPoToVo(root));
        }
        return mxGraphModelVo;
    }

    /**
     * mxCellList实体转Vo
     *
     * @param mxCellList
     * @return
     */
    public static List<MxCellVo> mxCellVoListPoToVo(List<MxCell> mxCellList){
        List<MxCellVo> mxCellVoList = null;
        if(null!=mxCellList&&mxCellList.size()>0){
            mxCellVoList = new ArrayList<MxCellVo>();
            for (MxCell mxCell:mxCellList) {
                MxCellVo mxCellVo = mxCellPoToVo(mxCell);
                if(null != mxCell){
                    mxCellVoList.add(mxCellVo);
                }
            }
        }
        return mxCellVoList;
    }

    /**
     * mxCell实体转Vo
     *
     * @param mxCell
     * @return
     */
    public static MxCellVo mxCellPoToVo(MxCell mxCell){
        MxCellVo mxCellVo = null;
        if(null!=mxCell){
            mxCellVo = new MxCellVo();
            BeanUtils.copyProperties(mxCell, mxCellVo);
            MxGeometry mxGeometry = mxCell.getMxGeometry();
            if(null!=mxGeometry){
                MxGeometryVo mxGeometryVo = new MxGeometryVo();
                BeanUtils.copyProperties(mxGeometry, mxGeometryVo);
                mxCellVo.setMxGeometryVo(mxGeometryVo);
            }
        }
        return mxCellVo;
    }
}
