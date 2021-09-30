package cn.cnic.component.process.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnic.base.utils.FileUtils;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.mapper.ProcessMapper;
import cn.cnic.component.process.mapper.ProcessStopMapper;
import cn.cnic.component.process.service.IProcessStopService;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.process.vo.ProcessStopPropertyVo;
import cn.cnic.component.process.vo.ProcessStopVo;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import cn.cnic.component.stopsComponent.mapper.StopsComponentMapper;

@Service
public class ProcessStopServiceImpl implements IProcessStopService {

	@Autowired
    private ProcessMapper processMapper;
	
    @Autowired
    private ProcessStopMapper processStopMapper;

    @Autowired
    private StopsComponentMapper stopsComponentMapper;

    /**
     * Query processStop based on processId and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    @Override
    public String getProcessStopVoByPageId(String processId, String pageId) {
        if (StringUtils.isAnyEmpty(processId, pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter passed in incorrectly");
        }
        ProcessStop processStopByPageId = processStopMapper.getProcessStopByPageIdAndPageId(processId, pageId);
        if (null == processStopByPageId) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("process stop data is null");
        }
        ProcessStopVo processStopVo = ProcessUtils.processStopPoToVo(processStopByPageId);
        StopsComponent stopsComponentByBundle = stopsComponentMapper.getStopsComponentByBundle(processStopByPageId.getBundel());
        if (null != stopsComponentByBundle) {
            processStopVo.setVisualizationType(stopsComponentByBundle.getVisualizationType());
        }
        Map<String, Object> succeededCustomParam = ReturnMapUtils.setSucceededCustomParam("processStopVo", processStopVo);
        Map<String, Object> runningDataMap = new HashMap<>();
        List<ProcessStopPropertyVo> processStopPropertyVoList = processStopVo.getProcessStopPropertyVoList();
        if (null != processStopPropertyVoList) {
        	String processAppId = processMapper.getProcessAppIdById(processId);
            for(ProcessStopPropertyVo processStopPropertyVo :processStopPropertyVoList) {
            	if (null == processStopPropertyVo) {
            		continue;
            	}
            	String customValue = processStopPropertyVo.getCustomValue();
            	if (StringUtils.equals(processStopPropertyVo.getName(), "output")) {
            		if(StringUtils.isBlank(customValue)) {
            			customValue = "/opt/mfs/galaxy/galaxy_data/" + processAppId + "_" + processStopVo.getName() + ".bed";
            		}
            		if (!FileUtils.isFileExists(customValue)) {
            			customValue = "";
            		}
            		runningDataMap.put("output", customValue);
        			continue;
            	}
            	if (StringUtils.equals(processStopPropertyVo.getName(), "input")) {
            		if(StringUtils.isBlank(customValue)) {
            			customValue = "/opt/mfs/galaxy/galaxy_data/" + processAppId + "_" + processStopVo.getName() + ".bed";
            		}
            		if (!FileUtils.isFileExists(customValue)) {
            			customValue = "";
            		}
            		runningDataMap.put("output", customValue);
        			continue;
            	}
            }
        }
        return ReturnMapUtils.appendValuesToJson(succeededCustomParam, "runningDataMap", runningDataMap);
    }
    
    
    
    
    
    
}