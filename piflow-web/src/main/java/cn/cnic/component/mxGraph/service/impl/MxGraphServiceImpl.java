package cn.cnic.component.mxGraph.service.impl;

import cn.cnic.component.mxGraph.domain.MxGeometryDomain;
import cn.cnic.component.mxGraph.service.IMxGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MxGraphServiceImpl implements IMxGraphService {

    private final MxGeometryDomain mxGeometryDomain;

    @Autowired
    public MxGraphServiceImpl(MxGeometryDomain mxGeometryDomain) {
        this.mxGeometryDomain = mxGeometryDomain;
    }

    @Override
    public int deleteMxGraphById(String username, String id) {
        return mxGeometryDomain.updateMxGeometryEnableFlagById(username, id);
    }

}
