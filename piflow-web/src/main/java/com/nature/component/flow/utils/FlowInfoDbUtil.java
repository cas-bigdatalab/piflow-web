package com.nature.component.flow.utils;

import com.nature.component.flow.model.FlowInfoDb;
import com.nature.component.flow.vo.FlowInfoDbVo;
import org.springframework.beans.BeanUtils;

public class FlowInfoDbUtil {
    /**
     * flowInfoDb Po To Vo
     *
     * @param flowInfoDb
     * @return
     */
    public static FlowInfoDbVo flowInfoDbToVo(FlowInfoDb flowInfoDb) {
        FlowInfoDbVo flowInfoDbVo = null;
        if (null != flowInfoDb) {
            flowInfoDbVo = new FlowInfoDbVo();
            BeanUtils.copyProperties(flowInfoDb, flowInfoDbVo);
        }
        return flowInfoDbVo;
    }
}
