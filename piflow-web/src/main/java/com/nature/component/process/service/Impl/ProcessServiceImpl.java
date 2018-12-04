package com.nature.component.process.service.Impl;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.common.Eunm.ArrowDirection;
import com.nature.common.Eunm.ProcessState;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessPath;
import com.nature.component.process.model.ProcessStop;
import com.nature.component.process.model.ProcessStopProperty;
import com.nature.component.process.service.IProcessService;
import com.nature.component.process.utils.ProcessStopUtils;
import com.nature.component.process.vo.ProcessPathVo;
import com.nature.component.process.vo.ProcessStopVo;
import com.nature.component.process.vo.ProcessVo;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Paths;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.utils.MxGraphModelUtil;
import com.nature.third.inf.IGetFlowInfo;
import com.nature.third.inf.IGetFlowProgress;
import com.nature.third.inf.IStartFlow;
import com.nature.third.vo.flow.ThirdProgressVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopsVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;
import com.nature.transaction.process.ProcessStopTransaction;
import com.nature.transaction.process.ProcessTransaction;
import com.nature.transaction.workFlow.FlowTransaction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ProcessServiceImpl implements IProcessService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessTransaction processTransaction;

    @Resource
    ProcessStopTransaction processStopTransaction;

    @Resource
    private FlowTransaction flowTransaction;

    @Resource
    private IStartFlow startFlowImpl;

    @Resource
    private IGetFlowInfo getFlowInfoImpl;

    @Resource
    IGetFlowProgress getFlowProgressImpl;

    /**
     * 查询processVoList(查询包含其子表)
     *
     * @return
     */
    @Override
    public List<ProcessVo> getProcessAllVoList() {
        List<ProcessVo> processVoList = null;
        List<Process> processList = processTransaction.getProcessList();
        if (null != processList && processList.size() > 0) {
            for (Process process : processList) {
                if (null != process) {
                    ProcessVo processVo = this.processPoToVo(process);
                    processVo.setCrtDate(process.getCrtDttm());
                    processVoList.add(processVo);
                }
            }
        }
        return processVoList;
    }

    /**
     * 查询processVoList(只查询process表)
     *
     * @return
     */
    @Override
    public List<ProcessVo> getProcessVoList() {
        List<ProcessVo> processVoList = null;
        List<Process> processList = processTransaction.getProcessList();
        if (null != processList && processList.size() > 0) {
            processVoList = new ArrayList<ProcessVo>();
            for (Process process : processList) {
                if (null != process) {
                    ProcessVo processVo = new ProcessVo();
                    BeanUtils.copyProperties(process, processVo);
                    processVo.setCrtDate(process.getCrtDttm());
                    processVoList.add(processVo);
                }
            }
        }
        return processVoList;
    }

    /**
     * 查询processVo根据id(查询包含其子表)
     *
     * @param id
     * @return
     */
    @Override
    public ProcessVo getProcessAllVoById(String id) {
        ProcessVo processVo = null;
        if (StringUtils.isNotBlank(id)) {
            Process processById = processTransaction.getProcessById(id);
            processVo = this.processPoToVo(processById);
        }
        return processVo;
    }

    /**
     * 查询processVo根据id(只查询process表)
     *
     * @param id
     * @return
     */
    @Override
    public ProcessVo getProcessVoById(String id) {
        ProcessVo processVo = null;
        if (StringUtils.isNotBlank(id)) {
            Process processById = processTransaction.getProcessById(id);
            if (null != processById) {
                processVo = new ProcessVo();
                BeanUtils.copyProperties(processById, processVo);
                processVo.setCrtDate(processById.getCrtDttm());
            }
        }
        return processVo;
    }

    /**
     * 查询process根据id
     *
     * @param id
     * @return
     */
    @Override
    public Process getProcessById(String id) {
        Process processById = processTransaction.getProcessById(id);
        return processById;
    }

    /**
     * 根据Appid查询process
     *
     * @param appId
     * @return
     */
    @Override
    public ProcessVo getProcessVoByAppId(String appId) {
        ProcessVo processVo = null;
        if (StringUtils.isNotBlank(appId)) {
            Process processById = processTransaction.getProcessByAppId(appId);
            if (null != processById) {
                processVo = this.processPoToVo(processById);
            }
        }
        return processVo;
    }

    /**
     * 根据数组Appid查询process
     *
     * @param appIDs
     * @return
     */
    public List<ProcessVo> getProcessVoByAppIds(String appIDs) {
        return null;
    }

    /**
     * 根据appID在第三方接口查询appInfo并保存
     *
     * @param appID
     * @return
     */
    @Override
    public ProcessVo getAppInfoByThirdAndSave(String appID) {
        ProcessVo processVo = new ProcessVo();
        Process processById = processTransaction.getProcessByAppId(appID);
        if (null != processById) {
            // 如果状态为STARTED，去调接口,否则说明已经是启动完成,直接返回
            ProcessState state = processById.getState();
            if (ProcessState.STARTED == state || null == processById.getStartTime()) {
                ThirdFlowInfoVo thirdFlowInfoVo = getFlowInfoImpl.getFlowInfo(appID);
                if (null != thirdFlowInfoVo) {
                    processById.getProcessStopList();
                    //判断接口返回的进度是否为空
                    if (StringUtils.isNotBlank(thirdFlowInfoVo.getProgress())) {
                        double progressNums = Double.parseDouble(thirdFlowInfoVo.getProgress());
                        Double progressNumsDb = null;
                        String percentage = processById.getProgress();
                        if (StringUtils.isNotBlank(percentage)) {
                            progressNumsDb = Double.parseDouble(percentage);
                        }
                        boolean isUpdateProcess = false;
                        // 判断状态，如果状态是STARTED，判断返回进度是否大于数据库进度，大于则保存
                        // 如果状态不是STARTED直接保存数据库
                        if ("STARTED".equals(thirdFlowInfoVo.getState())) {
                            // 如果数据库进度为空则进行保存
                            if (null == progressNumsDb) {
                                isUpdateProcess = true;
                            } else if (progressNums > progressNumsDb) {
                                //如果返回进度大于数据库进度则进行保存
                                isUpdateProcess = true;
                            }
                        } else {
                            isUpdateProcess = true;
                        }
                        if (isUpdateProcess) {
                            // 修改flow信息
                            processById.setLastUpdateUser("update");
                            processById.setLastUpdateDttm(new Date());
                            processById.setVersion(processById.getVersion() + 1);
                            processById.setProgress(progressNums + "");
                            processById.setState(ProcessState.selectGender(thirdFlowInfoVo.getState()));
                            processById.setProcessId(thirdFlowInfoVo.getPid());
                            processById.setName(thirdFlowInfoVo.getName());
                            processById.setStartTime(thirdFlowInfoVo.getStartTime());
                            processById.setEndTime(thirdFlowInfoVo.getEndTime());
                            processTransaction.updateProcess(processById);
                            // 修改stops信息
                            List<ThirdFlowInfoStopsVo> stops = thirdFlowInfoVo.getStops();
                            if (null != stops && stops.size() > 0) {
                                List<ProcessStop> processStopListNew = new ArrayList<ProcessStop>();
                                processVo.setId(processById.getId());
                                for (ThirdFlowInfoStopsVo thirdFlowInfoStopsVo : stops) {
                                    if (null != thirdFlowInfoStopsVo) {
                                        ThirdFlowInfoStopVo thirdFlowInfoStopVo = thirdFlowInfoStopsVo.getStop();
                                        if (null != thirdFlowInfoStopVo) {
                                            ProcessStopVo processStopVo = new ProcessStopVo();
                                            processStopVo.setName(thirdFlowInfoStopVo.getName());
                                            processStopVo.setState(thirdFlowInfoStopVo.getState());
                                            processStopVo.setStartTime(thirdFlowInfoStopVo.getStartTime());
                                            processStopVo.setEndTime(thirdFlowInfoStopVo.getEndTime());
                                            processStopVo.setProcessVo(processVo);
                                            ProcessStop processStop = processStopTransaction.updateProcessStopByProcessId(processStopVo);
                                            if (null != processStop) {
                                                processStopListNew.add(processStop);
                                            }
                                        }
                                    }
                                }
                                processById.setProcessStopList(processStopListNew);
                            }
                            processById = processTransaction.getProcessByAppId(appID);
                        }
                    }
                }
            }
            processVo = this.processPoToVo(processById);
        }

        return processVo;
    }

    /**
     * 根据appID在第三方接口查询progress并保存
     *
     * @param appIDs
     * @return
     */

    @Override
    public List<ProcessVo> getProgressByThirdAndSave(String[] appIDs) {
        List<ProcessVo> processVoList = null;
        List<Process> processListByAppIDs = processTransaction.getProcessListByAppIDs(appIDs);
        if (null != processListByAppIDs && processListByAppIDs.size() > 0) {
            processVoList = new ArrayList<ProcessVo>();
            for (Process process : processListByAppIDs) {
                if (null != process) {
                    ProcessVo processVo = null;
                    // 如果状态为STARTED，去调接口,否则说明已经是启动完成,直接返回
                    ProcessState state = process.getState();
                    if (ProcessState.STARTED == state) {
                        ThirdProgressVo flowProgress = getFlowProgressImpl.getFlowProgress(process.getAppId());
                        if (null != flowProgress) {
                            double progressNums = Double.parseDouble(flowProgress.getProgress());
                            double progressNumsDb = 0.00;
                            String percentage = process.getProgress();
                            if (StringUtils.isNotBlank(percentage)) {
                                progressNumsDb = Float.parseFloat(percentage);
                            }
                            boolean isUpdateProcess = false;
                            // 判断状态，如果状态是STARTED，判断返回进度是否大于数据库进度，大于则保存
                            // 如果状态不是STARTED直接保存数据库
                            if ("STARTED".equals(flowProgress.getState())) {
                                //如果返回进度大于数据库进度则进行保存
                                if (progressNums > progressNumsDb) {
                                    isUpdateProcess = true;
                                }
                            } else {
                                isUpdateProcess = true;
                            }
                            if (isUpdateProcess) {
                                // 修改flow信息
                                process.setLastUpdateUser("update");
                                process.setLastUpdateDttm(new Date());
                                process.setVersion(process.getVersion() + 1);
                                process.setProgress(progressNums + "");
                                process.setState(ProcessState.selectGender(flowProgress.getState()));
                                process.setName(flowProgress.getName());
                                processTransaction.updateProcess(process);
                            }
                        }
                        processVo = this.processPoToVo(process);
                    } else if (null == process.getStartTime()) {
                        processVo = this.getAppInfoByThirdAndSave(process.getAppId());
                    }
                    if (null != processVo) {
                        processVoList.add(processVo);
                    }
                }
            }
        }
        return processVoList;
    }

    /**
     * 修改process(只更新process表，子表不更新)
     *
     * @param processVo
     * @return
     */
    @Override
    public int updateProcess(ProcessVo processVo) {
        if (null != processVo) {
            Process processById = processTransaction.getProcessById(processVo.getId());
            if (null != processById) {
                BeanUtils.copyProperties("processVo", "processById");
                processById.setLastUpdateUser("update");
                processById.setLastUpdateDttm(new Date());
                processById.setVersion(processById.getVersion() + 1);
                processById.setState(processVo.getState());
                processById.setProgress(processVo.getProgress());
                processById.setStartTime(processVo.getStartTime());
                processById.setEndTime(processVo.getEndTime());
                processById.setProcessId(processVo.getProcessId());
                processById.setName(processVo.getName());
                return processTransaction.updateProcess(processById);
            }
        }
        return 0;
    }

    @Override
    public Process startFlowAndUpdateProcess(Flow flow) {
        Process process = null;
        if (null != flow) {
            process = this.flowToProcessAndSave(flow.getId());
            if (null != process) {
                String checkpoint = "";
                List<Stops> stopsList = flow.getStopsList();
                for (Stops stops : stopsList) {
                    stops.getCheckpoint();
                    if (null != stops.getCheckpoint() && stops.getCheckpoint()) {
                        if (StringUtils.isNotBlank(checkpoint)) {
                            checkpoint = (checkpoint + ",");
                        }
                        checkpoint = (checkpoint + stops.getName());
                    }
                }
                StatefulRtnBase startProcess = startFlowImpl.startProcess(process, checkpoint);
                if (null != startProcess && startProcess.isReqRtnStatus()) {
                    logger.info("调用接口并保存成功");
                } else {
                    processTransaction.updateProcessEnableFlag(process.getId());
                    logger.info("调用接口并保存失败");
                    process = null;
                }
            }
        }
        return process;
    }

    /**
     * 拷贝 process并新建
     *
     * @param processId
     * @return
     */
    @Override
    public Process processCopyProcessAndAdd(String processId) {
        Process processCopy = null;
        if (StringUtils.isNotBlank(processId)) {
            Process process = processTransaction.getProcessById(processId);
            if (null != process) {
                processCopy = new Process();
                processCopy.setId(Utils.getUUID32());
                processCopy.setCrtUser("Add");
                processCopy.setCrtDttm(new Date());
                processCopy.setLastUpdateUser("Copy");
                processCopy.setLastUpdateDttm(new Date());
                processCopy.setVersion(0L);
                processCopy.setEnableFlag(true);
                processCopy.setState(ProcessState.STARTED);
                processCopy.setName(process.getName());
                processCopy.setDriverMemory(process.getDriverMemory());
                processCopy.setExecutorNumber(process.getExecutorNumber());
                processCopy.setExecutorMemory(process.getExecutorMemory());
                processCopy.setExecutorCores(process.getExecutorCores());
                processCopy.setDescription(process.getDescription());
                processCopy.setViewXml(process.getViewXml());
                processCopy.setFlowId(process.getFlowId());
                processCopy.setParentProcessId(StringUtils.isNotBlank(process.getParentProcessId()) ? process.getParentProcessId() : process.getProcessId());
                List<ProcessPath> processPathList = process.getProcessPathList();
                if (null != processPathList && processPathList.size() > 0) {
                    List<ProcessPath> processPathListCopy = new ArrayList<ProcessPath>();
                    for (ProcessPath processPath : processPathList) {
                        if (null != processPath) {
                            ProcessPath processPathCopy = new ProcessPath();
                            processPathCopy.setId(Utils.getUUID32());
                            processPathCopy.setCrtDttm(new Date());
                            processPathCopy.setCrtUser("Copy");
                            processPathCopy.setLastUpdateDttm(new Date());
                            processPathCopy.setLastUpdateUser("Copy");
                            processPathCopy.setVersion(0L);
                            processPathCopy.setEnableFlag(true);
                            processPathCopy.setFrom(processPath.getFrom());
                            processPathCopy.setTo(processPath.getTo());
                            processPathCopy.setInport(processPath.getInport());
                            processPathCopy.setOutport(processPath.getOutport());
                            processPathCopy.setPageId(processPath.getPageId());
                            processPathCopy.setProcess(processCopy);
                            processPathListCopy.add(processPathCopy);
                        }
                    }
                    processCopy.setProcessPathList(processPathListCopy);
                }
                List<ProcessStop> processStopList = process.getProcessStopList();
                if (null != processStopList && processStopList.size() > 0) {
                    List<ProcessStop> processStopListCopy = new ArrayList<ProcessStop>();
                    for (ProcessStop processStop : processStopList) {
                        if (null != processStop) {
                            ProcessStop processStopCopy = new ProcessStop();
                            processStopCopy.setId(Utils.getUUID32());
                            processStopCopy.setCrtDttm(new Date());
                            processStopCopy.setCrtUser("Copy");
                            processStopCopy.setLastUpdateDttm(new Date());
                            processStopCopy.setLastUpdateUser("Copy");
                            processStopCopy.setVersion(0L);
                            processStopCopy.setEnableFlag(true);
                            processStopCopy.setBundel(processStop.getBundel());
                            processStopCopy.setName(processStop.getName());
                            processStopCopy.setDescription(processStop.getDescription());
                            processStopCopy.setGroups(processStop.getGroups());
                            processStopCopy.setInports(processStop.getInports());
                            processStopCopy.setInPortType(processStop.getInPortType());
                            processStopCopy.setOutports(processStop.getOutports());
                            processStopCopy.setOutPortType(processStop.getOutPortType());
                            processStopCopy.setOwner(processStop.getOwner());
                            processStopCopy.setPageId(processStop.getPageId());
                            processStopCopy.setProcess(processCopy);
                            List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
                            if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
                                List<ProcessStopProperty> processStopPropertyListCopy = new ArrayList<ProcessStopProperty>();
                                for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                                    if (null != processStopProperty) {
                                        ProcessStopProperty processStopPropertyCopy = new ProcessStopProperty();
                                        processStopPropertyCopy.setId(Utils.getUUID32());
                                        processStopPropertyCopy.setCrtDttm(new Date());
                                        processStopPropertyCopy.setCrtUser("Copy");
                                        processStopPropertyCopy.setLastUpdateDttm(new Date());
                                        processStopPropertyCopy.setLastUpdateUser("Copy");
                                        processStopPropertyCopy.setVersion(0L);
                                        processStopPropertyCopy.setEnableFlag(true);
                                        processStopPropertyCopy.setCustomValue(processStopProperty.getCustomValue());
                                        processStopPropertyCopy.setName(processStopProperty.getName());
                                        processStopPropertyCopy.setAllowableValues(processStopProperty.getAllowableValues());
                                        processStopPropertyCopy.setDescription(processStopProperty.getDescription());
                                        processStopPropertyCopy.setDisplayName(processStopProperty.getDisplayName());
                                        processStopPropertyCopy.setRequired(processStopProperty.getRequired());
                                        processStopPropertyCopy.setSensitive(processStopPropertyCopy.getSensitive());
                                        processStopPropertyCopy.setProcessStop(processStopCopy);
                                        processStopPropertyListCopy.add(processStopPropertyCopy);
                                    }
                                }
                                processStopCopy.setProcessStopPropertyList(processStopPropertyListCopy);
                            }
                            processStopListCopy.add(processStopCopy);
                        }
                    }
                    processCopy.setProcessStopList(processStopListCopy);
                }
                int addProcess = processTransaction.addProcess(processCopy);
                if (addProcess <= 0) {
                    processCopy = null;
                }
            }
        }
        return processCopy;
    }

    /**
     * 根据flowId生成Process并保存
     *
     * @param flowId
     * @return
     */
    @Override
    public Process flowToProcessAndSave(String flowId) {
        Process process = null;
        //flowId判空
        if (StringUtils.isNotBlank(flowId)) {
            // 查询flowId
            Flow flowById = flowTransaction.getFlowById(flowId);
            // 判空查询到的flow
            if (null != flowById) {
                process = new Process();
                // copy flow信息到process
                BeanUtils.copyProperties(flowById, process);
                // set基本信息
                process.setId(Utils.getUUID32());
                process.setCrtDttm(new Date());
                process.setCrtUser("Add");
                process.setLastUpdateDttm(new Date());
                process.setLastUpdateUser("Add");
                process.setEnableFlag(true);
                process.setVersion(0L);
                // 取出flow的画板信息
                MxGraphModel mxGraphModel = flowById.getMxGraphModel();
                // flow画板信息转为ViewXml
                String viewXml = this.mxGraphModelToViewXml(mxGraphModel);
                // set viewXml
                process.setViewXml(viewXml);
                // set flowId
                process.setFlowId(flowId);
                // 取出flow的stops
                List<Stops> stopsList = flowById.getStopsList();
                // 判空stopsList
                if (null != stopsList && stopsList.size() > 0) {
                    // proce的stop的list
                    List<ProcessStop> processStopList = new ArrayList<ProcessStop>();
                    // 循环stopsList
                    for (Stops stops : stopsList) {
                        // 判空stops
                        if (null != stops) {
                            ProcessStop processStop = new ProcessStop();
                            // copy stops的信息到processStop中
                            BeanUtils.copyProperties(stops, processStop);
                            // set基本信息
                            processStop.setId(Utils.getUUID32());
                            processStop.setCrtDttm(new Date());
                            processStop.setCrtUser("Add");
                            processStop.setLastUpdateDttm(new Date());
                            processStop.setLastUpdateUser("Add");
                            processStop.setEnableFlag(true);
                            processStop.setVersion(0L);
                            // 关联外键
                            processStop.setProcess(process);
                            // 取出stops的属性
                            List<Property> properties = stops.getProperties();
                            // stops的属性判空
                            if (null != properties && properties.size() > 0) {
                                List<ProcessStopProperty> processStopPropertyList = new ArrayList<ProcessStopProperty>();
                                // 循环stops的属性
                                for (Property property : properties) {
                                    // 判空
                                    if (null != property) {
                                        ProcessStopProperty processStopProperty = new ProcessStopProperty();
                                        // copy property的信息到processStopProperty中
                                        BeanUtils.copyProperties(property, processStopProperty);
                                        // set 基本信息
                                        processStopProperty.setId(Utils.getUUID32());
                                        processStopProperty.setCrtDttm(new Date());
                                        processStopProperty.setCrtUser("Add");
                                        processStopProperty.setLastUpdateDttm(new Date());
                                        processStopProperty.setLastUpdateUser("Add");
                                        processStopProperty.setEnableFlag(true);
                                        processStopProperty.setVersion(0L);
                                        // 关联外键
                                        processStopProperty.setProcessStop(processStop);
                                        processStopPropertyList.add(processStopProperty);
                                    }
                                }
                                processStop.setProcessStopPropertyList(processStopPropertyList);
                            }
                            processStopList.add(processStop);
                        }
                    }
                    process.setProcessStopList(processStopList);
                }
                // 取flow的paths信息
                List<Paths> pathsList = flowById.getPathsList();
                // 判空paths信息
                if (null != pathsList && pathsList.size() > 0) {
                    List<ProcessPath> processPathList = new ArrayList<ProcessPath>();
                    // 循环paths信息
                    for (Paths paths : pathsList) {
                        // 判空paths
                        if (null != paths) {
                            ProcessPath processPath = new ProcessPath();
                            // copy paths的信息到processPath中
                            BeanUtils.copyProperties(paths, processPath);
                            // set基本信息
                            processPath.setId(Utils.getUUID32());
                            processPath.setCrtDttm(new Date());
                            processPath.setCrtUser("Add");
                            processPath.setLastUpdateDttm(new Date());
                            processPath.setLastUpdateUser("Add");
                            processPath.setEnableFlag(true);
                            processPath.setVersion(0L);
                            // 关联外键
                            processPath.setProcess(process);
                            processPathList.add(processPath);
                        }
                    }
                    process.setProcessPathList(processPathList);
                }
                int addProcess = processTransaction.addProcess(process);
                if (addProcess <= 0) {
                    process = null;
                    logger.warn("保存失败，转换失败");
                }
            } else {
                logger.warn("查询不到flowId为‘" + flowId + "的flow’，转换失败");
            }
        } else {
            logger.warn("参数flowId为空，转换失败");
        }
        return process;
    }

    /**
     * 把画板转换成svg图
     *
     * @param mxGraphModel
     * @return
     */
    private String mxGraphModelToViewXml(MxGraphModel mxGraphModel) {
        String viewXml = "";
        if (null != mxGraphModel) {
            StringBuffer viewXmlStrBuf = new StringBuffer();
            viewXmlStrBuf.append("<svg style='width: 100%; height: 100%; display: block; min-width: 2391px; min-height: 2167px; position: absolute; background-image: none;'>");
            viewXmlStrBuf.append("<g>");
            List<MxCell> mxCellList = mxGraphModel.getRoot();
            if (null != mxCellList && mxCellList.size() > 0) {
                // 把需添加addMxCellVoList中的stops和线分开
                Map<String, Object> stopsPathsMap = MxGraphModelUtil.mxCellDistinguishStopsPaths(mxCellList);

                // 取出stops
                List<MxCell> stops = (List<MxCell>) stopsPathsMap.get("stops");
                Map<String, MxCell> stopPageIdKeyMap = new HashMap<String, MxCell>();
                if (null != stops && stops.size() > 0) {
                    // 放stops的g标签开始
                    viewXmlStrBuf.append("<g>");
                    // stop图片的坐标
                    int imageX = 0;
                    int imageY = 0;
                    for (MxCell mxCell : stops) {
                        if (null != mxCell) {
                            //stop的name
                            String name = (null != mxCell.getValue() ? mxCell.getValue() : "");
                            //取出样式
                            String style = mxCell.getStyle();
                            String imgPath = (null != style ? StringUtils.substringAfterLast(style, "=") : "");
                            MxGeometry mxGeometry = mxCell.getMxGeometry();
                            if (null != mxGeometry) {
                                //mxCell的x坐标
                                int mxGeometryX = (StringUtils.isNotBlank(mxGeometry.getX()) ? Integer.parseInt(mxGeometry.getX()) : 0);
                                //mxCell的y坐标
                                int mxGeometryY = (StringUtils.isNotBlank(mxGeometry.getY()) ? Integer.parseInt(mxGeometry.getY()) : 0);
                                //mxCell的高度
                                int mxGeometryHeight = (StringUtils.isNotBlank(mxGeometry.getHeight()) ? Integer.parseInt(mxGeometry.getHeight()) : 0);
                                //mxCell的宽度
                                int mxGeometryWidth = (StringUtils.isNotBlank(mxGeometry.getWidth()) ? Integer.parseInt(mxGeometry.getWidth()) : 0);
                                // stop 图片信息计算
                                // stop图片的坐标
                                imageX = mxGeometryX;
                                imageY = mxGeometryY;

                                // 点击的边框
                                viewXmlStrBuf.append("<g onclick=\"clickFormation(");//点击时选中效果坐标
                                viewXmlStrBuf.append("'" + imageX + "',");
                                viewXmlStrBuf.append("'" + imageY + "',");
                                viewXmlStrBuf.append("'" + mxGeometryWidth + "',");
                                viewXmlStrBuf.append("'" + mxGeometryHeight + "',");
                                viewXmlStrBuf.append("'" + mxCell.getPageId() + "'");
                                viewXmlStrBuf.append(")\">");

                                // 开始拼接监控图标
                                // fail图标
                                viewXmlStrBuf.append("<g style='visibility: visible;'>");
                                viewXmlStrBuf.append("<image id='stopFailShow" + mxCell.getPageId() + "' style='display: none;'");
                                viewXmlStrBuf.append("x='" + (imageX + mxGeometryWidth) + "' ");//监控的图标为图片X坐标+宽
                                viewXmlStrBuf.append("y='" + imageY + "' ");//监控的图标为图片Y坐标
                                viewXmlStrBuf.append("width = '15' height = '15'");
                                viewXmlStrBuf.append("xlink:href = '/piflow-web/img/Fail.png'");//监控图标地址
                                viewXmlStrBuf.append(" ></image >");
                                // ok图标
                                viewXmlStrBuf.append("<image id='stopOkShow" + mxCell.getPageId() + "' style='display: none;'");
                                viewXmlStrBuf.append("x='" + (imageX + mxGeometryWidth) + "' ");//监控的图标为图片X坐标+宽
                                viewXmlStrBuf.append("y='" + imageY + "' ");//监控的图标为图片Y坐标
                                viewXmlStrBuf.append("width = '15' height = '15'");
                                viewXmlStrBuf.append("xlink:href = '/piflow-web/img/Ok.png'");//监控图标地址
                                viewXmlStrBuf.append(" ></image >");
                                // Loading图标
                                viewXmlStrBuf.append("<image id='stopLoadingShow" + mxCell.getPageId() + "' style='display: none;'");
                                viewXmlStrBuf.append("x='" + (imageX + mxGeometryWidth) + "' ");//监控的图标为图片X坐标+宽
                                viewXmlStrBuf.append("y='" + imageY + "' ");//监控的图标为图片Y坐标
                                viewXmlStrBuf.append("width = '15' height = '15'");
                                viewXmlStrBuf.append("xlink:href = '/piflow-web/img/Loading.gif'");//监控图标地址
                                viewXmlStrBuf.append(" ></image >");
                                viewXmlStrBuf.append("</g>");

                                // 开始拼图片
                                viewXmlStrBuf.append("<g style='visibility: visible;'>");
                                viewXmlStrBuf.append("<image ");
                                viewXmlStrBuf.append("x='" + imageX + "' ");//图片X坐标
                                viewXmlStrBuf.append("y='" + imageY + "' ");//图片Y坐标
                                viewXmlStrBuf.append("width='" + mxGeometryWidth + "' ");//图片宽
                                viewXmlStrBuf.append("height='" + mxGeometryHeight + "' ");//图片高
                                viewXmlStrBuf.append("xlink:href='" + imgPath + "'");//图片地址
                                viewXmlStrBuf.append("></image>");
                                viewXmlStrBuf.append("</g>");
                                // stop 文字信息计算
                                // 字的坐标
                                int fontX = ((mxGeometryWidth - name.length() * 6) / 2) + imageX;
                                int fontY = imageY + mxGeometryHeight + 8;
                                int fontWidth = name.length() * 6;
                                int fontHeight = 12;
                                // 开始拼字
                                viewXmlStrBuf.append("<g transform='translate(" + fontX + "," + fontY + ")'>");//x和y坐标
                                viewXmlStrBuf.append("<foreignObject style='overflow:visible;' pointer-events='all' ");
                                viewXmlStrBuf.append("width='" + fontWidth + "' height='" + fontHeight + "'>");//宽度和高度
                                viewXmlStrBuf.append("<div style='display:inline-block;font-size:12px;font-family:Helvetica;color:#000000;line-height:1.2;vertical-align:top;white-space:nowrap;text-align:center;'>");
                                viewXmlStrBuf.append("<div xmlns='http://www.w3.org/1999/xhtml' style = 'display:inline-block;text-align:inherit;text-decoration:inherit;background-color:#ffffff;'>");
                                viewXmlStrBuf.append(name); //stop的name
                                viewXmlStrBuf.append("</div>");
                                viewXmlStrBuf.append("</div>");
                                viewXmlStrBuf.append("</foreignObject>");
                                viewXmlStrBuf.append("</g>");
                                viewXmlStrBuf.append("</g>");
                                // 把stop的mxCell放入map用于生成线的坐标
                                stopPageIdKeyMap.put(mxCell.getPageId(), mxCell);
                            }
                        }
                    }
                    // 放stops的g标签结束
                    viewXmlStrBuf.append("</g>");
                }
                // 取出paths
                List<MxCell> paths = (List<MxCell>) stopsPathsMap.get("paths");
                if (null != paths && paths.size() > 0) {
                    // 放paths的g标签开始
                    viewXmlStrBuf.append("<g>");
                    for (MxCell mxCell : paths) {
                        if (null != mxCell) {
                            MxCell sourceMxCell1 = stopPageIdKeyMap.get(mxCell.getSource());
                            MxCell targetMxCell1 = stopPageIdKeyMap.get(mxCell.getTarget());
                            if (null != sourceMxCell1 && null != targetMxCell1) {
                                MxGeometry sourceMxGeometry = sourceMxCell1.getMxGeometry();
                                MxGeometry targetMxGeometry = targetMxCell1.getMxGeometry();
                                if (null != sourceMxGeometry && null != targetMxGeometry) {
                                    String drawingLine = this.drawingLine(sourceMxGeometry, targetMxGeometry);
                                    if (StringUtils.isNotBlank(drawingLine)) {
                                        viewXmlStrBuf.append(drawingLine);
                                    }
                                }
                            }
                        }
                    }
                    // 放paths的g标签结束
                    viewXmlStrBuf.append("</g>");
                }
            }
            viewXmlStrBuf.append(" <g transform='translate(2,2)'>");
            viewXmlStrBuf.append("<rect id='selectedRectShow' x='0' y='0' width='66' height='66' fill='none' stroke='#00a8ff' stroke-dasharray='3 3' pointer-events='none' style='display: none;'></rect>");
            viewXmlStrBuf.append("</g>");
            viewXmlStrBuf.append("</g>");
            viewXmlStrBuf.append("</svg>");
            viewXml = viewXmlStrBuf.toString();
        }
        return viewXml;
    }

    /**
     * 根据source属性和target属性画连接线
     *
     * @param sourceMxGeometry
     * @param targetMxGeometry
     * @return
     */
    private String drawingLine(MxGeometry sourceMxGeometry, MxGeometry targetMxGeometry) {
        String lineSvg = "";
        if (null != sourceMxGeometry && null != targetMxGeometry) {
            // 取坐标和高宽参数
            String sourceXStr = sourceMxGeometry.getX();
            String sourceYStr = sourceMxGeometry.getY();
            String sourceWidthStr = sourceMxGeometry.getWidth();
            String sourceHeightStr = sourceMxGeometry.getHeight();
            String targetXStr = targetMxGeometry.getX();
            String targetYStr = targetMxGeometry.getY();
            String targetWidthStr = targetMxGeometry.getWidth();
            String targetHeightStr = targetMxGeometry.getHeight();
            // 参数判空
            if (!StringUtils.isAnyEmpty(sourceXStr, sourceYStr, sourceWidthStr, sourceHeightStr, targetXStr, targetYStr, targetWidthStr, targetHeightStr)) {
                // 坐标和高宽参数转int
                int sourceX = Integer.parseInt(sourceXStr);
                int sourceY = Integer.parseInt(sourceYStr);
                int sourceWidth = Integer.parseInt(sourceWidthStr);
                int sourceHeight = Integer.parseInt(sourceHeightStr);
                int targetX = Integer.parseInt(targetXStr);
                int targetY = Integer.parseInt(targetYStr);
                int targetWidth = Integer.parseInt(targetWidthStr);
                int targetHeight = Integer.parseInt(targetHeightStr);
                // 线的起点坐标
                int sourceDotX = 0;
                int sourceDotY = 0;
                // 线的终点点坐标
                int targetDotX = 0;
                int targetDotY = 0;
                // 箭头方向
                ArrowDirection arrowDirection = null;

                //线的生成规则如下：
                //             ||           ||
                //     E区     ||    A区    ||     F区
                //             ||           ||
                //=============||===========||=============
                //             ||           ||
                //     D区     ||   target  ||     B区
                //             ||           ||
                //=============||===========||=============
                //             ||           ||
                //     H区     ||     C区   ||     G区
                //             ||           ||
                //-----------------------------------------
                // 以target为中心，source的位置分布在A到H的9个区中，通过source和target的位置判断出线入线方向，以此画线

                // A区为下出上入，条件：sourceY < targetY 且 (targetX-source宽) <= sourceX <= (targetX+target宽)
                // B区为左出右入，条件：sourceX > (targetX+target宽) 且 (targetY-source高) <= sourceY <= (targetY+target高)
                // C区为上出下入，条件：sourceY >= targetY 且 (targetX-source宽) <= sourceX <= (targetX+target宽)
                // D区为右出左入，条件：sourceX < (targetX-source宽) 且 (targetY-source高) <= sourceY <= (targetY+target高)
                // E区为右出上入，条件：sourceX < (targetX-source宽) 且 sourceY < (targetY-source高)
                // F区为左出上入，条件：sourceX > (targetX+target宽) 且 sourceY < (targetY-source高)
                // G区为左出下入，条件：sourceX > (targetX+target宽) 且 sourceY > (targetY+target高)
                // H区为右出下入，条件：sourceX < (targetX-source宽) 且 sourceY > (targetY+target高)
                // 线的折点数,
                // ABCD区线的折点数为0或2，breakPoint用0表示，
                // EFGH区线的折点数为1,breakPoint用1表示，
                int breakPoint = 0;
                // 根据A到H区的条件判断求出出线点和入线点的坐标(起点终点)
                if (sourceY < targetY && (targetX - sourceWidth) <= sourceX && sourceX <= (targetX + targetWidth)) {
                    // A区 下出上入
                    // 当source在A区时，起点为source下边的中心点，终点为target上边的中心点
                    sourceDotX = sourceX + (sourceWidth / 2);
                    sourceDotY = sourceY + sourceHeight;
                    targetDotX = targetX + (targetWidth / 2);
                    targetDotY = targetY;
                    arrowDirection = ArrowDirection.DOWN_DIRECTION;
                    breakPoint = 0;
                } else if (sourceX > (targetX + targetWidth) && (targetY - sourceHeight) <= sourceY && sourceY <= (targetY + targetHeight)) {
                    // B区为左出右入
                    // 当source在B区时，起点为source左边的中心点，终点为target右边的中心点
                    sourceDotX = sourceX;
                    sourceDotY = sourceY + (sourceHeight / 2);
                    targetDotX = targetX + targetWidth;
                    targetDotY = targetY + (targetHeight / 2);
                    arrowDirection = ArrowDirection.LEFT_DIRECTION;
                    breakPoint = 0;
                } else if (sourceY >= targetY && (targetX - sourceWidth) <= sourceX && sourceX <= (targetX + targetWidth)) {
                    // C区为上出下入
                    // 当source在C区时，起点为source上边的中心点，终点为target下边的中心点
                    sourceDotX = sourceX + (sourceWidth / 2);
                    sourceDotY = sourceY;
                    targetDotX = targetX + (targetWidth / 2);
                    targetDotY = targetY + targetHeight;
                    arrowDirection = ArrowDirection.UP_DIRECTION;
                    breakPoint = 0;
                } else if (sourceX < (targetX - sourceWidth) && (targetY - sourceHeight) <= sourceY && sourceY <= (targetY + targetHeight)) {
                    // D区为右出左入
                    // 当source在D区时，起点为source右边的中心点，终点为target左边的中心点
                    sourceDotX = sourceX + sourceWidth;
                    sourceDotY = sourceY + (sourceHeight / 2);
                    targetDotX = targetX;
                    targetDotY = targetY + (targetHeight / 2);
                    arrowDirection = ArrowDirection.RIGHT_DIRECTION;
                    breakPoint = 0;
                } else if (sourceX < (targetX - sourceWidth) && sourceY < (targetY - sourceHeight)) {
                    // E区为右出上入
                    // 当source在E区时，起点为source右边的中心点，终点为target上边的中心点
                    sourceDotX = sourceX + sourceWidth;
                    sourceDotY = sourceY + (sourceHeight / 2);
                    targetDotX = targetX + (targetWidth / 2);
                    targetDotY = targetY;
                    arrowDirection = ArrowDirection.DOWN_DIRECTION;
                    breakPoint = 1;
                } else if (sourceX > (targetX + targetWidth) && sourceY < (targetY - sourceHeight)) {
                    // F区为左出上入
                    // 当source在F区时，起点为source左边的中心点，终点为target上边的中心点
                    sourceDotX = sourceX;
                    sourceDotY = sourceY + (sourceHeight / 2);
                    targetDotX = targetX + (targetWidth / 2);
                    targetDotY = targetY;
                    arrowDirection = ArrowDirection.DOWN_DIRECTION;
                    breakPoint = 1;
                } else if (sourceX > (targetX + targetWidth) && sourceY > (targetY + targetHeight)) {
                    // G区为左出下入
                    // 当source在G区时，起点为source左边的中心点，终点为target下边的中心点
                    sourceDotX = sourceX;
                    sourceDotY = sourceY + (sourceHeight / 2);
                    targetDotX = targetX + (targetWidth / 2);
                    targetDotY = targetY + targetHeight;
                    arrowDirection = ArrowDirection.DOWN_DIRECTION;
                    breakPoint = 1;
                } else if (sourceX < (targetX - sourceWidth) && sourceY > (targetY + targetHeight)) {
                    // H区为右出下入
                    // 当source在H区时，起点为source右边的中心点，终点为target下边的中心点
                    sourceDotX = sourceX + sourceWidth;
                    sourceDotY = sourceY + (sourceHeight / 2);
                    targetDotX = targetX + (targetWidth / 2);
                    targetDotY = targetY + targetHeight;
                    arrowDirection = ArrowDirection.DOWN_DIRECTION;
                    breakPoint = 1;
                } else {
                    logger.warn("没有判断出位置信息，画线失败");
                    return lineSvg;
                }
                if (null != arrowDirection) {
                    // 开始画线
                    StringBuffer lineSvgBuf = new StringBuffer();
                    lineSvgBuf.append("<g transform='translate(0.5,0.5)' style='visibility: visible;'>");
                    // 线开始计算
                    switch (breakPoint) {
                        case 0:
                            if (sourceDotX == targetDotX || sourceDotY == targetDotY) {
                                //无折点，直接拼线
                                lineSvgBuf.append("<path d='");
                                lineSvgBuf.append("M " + sourceDotX + " " + sourceDotY + " ");
                                lineSvgBuf.append("L " + targetDotX + " " + targetDotY + " ");
                                lineSvgBuf.append("' fill='none' stroke='#000000' stroke-miterlimit='10'></path>");
                            } else {
                                lineSvgBuf.append("<path d='");
                                lineSvgBuf.append("M " + sourceDotX + " " + sourceDotY + " ");
                                // 两个折点，计算折点坐标
                                if (arrowDirection == ArrowDirection.UP_DIRECTION) {
                                    lineSvgBuf.append("L " + sourceDotX + " " + (((sourceDotY - targetDotY) / 2) + targetDotY) + " ");
                                    lineSvgBuf.append("L " + targetDotX + " " + (((sourceDotY - targetDotY) / 2) + targetDotY) + " ");
                                } else if (arrowDirection == ArrowDirection.DOWN_DIRECTION) {
                                    lineSvgBuf.append("L " + sourceDotX + " " + (((targetDotY - sourceDotY) / 2) + sourceDotY) + " ");
                                    lineSvgBuf.append("L " + targetDotX + " " + (((targetDotY - sourceDotY) / 2) + sourceDotY) + " ");
                                } else if (arrowDirection == ArrowDirection.LEFT_DIRECTION) {
                                    lineSvgBuf.append("L " + (((sourceDotX - targetDotX) / 2) + targetDotX) + " " + sourceDotY + " ");
                                    lineSvgBuf.append("L " + (((sourceDotX - targetDotX) / 2) + targetDotX) + " " + targetDotY + " ");
                                } else if (arrowDirection == ArrowDirection.RIGHT_DIRECTION) {
                                    lineSvgBuf.append("L " + (((targetDotX - sourceDotX) / 2) + sourceDotX) + " " + sourceDotX + " ");
                                    lineSvgBuf.append("L " + (((targetDotX - sourceDotX) / 2) + sourceDotX) + " " + targetDotX + " ");
                                }
                                lineSvgBuf.append("L " + targetDotX + " " + targetDotY + " ");
                                lineSvgBuf.append("' fill='none' stroke='#000000' stroke-miterlimit='10'></path>");
                            }
                            break;
                        case 1:
                            // 一个折点
                            lineSvgBuf.append("<path d='");
                            lineSvgBuf.append("M " + sourceDotX + " " + sourceDotY + " ");
                            lineSvgBuf.append("L " + targetDotX + " " + sourceDotY + " ");
                            lineSvgBuf.append("L " + targetDotX + " " + targetDotY + " ");
                            lineSvgBuf.append("' fill='none' stroke='#000000' stroke-miterlimit='10'></path>");
                            break;
                        default:
                            break;
                    }

                    // 箭头坐标开始计算
                    lineSvgBuf.append("<path d='");
                    // 箭头是由一个M坐标三个L坐标组成，M坐标为箭头的指向的点
                    lineSvgBuf.append("M " + targetDotX + " " + targetDotY + " ");
                    // 第一个L箭头(右侧点)，
                    lineSvgBuf.append("L ");
                    // 第一个L(右侧点)的X
                    lineSvgBuf.append((targetDotX - (arrowDirection.getUpX() - arrowDirection.getRightX())) + " ");
                    // 第一个L(右侧点)的Y
                    lineSvgBuf.append((targetDotY - (arrowDirection.getUpY() - arrowDirection.getRightY())) + " ");
                    // 第二个L箭头(尾点)，
                    lineSvgBuf.append("L ");
                    // 第二个L(尾点)的X
                    lineSvgBuf.append((targetDotX - (arrowDirection.getUpX() - arrowDirection.getDownX())) + " ");
                    // 第二个L(尾点)的Y
                    lineSvgBuf.append((targetDotY - (arrowDirection.getUpY() - arrowDirection.getDownY())) + " ");
                    // 第三个L箭头(左侧点)，
                    lineSvgBuf.append("L ");
                    // 第三个L(左侧点)的X
                    lineSvgBuf.append((targetDotX - (arrowDirection.getUpX() - arrowDirection.getLfetX())) + " ");
                    // 第三个L(左侧点)的Y
                    lineSvgBuf.append((targetDotY - (arrowDirection.getUpY() - arrowDirection.getLfetY())) + " ");
                    lineSvgBuf.append("Z' fill='#000000' stroke='#000000' stroke-miterlimit='10' pointer-events='all'></path>");
                    lineSvgBuf.append("</g>");
                    lineSvg = lineSvgBuf.toString();
                } else {
                    logger.warn("没有判断出箭头方向，箭头方向为空，画线失败");
                }
            } else {
                logger.warn("source或target的坐标或高宽为空，画线失败");
            }
        } else {
            logger.warn("参数有空值，画线失败");
        }

        return lineSvg;
    }

    private ProcessVo processPoToVo(Process process) {
        ProcessVo processVo = null;
        if (null != process) {
            processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setCrtDate(process.getCrtDttm());
            List<ProcessStopVo> processStopVoList = ProcessStopUtils.processStopListPoToVo(process.getProcessStopList());
            processVo.setProcessStopVoList(processStopVoList);
            List<ProcessPath> processPathList = process.getProcessPathList();
            if (null != processPathList && processPathList.size() > 0) {
                List<ProcessPathVo> processPathVoList = new ArrayList<ProcessPathVo>();
                for (ProcessPath processPath : processPathList) {
                    if (null != processPath) {
                        ProcessPathVo processPathVo = new ProcessPathVo();
                        BeanUtils.copyProperties(processPath, processPathVo);
                        processPathVoList.add(processPathVo);
                    }
                }
                processVo.setProcessPathVoList(processPathVoList);
            }
        }
        return processVo;
    }

    /**
     * 逻辑删除
     *
     * @param processId
     * @return
     */
    @Override
    public boolean updateProcessEnableFlag(String processId) {
        return processTransaction.updateProcessEnableFlag(processId);
    }
}