package com.nature.component.workFlow.service.impl;

import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.service.IStopsService;
import com.nature.component.workFlow.utils.StopsUtil;
import com.nature.component.workFlow.vo.StopsVo;
import com.nature.mapper.StopsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopsServiceImpl implements IStopsService {

    @Autowired
    private StopsMapper stopsMapper;

    @Override
    public int deleteStopsByFlowId(String id) {
        return stopsMapper.deleteStopsByFlowId(id);
    }

    /**
     * 根据flowId和pagesId查询stops
     *
     * @param flowId  必填
     * @param pageIds 可为空
     * @return
     */
    @Override
    public List<StopsVo> getStopsByFlowIdAndPageIds(String flowId, String[] pageIds) {
        List<Stops> stopsList = stopsMapper.getStopsListByFlowIdAndPageIds(flowId, pageIds);
        List<StopsVo> stopsVoList = StopsUtil.stopsListPoToVo(stopsList);
        return stopsVoList;
    }

}
