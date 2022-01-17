package cn.cnic.component.mxGraph.service.impl;

import cn.cnic.component.mxGraph.service.IMxGraphService;
import cn.cnic.component.mxGraph.mapper.MxGeometryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MxGraphServiceImpl implements IMxGraphService {

    private final MxGeometryMapper mxGeometryMapper;

    @Autowired
    public MxGraphServiceImpl(MxGeometryMapper mxGeometryMapper) {
        this.mxGeometryMapper = mxGeometryMapper;
    }

    @Override
    public int deleteMxGraphById(String username, String id) {
        return mxGeometryMapper.updateEnableFlagById(username, id);
    }

}
