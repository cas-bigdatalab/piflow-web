package com.nature.third.service;

import com.nature.common.Eunm.RunModeType;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessGroup;
import com.nature.third.vo.flow.ThirdProgressVo;

import java.util.Map;

public interface IFlow {

    /**
     * startFlow
     *
     * @param process
     * @return
     */
    public Map<String,Object> startFlow(Process process, String checkpoint, RunModeType runModeType);

    /**
     * stopFlow
     *
     * @param appId
     * @return
     */
    public String stopFlow(String appId);

    /**
     * getFlowProgress
     * @param appId
     * @return
     */
    public ThirdProgressVo getFlowProgress(String appId);

    /**
     * getFlowLog
     *
     * @param appId
     * @return
     */
    public String getFlowLog(String appId);

    /**
     * getCheckpoints
     *
     * @param appID
     * @return
     */
    public String getCheckpoints(String appID);

    public String getDebugData(String appID,String stopName,String portName);

}
