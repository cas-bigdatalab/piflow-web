package cn.cnic.component.mxGraph.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnic.component.mxGraph.domain.MxCellDomain;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.service.IMxCellService;

@Service
public class MxCellServiceImpl implements IMxCellService {

    private final MxCellDomain mxCellDomain;

    @Autowired
    public MxCellServiceImpl(MxCellDomain mxCellDomain) {
        this.mxCellDomain = mxCellDomain;
    }

    @Override
    public int deleteMxCellById(String username,String id) {
        return mxCellDomain.updateEnableFlagById(username,id);
    }

    @Override
    public MxCell getMeCellById(String id) {
        MxCell meCellById = mxCellDomain.getMeCellById(id);
        return meCellById;
    }

}
