package cn.cnic.component.mxGraph.domain;

import cn.cnic.component.mxGraph.entity.MxGeometry;
import cn.cnic.component.mxGraph.mapper.MxGeometryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class MxGeometryDomain {

    private final MxGeometryMapper mxGeometryMapper;

    @Autowired
    public MxGeometryDomain(MxGeometryMapper mxGeometryMapper) {
        this.mxGeometryMapper = mxGeometryMapper;
    }

    public int updateMxGeometry(MxGeometry mxGeometry) {
        if (null == mxGeometry) {
            return 0;
        }
        return mxGeometryMapper.updateMxGeometry(mxGeometry);
    }

    public int updateMxGeometryEnableFlagById(String username, String id) {
        return mxGeometryMapper.updateEnableFlagById(username, id);
    }

}
