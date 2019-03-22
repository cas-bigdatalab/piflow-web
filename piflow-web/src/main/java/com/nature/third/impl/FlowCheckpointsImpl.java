package com.nature.third.impl;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.common.constant.SysParamsCache;
import com.nature.third.inf.IFlowCheckpoints;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FlowCheckpointsImpl implements IFlowCheckpoints {

    Logger logger = LoggerUtil.getLogger();

    /**
     * 发送 get
     */
    @SuppressWarnings("rawtypes")
    @Override
    public String getCheckpoints(String processId) {
        String jb = null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("processID", processId);
        String doGet = HttpUtils.doGet(SysParamsCache.FLOW_CHECKPOINTS_URL(), map, 5 * 1000);
        if (StringUtils.isNotBlank(doGet) && !doGet.contains("Exception")) {
            // 同样先将json字符串转换为json对象，再将json对象转换为java对象，如下所示。
            JSONObject obj = JSONObject.fromObject(doGet);// 将json字符串转换为json对象
            if (null != obj) {
                jb = obj.getString("checkpoints");
            }
        }
        return jb;
    }
}
