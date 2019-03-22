package com.nature.third.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.common.constant.SysParamsCache;
import com.nature.third.inf.IGetStopInfo;

@Component
public class GetStopInfoImpl implements IGetStopInfo {

    Logger logger = LoggerUtil.getLogger();

    @Override
    public void getStopInfo(String bundle) {
        if (StringUtils.isNotBlank(bundle)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("bundle", bundle);
            String sendGetData = HttpUtils.doGet(SysParamsCache.STOP_INFO_URL(), map, 30 * 1000);
            logger.info("返回信息：" + sendGetData);
        }
    }
}
