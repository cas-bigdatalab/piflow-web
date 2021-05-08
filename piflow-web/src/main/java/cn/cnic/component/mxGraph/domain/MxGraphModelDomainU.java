package cn.cnic.component.mxGraph.domain;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGeometry;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.mapper.MxCellMapper;
import cn.cnic.component.mxGraph.mapper.MxGeometryMapper;
import cn.cnic.component.mxGraph.mapper.MxGraphModelMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class MxGraphModelDomainU {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private MxGraphModelMapper mxGraphModelMapper;

    @Resource
    private MxCellMapper mxCellMapper;

    @Resource
    private MxGeometryMapper mxGeometryMapper;


    /**
     * Add process of things
     *
     * @param mxGraphModel mxGraphModel
     * @return affected rows
     */
    public int addMxGraphModel(MxGraphModel mxGraphModel) throws Exception {
        if (null == mxGraphModel) {
            return 0;
        }
        String id = mxGraphModel.getId();
        if (StringUtils.isBlank(id)) {
            mxGraphModel.setId(UUIDUtils.getUUID32());
        }
        int addMxGraphModelCounts = mxGraphModelMapper.addMxGraphModel(mxGraphModel);
        if (addMxGraphModelCounts <= 0) {
            throw new Exception("save failed");
        }
        // save path
        // Number of save Paths
        int addMxCellCounts = 0;
        int addMxGeometryCounts = 0;
        List<MxCell> mxCellList = mxGraphModel.getRoot();
        if (null != mxCellList && mxCellList.size() > 0) {
            for (MxCell mxCell : mxCellList) {
                mxCell.setMxGraphModel(mxGraphModel);
                String mxCellId = mxCell.getId();
                if (StringUtils.isBlank(mxCellId)) {
                    mxCell.setId(UUIDUtils.getUUID32());
                }
                int addMxCell = mxCellMapper.addMxCell(mxCell);
                if (addMxCell <= 0) {
                    throw new Exception("save failed");
                }
                addMxCellCounts += addMxCell;
                MxGeometry mxGeometry = mxCell.getMxGeometry();
                if (null == mxGeometry) {
                    continue;
                }
                mxGeometry.setMxCell(mxCell);
                String mxGeometryId = mxGeometry.getId();
                if (StringUtils.isBlank(mxGeometryId)) {
                    mxGeometry.setId(UUIDUtils.getUUID32());
                }
                int addMxGeometry = mxGeometryMapper.addMxGeometry(mxGeometry);
                if (addMxGeometry <= 0) {
                    throw new Exception("save failed");
                }
                addMxGeometryCounts += addMxGeometry;
            }
        }
        int influenceCounts = (addMxGraphModelCounts + addMxCellCounts + addMxGeometryCounts);
        return influenceCounts;
    }

}
