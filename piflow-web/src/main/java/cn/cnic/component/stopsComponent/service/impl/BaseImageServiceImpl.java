package cn.cnic.component.stopsComponent.service.impl;

import cn.cnic.base.utils.*;

import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.stopsComponent.domain.BaseImageInfoDomain;
import cn.cnic.component.stopsComponent.entity.BaseImageInfo;
import cn.cnic.component.stopsComponent.service.IBaseImageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.CipherInputStream;

@Log4j
@Service
public class BaseImageServiceImpl implements IBaseImageService {

    private final BaseImageInfoDomain baseImageInfoDomain;

    @Autowired
    public BaseImageServiceImpl(BaseImageInfoDomain baseImageInfoDomain) {
        this.baseImageInfoDomain = baseImageInfoDomain;
    }

    @Override
    public String uploadBaseImage(String username, String imageName, String imageVersion, String description, String harborUser, String harborPassword) {

        try {
            // 判断是否已存在
            List<BaseImageInfo> infos = baseImageInfoDomain.getBaseImageInfoListByName(imageName);
            if (!infos.isEmpty()) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("上传基础镜像失败，" + imageName + "已存在！");
            }

            // 创建新的 BaseImageInfo 实例
            BaseImageInfo info = BaseImageInfo.builder()
                    .baseImageName(imageName)
                    .baseImageVersion(imageVersion)
                    .baseImageDescription(description)
                    .harborUser(harborUser)
                    .harborPassword(harborPassword)
                    .crtUser(username)
                    .build();

            // 添加基础镜像信息
            baseImageInfoDomain.addBaseImageInfo(info);
        } catch (Exception e) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(e.getMessage());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("上传基础镜像" + imageName + "完成！");
    }

    @Override
    public String updateBaseImage(String username, String imageName, String imageVersion, String description, String harborUser, String harborPassword) {

        try {
            // 创建新的 BaseImageInfo 实例
            BaseImageInfo info = BaseImageInfo.builder()
                    .baseImageName(imageName)
                    .baseImageVersion(imageVersion)
                    .baseImageDescription(description)
                    .harborUser(harborUser)
                    .harborPassword(harborPassword)
                    .crtUser(username)
                    .build();

            // 更新基础镜像信息
            baseImageInfoDomain.updateBaseImageInfo(info);
        } catch (Exception e) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(e.getMessage());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("更新基础镜像" + imageName + "完成！");
    }

    @Override
    public String deleteBaseImage(String username, String imageName) {
        try {
            // 删除基础镜像信息
            baseImageInfoDomain.deleteBaseImageInfo(username, imageName);
        } catch (Exception e) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(e.getMessage());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("删除基础镜像" + imageName + "完成！");
    }

    @Override
    public String getBaseImageInfoByPage(Integer page, Integer limit) {
        if (null == page || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        Page<BaseImageInfo> page0 = PageHelper.startPage(page, limit);
        baseImageInfoDomain.getBaseImageInfoList();
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        rtnMap = PageHelperUtils.setLayTableParam(page0, rtnMap);
        return JsonUtils.toJsonNoException(rtnMap);
    }
}

