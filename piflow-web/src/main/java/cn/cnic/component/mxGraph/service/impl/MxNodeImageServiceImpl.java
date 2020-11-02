package cn.cnic.component.mxGraph.service.impl;

import cn.cnic.base.util.FileUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.mxGraph.entity.MxNodeImage;
import cn.cnic.component.mxGraph.service.IMxNodeImageService;
import cn.cnic.component.mxGraph.utils.MxNodeImageUtils;
import cn.cnic.component.mxGraph.vo.MxNodeImageVo;
import cn.cnic.component.mxGraph.jpa.domain.MxNodeImageDomain;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MxNodeImageServiceImpl implements IMxNodeImageService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private MxNodeImageDomain mxNodeImageDomain;

    @Override
    public String uploadNodeImage(String username,MultipartFile file, String imageType) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(imageType)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("imageType is null");
        }
        if (file.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
        }
        Map<String, Object> uploadMap = FileUtils.uploadRtnMap(file, SysParamsCache.IMAGES_PATH, null);
        if (null == uploadMap || uploadMap.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
        }
        Integer code = (Integer) uploadMap.get("code");
        if (500 == code) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed to upload file");
        }
        String saveFileName = (String) uploadMap.get("saveFileName");
        String fileName = (String) uploadMap.get("fileName");
        String path = (String) uploadMap.get("path");
        MxNodeImage mxNodeImage = MxNodeImageUtils.newMxNodeImageNoId(username);
        mxNodeImage.setId(UUIDUtils.getUUID32());
        mxNodeImage.setImageName(fileName);
        mxNodeImage.setImagePath(path);
        mxNodeImage.setImageUrl("/images/" + saveFileName);
        mxNodeImage.setImageType(imageType);
        mxNodeImageDomain.saveOrUpdate(mxNodeImage);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("imgUrl", mxNodeImage.getImageUrl());
    }

    @Override
    public String getMxNodeImageList(String username,String imageType) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(imageType)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("imageType is null");
        }
        List<MxNodeImageVo> mxNodeImageVoList = new ArrayList<>();
        List<MxNodeImage> mxNodeImageList = mxNodeImageDomain.userGetMxNodeImageListByImageType(username, imageType);
        if (null == mxNodeImageList || mxNodeImageList.size() <= 0) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("nodeImageList", mxNodeImageList);
        }
        MxNodeImageVo mxNodeImageVo;
        for (MxNodeImage mxNodeImage : mxNodeImageList) {
            if (null == mxNodeImage) {
                continue;
            }
            mxNodeImageVo = new MxNodeImageVo();
            BeanUtils.copyProperties(mxNodeImage, mxNodeImageVo);
            mxNodeImageVo.setImageUrl(SysParamsCache.SYS_CONTEXT_PATH + mxNodeImage.getImageUrl());
            mxNodeImageVoList.add(mxNodeImageVo);
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("nodeImageList", mxNodeImageVoList);
    }
}
