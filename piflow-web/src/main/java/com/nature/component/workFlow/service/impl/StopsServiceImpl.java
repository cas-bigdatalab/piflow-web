package com.nature.component.workFlow.service.impl;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.StatefulRtnBaseUtils;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.service.IStopsService;
import com.nature.component.workFlow.utils.StopsUtil;
import com.nature.component.workFlow.vo.StopsVo;
import com.nature.mapper.FlowMapper;
import com.nature.mapper.StopsMapper;
import com.nature.mapper.mxGraph.MxCellMapper;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StopsServiceImpl implements IStopsService {

    @Autowired
    private StopsMapper stopsMapper;
    
    @Autowired
    private FlowMapper flowMapper;
    
    @Autowired
    private MxCellMapper mxCellMapper;
    
    Logger logger = LoggerUtil.getLogger();

    @Override
    public int deleteStopsByFlowId(String id) {
        return stopsMapper.updateEnableFlagByFlowId(id);
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
                stopsById.setLastUpdateDttm(new Date());
                stopsById.setLastUpdateUser("-1");
                int i = stopsMapper.updateStops(stopsById);
                return i;
            }
        }
        return 0;
    }

    @Override
    public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo stopVo) {
        return stopsMapper.updateStopsByFlowIdAndName(stopVo);
    }

    /**
     * 修改isCheckpoint字段
     *
     * @param stopId
     * @param isCheckpoint
     * @return
     */
    @Override
    public int updateStopsCheckpoint(String stopId, boolean isCheckpoint) {
    	 User user = SessionUserUtil.getCurrentUser();
         String username = (null != user) ? user.getUsername() : "-1";
        if (StringUtils.isNotBlank(stopId)) {
            Stops stopsById = stopsMapper.getStopsById(stopId);
            if(null!=stopsById){
                stopsById.setLastUpdateUser(username);
                stopsById.setLastUpdateDttm(new Date());
                stopsById.setCheckpoint(isCheckpoint);
                return stopsMapper.updateStops(stopsById);
            }
        }
        return 0;
    }

	@Override
	public int updateStopsNameById(String id, String stopName) {
		 User user = SessionUserUtil.getCurrentUser();
         String username = (null != user) ? user.getUsername() : "-1";
          if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(stopName)) {
              Stops stopsById = stopsMapper.getStopsById(id);
              if(null!=stopsById){
                  stopsById.setLastUpdateUser(username);
                  stopsById.setLastUpdateDttm(new Date());
                  stopsById.setName(stopName);
                  return stopsMapper.updateStops(stopsById);
              }
          }
        return 0;
	}

	@Override
	public String getStopByNameAndFlowId(String flowId, String stopName) {
		return stopsMapper.getStopByNameAndFlowId(flowId, stopName);
	}

	@SuppressWarnings("null")
	@Override
	public StatefulRtnBase updateStopName(String stopId,Flow flowById,String stopName, String pageId) {
		 User user = SessionUserUtil.getCurrentUser();
         String username = (null != user) ? user.getUsername() : "-1";
		StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
    	List<MxCell> root = null;
    	if (null != flowById) {
    		MxGraphModel mxGraphModel = flowById.getMxGraphModel();
    		if (null  != mxGraphModel) {
    			root = mxGraphModel.getRoot();
			}
    	}
    	if (null == root && root.size() == 0) {
        	statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("No flow information,update failed ");
        	logger.info(flowById.getId()+"画板信息为空,更新失败");
        	return statefulRtnBase;
		}
    	//校验name是否重名
    	String checkResult = this.getStopByNameAndFlowId(flowById.getId(), stopName);
        if(StringUtils.isNotBlank(checkResult)){
        	statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("Name already exists");
            logger.info(stopName+"名称已经重复,保存失败");
        }else {
        	int updateStopsNameById = this.updateStopsNameById(stopId, stopName);
        	 if(updateStopsNameById > 0){
        		 for (MxCell mxCell : root) {
        			 if (null != mxCell) {
        				 if (mxCell.getPageId().equals(pageId)) {
     						mxCell.setValue(stopName);
     						mxCell.setLastUpdateDttm(new Date());
     						mxCell.setLastUpdateUser(username);
     						int updateMxCell = mxCellMapper.updateMxCell(mxCell);
     						if (updateMxCell > 0) {
     							logger.info("修改成功");
     							statefulRtnBase = StatefulRtnBaseUtils.setSuccessdMsg("Update success");
     						}
     					}
					}
				}
             }else {
            	 statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("Update failed");
                 logger.info("修改stopname失败");
			}
        }
		return statefulRtnBase;
	}

}
