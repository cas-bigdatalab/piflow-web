package com.nature.component.mxGraph.service.impl;

import com.nature.base.util.LoggerUtil;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.service.IMxCellService;
import com.nature.mapper.mxGraph.MxCellMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MxCellServiceImpl implements IMxCellService {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private MxCellMapper mxCellMapper;

    @Override
    public int deleteMxCellById(String id) {
        return mxCellMapper.updateEnableFlagById(id);
    }

    @Override
    public MxCell getMeCellById(String id) {
        MxCell meCellById = mxCellMapper.getMeCellById(id);
        return meCellById;
    }

}
