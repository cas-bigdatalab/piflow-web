package cn.cnic.component.mxGraph.service.impl;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.service.IMxCellService;
import cn.cnic.component.mxGraph.mapper.MxCellMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MxCellServiceImpl implements IMxCellService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private MxCellMapper mxCellMapper;

    @Override
    public int deleteMxCellById(String username,String id) {
        return mxCellMapper.updateEnableFlagById(username,id);
    }

    @Override
    public MxCell getMeCellById(String id) {
        MxCell meCellById = mxCellMapper.getMeCellById(id);
        return meCellById;
    }

}
