package cn.cnic.component.mxGraph.domain;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.mxGraph.entity.MxNodeImage;
import cn.cnic.component.mxGraph.mapper.MxNodeImageMapper;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class MxNodeImageDomain {

    private final MxNodeImageMapper mxNodeImageMapper;

    @Autowired
    public MxNodeImageDomain(MxNodeImageMapper mxNodeImageMapper) {
        this.mxNodeImageMapper = mxNodeImageMapper;
    }

    public int addMxNodeImage(MxNodeImage mxNodeImage) throws Exception {
        if (null == mxNodeImage) {
            return 0;
        }
        if (StringUtils.isBlank(mxNodeImage.getId())) {
            mxNodeImage.setId(UUIDUtils.getUUID32());
        }
        int addMxNodeImage = mxNodeImageMapper.addMxNodeImage(mxNodeImage);
        if (addMxNodeImage <= 0) {
            throw new Exception("save failed");
        }
        return addMxNodeImage;
    }

    public List<MxNodeImage> userGetMxNodeImageListByImageType(String username, String imageType) {
        return mxNodeImageMapper.userGetMxNodeImageListByImageType(username, imageType);
    }

}
