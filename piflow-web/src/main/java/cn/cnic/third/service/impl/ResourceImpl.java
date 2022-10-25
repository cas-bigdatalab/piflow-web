package cn.cnic.third.service.impl;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.common.constant.ApiConfig;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.third.service.IResource;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class ResourceImpl implements IResource {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Override
    public String getResourceInfo() {

        Map<String, String> map = new HashMap<>();
        String sendGetData = HttpUtils.doGet(ApiConfig.getResourceInfoUrl(), map, 30 * 1000);
        logger.info("return msg：" + sendGetData);
        if (StringUtils.isBlank(sendGetData)) {
            logger.warn("Interface return value is null");
            return null;
        }
        if (sendGetData.contains("Error") || sendGetData.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG())) {
            logger.warn("return err : " + sendGetData);
            return null;
        }

        return sendGetData;
    }
}
