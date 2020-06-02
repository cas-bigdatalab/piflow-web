package cn.cnic.component.mxGraph.service.impl;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.mxGraph.service.IMxGraphService;
import cn.cnic.mapper.mxGraph.MxGeometryMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MxGraphServiceImpl implements IMxGraphService {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private MxGeometryMapper mxGeometryMapper;

    @Override
    public int deleteMxGraphById(String id) {
        return mxGeometryMapper.updateEnableFlagById(id);
    }

}
