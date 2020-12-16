package cn.cnic.third.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import cn.cnic.base.util.HttpUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.third.service.IResource;

@Service
public class ResourceImpl implements IResource {

    Logger logger = LoggerUtil.getLogger();

    @Override
    public String getResourceInfo() {

        Map<String, String> map = new HashMap<>();
        String sendGetData = HttpUtils.doGet(SysParamsCache.getResourceInfoUrl(), map, 30 * 1000);
        logger.info("return msg：" + sendGetData);
        if (StringUtils.isBlank(sendGetData)) {
            logger.warn("Interface return value is null");
            return null;
        }
        if (sendGetData.contains("Error")) {
            logger.warn("return err");
            return null;
        }

        return sendGetData;
    }
}
