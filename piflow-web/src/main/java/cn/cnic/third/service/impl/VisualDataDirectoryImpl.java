package cn.cnic.third.service.impl;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.common.constant.ApiConfig;
import cn.cnic.third.service.IStop;
import cn.cnic.third.service.IVisualDataDirectory;
import cn.cnic.third.utils.ThirdInterfaceReturnMsgUtils;
import cn.cnic.third.utils.ThirdStopsComponentUtils;
import cn.cnic.third.vo.stop.StopsHubVo;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class VisualDataDirectoryImpl implements IVisualDataDirectory {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();


    @Override
    public String getVisualDataDirectoryPathUrl(String appId, String stopName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appId", appId);
        map.put("stopName", stopName);
        String sendGetData = HttpUtils.doGet(ApiConfig.getVisualDataDirectoryPathUrl(), map, 30 * 1000);
        logger.info("return msg：" + sendGetData);
        if (StringUtils.isBlank(sendGetData)) {
            logger.warn("Interface return value is null");
            return null;
        }
        if (sendGetData.contains("Error") || sendGetData.contains(HttpUtils.INTERFACE_CALL_ERROR)) {
            logger.warn("return err: " + sendGetData);
            return null;
        }

        String sparkJarPath = JSONObject.fromObject(sendGetData).getString("visualDataDirectoryPath");
        return sparkJarPath;
    }
}
