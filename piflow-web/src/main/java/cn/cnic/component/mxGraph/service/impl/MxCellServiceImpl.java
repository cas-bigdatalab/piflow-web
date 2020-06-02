package cn.cnic.component.mxGraph.service.impl;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.mxGraph.model.MxCell;
import cn.cnic.component.mxGraph.service.IMxCellService;
import cn.cnic.mapper.mxGraph.MxCellMapper;
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
