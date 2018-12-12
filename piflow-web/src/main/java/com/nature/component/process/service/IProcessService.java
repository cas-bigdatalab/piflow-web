package com.nature.component.process.service;

import com.nature.base.vo.StatefulRtnBase;
import com.nature.component.process.model.Process;
import com.nature.component.process.vo.ProcessVo;
import com.nature.component.workFlow.model.Flow;
import org.springframework.data.annotation.Transient;

import java.util.List;

public interface IProcessService {

    /**
     * 查询processVoList(查询包含其子表)
     *
     * @return
     */
    @Transient
    public List<ProcessVo> getProcessAllVoList();
    /**
     * 查询processVoList(只查询process表)
     *
     * @return
     */
    @Transient
    public List<ProcessVo> getProcessVoList();

    /**
     * 查询processVo根据id(查询包含其子表)
     *
     * @param id
     * @return
     */
    @Transient
    public ProcessVo getProcessAllVoById(String id);

    /**
     * 查询processVo根据id(只查询process表)
     *
     * @param id
     * @return
     */
    @Transient
    public ProcessVo getProcessVoById(String id);

    /**
     * 查询process根据id
     *
     * @param id
     * @return
     */
    @Transient
    public Process getProcessById(String id);

    /**
     * 根据Appid查询process
     *
     * @param appId
     * @return
     */
    public ProcessVo getProcessVoByAppId(String appId);

    /**
     * 根据数组Appid查询process
     *
     * @param appIDs
     * @return
     */
    public List<ProcessVo> getProcessVoByAppIds(String appIDs);

    /**
     * 根据appID在第三方接口查询appInfo并保存
     *
     * @param appID
     * @return
     */
    public ProcessVo getAppInfoByThirdAndSave(String appID);

    /**
     * 根据appID在第三方接口查询progress并保存
     *
     * @param appIDs
     * @return
     */
    public List<ProcessVo> getProgressByThirdAndSave(String[] appIDs);

    /**
     * 修改process
     *
     * @param processVo
     * @return
     */
    public int updateProcess(ProcessVo processVo);

    /**
     * 调用start接口并保存返回信息
     *
     * @param flow
     * @return
     */
    public Process startFlowAndUpdateProcess(Flow flow);

    /**
     * 拷贝 process并新建
     *
     * @param processId
     * @return
     */
    public Process processCopyProcessAndAdd(String processId);

    /**
     * 根据flowId生成Process并保存
     *
     * @param flowId
     * @return
     */
    @Transient
    public Process flowToProcessAndSave(String flowId);

    /**
     * 逻辑删除
     *
     * @param processId
     * @return
     */
    @Transient
    public StatefulRtnBase updateProcessEnableFlag(String processId);
}
