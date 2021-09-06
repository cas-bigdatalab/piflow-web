package cn.cnic.component.flow.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
    
    public static String [] globalParamsToIds(List<FlowGlobalParams> flowGlobalParamsList) {
        if (null == flowGlobalParamsList || flowGlobalParamsList.size() <= 0) {
        	return null;
        }
        List<String> globalParamsIdsList = new ArrayList<>();
    	for (int i = 0; i < flowGlobalParamsList.size(); i++) {
    		FlowGlobalParams flowGlobalParams = flowGlobalParamsList.get(i);
    		if (null == flowGlobalParams || StringUtils.isBlank(flowGlobalParams.getId())) {
    			continue;
    		}
    		globalParamsIdsList.add(flowGlobalParams.getId());
		}
    	String[] globalParamsIds = globalParamsIdsList.toArray(new String[globalParamsIdsList.size()]);;
    	return globalParamsIds;
	}
    
    public static List<FlowGlobalParams> globalParamsIdToGlobalParams(String [] globalParamsIds) {
        if (null == globalParamsIds || globalParamsIds.length <= 0) {
        	return null;
        }
        List<FlowGlobalParams> flowGlobalParamsList = new ArrayList<>();
        FlowGlobalParams flowGlobalParams;
        for (String id : globalParamsIds) {
        	if (StringUtils.isBlank(id)) {
        		continue;
        	}
        	flowGlobalParams = new FlowGlobalParams();
        	flowGlobalParams.setId(id);
        	flowGlobalParamsList.add(flowGlobalParams);
		}
    	return flowGlobalParamsList;
	}
}
