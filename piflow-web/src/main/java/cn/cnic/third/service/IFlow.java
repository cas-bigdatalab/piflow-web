package cn.cnic.third.service;

import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.process.entity.Process;
import cn.cnic.third.vo.flow.ThirdFlowInfoVo;
import cn.cnic.third.vo.flow.ThirdProgressVo;

import java.util.Map;

public interface IFlow {

    /**
     * startFlow
     *
     * @param process
     * @return
     */
    public Map<String, Object> startFlow(Process process, String checkpoint, RunModeType runModeType);

    /**
     * stopFlow
     *
     * @param appId
     * @return
     */
    public String stopFlow(String appId);

    /**
     * getFlowProgress
     *
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

    public String getDebugData(String appID, String stopName, String portName);

    public String getVisualizationData(String appID, String stopName, String visualizationType);

    public ThirdFlowInfoVo getFlowInfo(String appid);

    public void getProcessInfoAndSave(String appid);
    
    public String getTestDataPathUrl();

}
