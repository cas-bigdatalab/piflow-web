package com.nature.component.workFlow.service.impl;

import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.service.IStopsService;
import com.nature.component.workFlow.utils.StopsUtil;
import com.nature.component.workFlow.vo.StopsVo;
import com.nature.mapper.StopsMapper;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo2;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Override
    public Integer stopsUpdate(StopsVo stopsVo) {
            if (null != stopsVo) {
            Stops stopsById = stopsMapper.getStopsById(stopsVo.getId());
            if (null != stopsById) {
                BeanUtils.copyProperties(stopsVo, stopsById);
                stopsById.setVersion(stopsById.getVersion() + 1);
                stopsById.setLastUpdateDttm(new Date());
                stopsById.setLastUpdateUser("-1");
                int i = stopsMapper.updateStops(stopsById);
                return i;
            }
        }
        return 0;
    }

	@Override
	public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo2 stopVo) {
		return stopsMapper.updateStopsByFlowIdAndName(stopVo);
	}

}
