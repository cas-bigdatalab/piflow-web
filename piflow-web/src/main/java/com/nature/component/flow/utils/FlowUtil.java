package com.nature.component.flow.utils;

import com.nature.component.flow.model.CustomizedProperty;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.flow.vo.*;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class FlowUtil {
    /**
     * stopsList Po To Vo
     *
     * @param flowList
     * @return
     */
    public static List<FlowVo> flowListPoToVo(List<Flow> flowList) {
        List<FlowVo> flowVoList = null;
        if (null != flowList && flowList.size() > 0) {
            flowVoList = new ArrayList<>();
            for (Flow flow : flowList) {
                FlowVo flowVo = flowPoToVo(flow);
                if (null != flowVo) {
                    flowVoList.add(flowVo);
                }
            }
        }
        return flowVoList;
    }

    /**
     * stop Po To Vo
     *
     * @param flow
     * @return
     */
    public static FlowVo flowPoToVo(Flow flow) {
        FlowVo flowVo = null;
        if (null != flow) {
            flowVo = new FlowVo();
            BeanUtils.copyProperties(flow, flowVo);
            List<StopsVo> stopsVoList = StopsUtil.stopsListPoToVo(flow.getStopsList());
            List<PathsVo> pathsVoList = PathsUtil.pathsListPoToVo(flow.getPathsList());
            flowVo.setStopsVoList(stopsVoList);
            flowVo.setPathsVoList(pathsVoList);
        }
        return flowVo;
    }
}
