package cn.cnic.component.mxGraph.service;

import cn.cnic.component.mxGraph.entity.MxCell;

public interface IMxCellService {

    public int deleteMxCellById(String username, String id);

    public MxCell getMeCellById(String id);


}
