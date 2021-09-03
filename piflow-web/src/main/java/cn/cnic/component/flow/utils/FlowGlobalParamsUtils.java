package cn.cnic.component.flow.utils;

import java.util.Date;

import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.flow.entity.FlowGlobalParams;

public class FlowGlobalParamsUtils {

    public static FlowGlobalParams setFlowGlobalParamsBasicInformation(FlowGlobalParams globalParams, boolean isSetId, String username) {
        if (null == globalParams) {
        	globalParams = new FlowGlobalParams();
        }
        if (isSetId) {
        	globalParams.setId(UUIDUtils.getUUID32());
        }
        // set MxGraphModel basic information
        globalParams.setCrtDttm(new Date());
        globalParams.setCrtUser(username);
        globalParams.setLastUpdateDttm(new Date());
        globalParams.setLastUpdateUser(username);
        globalParams.setVersion(0L);
        return globalParams;
    }
}
