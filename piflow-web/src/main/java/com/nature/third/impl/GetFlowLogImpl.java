package com.nature.third.impl;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.common.constant.SysParamsCache;
import com.nature.third.inf.IGetFlowLog;
import com.nature.third.vo.flowLog.ThirdFlowLog;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetFlowLogImpl implements IGetFlowLog {

    Logger logger = LoggerUtil.getLogger();

    /**
     * 发送 post请求
     */
    @Override
    public ThirdFlowLog getFlowLog(String appid) {
        ThirdFlowLog thirdFlowLog = null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("appID", appid);
        String doGet = HttpUtils.doGet(SysParamsCache.FLOW_LOG_URL(), map);
        if (StringUtils.isNotBlank(doGet) && !doGet.contains("Exception")) {
            logger.info("调用成功 : " + doGet);
            // 同样先将json字符串转换为json对象，再将json对象转换为java对象，如下所示。
            JSONObject obj = JSONObject.fromObject(doGet);// 将json字符串转换为json对象
            // 将json对象转换为java对象
            thirdFlowLog = (ThirdFlowLog) JSONObject.toBean(obj, ThirdFlowLog.class);
        }
        return thirdFlowLog;
    }

}
