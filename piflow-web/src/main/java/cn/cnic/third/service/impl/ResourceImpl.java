package cn.cnic.third.service.impl;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.third.service.IResource;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class ResourceImpl implements IResource {

    private Logger logger = LoggerUtil.getLogger();

    @Override
    public String getResourceInfo() {

        Map<String, String> map = new HashMap<>();
        String sendGetData = HttpUtils.doGet(SysParamsCache.getResourceInfoUrl(), map, 30 * 1000);
        logger.info("return msg：" + sendGetData);
        if (StringUtils.isBlank(sendGetData)) {
            logger.warn("Interface return value is null");
            return null;
        }
        if (sendGetData.contains("Error") || sendGetData.contains(HttpUtils.INTERFACE_CALL_ERROR)) {
            logger.warn("return err : " + sendGetData);
            return null;
        }

        return sendGetData;
    }
}
