package com.nature.component.mxGraph.service.impl;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.StatefulRtnBaseUtils;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.service.IMxGraphModelService;
import com.nature.mapper.mxGraph.MxCellMapper;
import com.nature.mapper.mxGraph.MxGeometryMapper;
import com.nature.mapper.mxGraph.MxGraphModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MxGraphModelServiceImpl implements IMxGraphModelService {

	Logger logger = LoggerUtil.getLogger();

	@Autowired
	private MxGraphModelMapper mxGraphModelMapper;

	@Autowired
	private MxCellMapper mxCellMapper;

	@Autowired
	private MxGeometryMapper mxGeometryMapper;

	@Override
	public StatefulRtnBase addMxGraphModel(MxGraphModel mxGraphModel) {
		StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
		int addMxGraphModel = mxGraphModelMapper.addMxGraphModel(mxGraphModel);
		if (addMxGraphModel > 0) {
			List<MxCell> mxCellList = mxGraphModel.getRoot();
			for (MxCell mxCell : mxCellList) {
				MxGeometry mxGeometry = mxCell.getMxGeometry();
				if (null != mxGeometry) {
					int addMxGeometry = mxGeometryMapper.addMxGeometry(mxGeometry);
					if (addMxGeometry > 0) {
						mxCell.setMxGraphModel(mxGraphModel);
						mxCell.setMxGeometry(mxGeometry);
						int addMxCell = mxCellMapper.addMxCell(mxCell);
						if (addMxCell <= 0) {
							statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("MxCell save failed");
						}
					} else {
						statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("MxGraphModelAttributes");
					}
				}
			}
		} else {
			statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("MxGraphModel save failed");
		}
		return statefulRtnBase;
	}

	@Override
	public int deleteMxGraphModelById(String id) {
		return mxGraphModelMapper.updateEnableFlagByFlowId(id);
	}
}
