package cn.cnic.component.process.domain;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.component.flow.domain.FlowPublishDomain;
import cn.cnic.component.flow.utils.FlowGlobalParamsUtils;
import cn.cnic.component.mxGraph.domain.MxGraphModelDomain;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.*;
import cn.cnic.component.process.mapper.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class ProcessDomain {

    private Logger logger = LoggerUtil.getLogger();

    private final ProcessStopCustomizedPropertyMapper processStopCustomizedPropertyMapper;
    private final ProcessStopPropertyMapper processStopPropertyMapper;
    private final MxGraphModelDomain mxGraphModelDomain;
    private final ProcessPathMapper processPathMapper;
    private final ProcessStopMapper processStopMapper;
    private final ProcessMapper processMapper;

    @Autowired
    public ProcessDomain(ProcessStopCustomizedPropertyMapper processStopCustomizedPropertyMapper,
                         ProcessStopPropertyMapper processStopPropertyMapper,
                         MxGraphModelDomain mxGraphModelDomain,
                         ProcessPathMapper processPathMapper,
                         ProcessStopMapper processStopMapper,
                         ProcessMapper processMapper) {
        this.processStopCustomizedPropertyMapper = processStopCustomizedPropertyMapper;
        this.processStopPropertyMapper = processStopPropertyMapper;
        this.mxGraphModelDomain = mxGraphModelDomain;
        this.processPathMapper = processPathMapper;
        this.processStopMapper = processStopMapper;
        this.processMapper = processMapper;
    }

    public int saveOrUpdate(Process process) throws Exception {
        if (null == process) {
            throw new Exception("save failed, process is null");
        }
        if (StringUtils.isBlank(process.getId())) {
            return addProcess(process);
        }
        return updateProcess(process);
    }

    /**
     * Add process of things
     *
     * @param process process
     * @return affected rows
     */
    public int addProcess(Process process) throws Exception {
        if (null == process) {
            throw new Exception("save failed");
        }
        String id = process.getId();
        if (StringUtils.isBlank(id)) {
            process.setId(UUIDUtils.getUUID32());
        }
        int affectedRows = processMapper.addProcess(process);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        // save path
        // Number of save Paths
        List<ProcessPath> processPathList = process.getProcessPathList();
        if (null != processPathList && processPathList.size() > 0) {
            for (ProcessPath processPath : processPathList) {
                if (null == processPath) {
                    continue;
                }
                processPath.setProcess(process);
                affectedRows += addProcessPath(processPath);
            }
        }
        // Save Stop
        // Number of deposits in Stop
        List<ProcessStop> processStopList = process.getProcessStopList();
        if (null != processStopList && processStopList.size() > 0) {
            for (ProcessStop processStop : processStopList) {
                processStop.setProcess(process);
                affectedRows += addProcessStop(processStop);
            }
        }
        MxGraphModel mxGraphModel = process.getMxGraphModel();
        if (null != mxGraphModel) {
            mxGraphModel.setProcess(process);
            affectedRows += mxGraphModelDomain.addMxGraphModel(mxGraphModel);
        }
        String [] globalParamsIds = FlowGlobalParamsUtils.globalParamsToIds(process.getFlowGlobalParamsList());
        if (null != globalParamsIds && globalParamsIds.length > 0) {
        	affectedRows += linkGlobalParams(process.getId(), globalParamsIds);
        }

        return affectedRows;
    }

    public int addProcessPath(ProcessPath processPath) throws Exception {
        if (null == processPath) {
            throw new Exception("save failed");
        }
        int affectedRows = processPathMapper.addProcessPath(processPath);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        return affectedRows;
    }

    public int addProcessPathList(List<ProcessPath> processPathList) throws Exception {
        return processPathMapper.addProcessPathList(processPathList);
    }

    public int addProcessStop(ProcessStop processStop) throws Exception {
        if (null == processStop) {
            throw new Exception("save failed, ProcessStop is null");
        }
        String processStopId = processStop.getId();
        if (StringUtils.isBlank(processStopId)) {
            processStop.setId(UUIDUtils.getUUID32());
        }
        int affectedRows = processStopMapper.addProcessStop(processStop);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
        if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
            for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                if (null == processStopProperty) {
                    continue;
                }
                processStopProperty.setProcessStop(processStop);
                affectedRows += addProcessStopProperty(processStopProperty);
            }
        }
        List<ProcessStopCustomizedProperty> processStopCustomizedPropertyList = processStop.getProcessStopCustomizedPropertyList();
        if (null != processStopCustomizedPropertyList && processStopCustomizedPropertyList.size() > 0) {
            for (ProcessStopCustomizedProperty processStopCustomizedProperty : processStopCustomizedPropertyList) {
                if (null == processStopCustomizedProperty) {
                    throw new Exception("save failed");
                }
                processStopCustomizedProperty.setProcessStop(processStop);
                affectedRows += addProcessStopCustomizedProperty(processStopCustomizedProperty);
            }
        }
        return affectedRows;
    }

    public int addProcessStopList(List<ProcessStop> processStopList) throws Exception {
        return processStopMapper.addProcessStopList(processStopList);
    }

    public int addProcessStopProperty(ProcessStopProperty processStopProperty) throws Exception {
        if (null == processStopProperty) {
            throw new Exception("save failed");
        }
        int affectedRows = processStopPropertyMapper.addProcessStopProperty(processStopProperty);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        return affectedRows;
    }

    public int addProcessStopCustomizedProperty(ProcessStopCustomizedProperty processStopCustomizedProperty) throws Exception {
        if (null == processStopCustomizedProperty) {
            throw new Exception("save failed");
        }
        int affectedRows = processStopCustomizedPropertyMapper.addProcessStopCustomizedProperty(processStopCustomizedProperty);
        if (affectedRows <= 0) {
            throw new Exception("save failed");
        }
        return affectedRows;
    }
    /**
     * The process of modifying things (just modify the process table)
     *
     * @param process process
     * @return affected rows
     * @throws Exception
     */
    public int updateProcess(Process process) throws Exception {
        int affectedRows = processMapper.updateProcess(process);
        affectedRows += updateProcessPathList(process.getProcessPathList());
        affectedRows += updateProcessStopList(process.getProcessStopList());
        String[] globalParamsIdsDB = getGlobalParamsIdsByProcessId(process.getId());
        String [] globalParamsIds = FlowGlobalParamsUtils.globalParamsToIds(process.getFlowGlobalParamsList());
        affectedRows += unlinkGlobalParams(process.getId(), globalParamsIdsDB);
        if (null != globalParamsIds && globalParamsIds.length > 0) {
        	//List<String> req = Arrays.asList(globalParamsIds);
        	//List<String> db = Arrays.asList(globalParamsIdsDB);
            //1、并集 union
        	//Object[] array = CollectionUtils.union(req, db).toArray();
            //2、交集 intersection
        	//Object[] array = CollectionUtils.intersection(req, db).toArray();
        	//3、交集的补集
        	//Object[] disjunction = CollectionUtils.disjunction(req, db).toArray();
        	//4、差集（扣除）
        	//Object[] subtract = CollectionUtils.subtract(db, req).toArray();
            //String[] unlinkIds = Arrays.copyOf(subtract, subtract.length, String[].class);
            //affectedRows += unlinkGlobalParams(process.getId(), unlinkIds);
            affectedRows += linkGlobalParams(process.getId(), globalParamsIds);
        }
        return affectedRows;
    }

    public int updateProcessPath(ProcessPath processPath) {
        if (null == processPath) {
            return 0;
        }
        return processPathMapper.updateProcessPath(processPath);
    }

    public int updateProcessPathList(List<ProcessPath> processPathList) throws Exception {
        if (null == processPathList || processPathList.size() <= 0) {
            return 0;
        }
        int affectedRows = 0;
        for (ProcessPath processPath : processPathList) {
            if (null == processPath) {
                continue;
            }
            if (StringUtils.isBlank(processPath.getId())) {
                int addProcessPath = addProcessPath(processPath);
                if (addProcessPath <= 0) {
                    throw new Exception("save failed");
                }
                affectedRows += addProcessPath;
            } else {
                affectedRows += updateProcessPath(processPath);
            }
        }
        return affectedRows;
    }

    public int updateProcessStop(ProcessStop processStop) throws Exception {
        if (null == processStop) {
            return 0;
        }
        int affectedRows = 0;
        affectedRows += processStopMapper.updateProcessStop(processStop);
        affectedRows += updateProcessStopPropertyList(processStop.getProcessStopPropertyList());
        return affectedRows;
    }

    public int updateProcessStopList(List<ProcessStop> processStopList) throws Exception {
        if (null == processStopList || processStopList.size() <= 0) {
            return 0;
        }
        int affectedRows = 0;
        for (ProcessStop processStop : processStopList) {
            if (null == processStop) {
                continue;
            }
            if (StringUtils.isBlank(processStop.getId())) {
                int addProcessStop = addProcessStop(processStop);
                if (addProcessStop <= 0) {
                    throw new Exception("save failed");
                }
                affectedRows += addProcessStop;
            } else {
                affectedRows += updateProcessStop(processStop);
            }
        }
        return affectedRows;
    }

    public int updateProcessStopProperty(ProcessStopProperty processStopProperty) {
        if (null == processStopProperty) {
            return 0;
        }
        int affectedRows = processStopPropertyMapper.updateProcessStopProperty(processStopProperty);
        return affectedRows;
    }

    public int updateProcessStopPropertyList(List<ProcessStopProperty> processStopPropertyList) throws Exception {
        if (null == processStopPropertyList || processStopPropertyList.size() <= 0) {
            return 0;
        }
        int affectedRows = 0;
        for (ProcessStopProperty processStopProperty : processStopPropertyList) {
            if (null == processStopProperty) {
                continue;
            }
            if (StringUtils.isBlank(processStopProperty.getId())) {
                int addProcessStopProperty = addProcessStopProperty(processStopProperty);
                if (addProcessStopProperty <= 0) {
                    throw new Exception("save failed");
                }
                affectedRows += addProcessStopProperty;
            } else {
                affectedRows += updateProcessStopProperty(processStopProperty);
            }
        }
        return affectedRows;
    }

    /**
     * logically delete
     *
     * @param processId processId
     * @return affected rows
     */
    public boolean updateProcessEnableFlag(String username, boolean isAdmin, String processId) {
        int affectedLine = 0;
        if (StringUtils.isBlank(processId) || StringUtils.isBlank(username)) {
            logger.info("Number of rows affected：" + affectedLine);
            return false;
        }
        Process processById = processMapper.getProcessById(username, isAdmin, processId);
        if (null == processById) {
            return false;
        }
        List<ProcessPath> processPathList = processById.getProcessPathList();
        if (null != processPathList && processPathList.size() > 0) {
            int updateEnableFlagByProcessId = processPathMapper.updateEnableFlagByProcessId(processId, username);
            affectedLine += updateEnableFlagByProcessId;
        }
        List<ProcessStop> processStopList = processById.getProcessStopList();
        if (null != processStopList && processStopList.size() > 0) {
            for (ProcessStop processStop : processStopList) {
                if (null != processStop) {
                    List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
                    if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
                        int updateEnableFlagByProcessStopId = processStopPropertyMapper.updateEnableFlagByProcessStopId(processStop.getId(), username);
                        affectedLine += updateEnableFlagByProcessStopId;
                    }
                }
            }
            int updateEnableFlagByProcessId = processStopMapper.updateEnableFlagByProcessId(processId, username);
            affectedLine += updateEnableFlagByProcessId;
        }
        int updateEnableFlag = processMapper.updateEnableFlag(processId, username);
        if (updateEnableFlag <= 0) {
            return false;
        }
        logger.info("Number of rows affected：" + affectedLine);
        return true;
    }

    /**
     * logically delete
     *
     * @param appId appId
     * @return Process
     */
    public Process getProcessByAppId(String appId) {
        if (StringUtils.isBlank(appId)) {
            logger.warn("appId id is null");
            return null;
        }

        Process processByAppId = processMapper.getProcessByAppId(appId);
        if (null == processByAppId) {
            logger.warn("data is null");
            return null;
        }
        return processByAppId;
    }

    /**
     * logically delete
     *
     * @param appId appId
     * @return Process
     */
    public String getProcessIdByAppId(String username, boolean isAdmin, String appId) {
        if (StringUtils.isBlank(appId)) {
            logger.warn("appId id is null");
            return null;
        }

        String processId = processMapper.getProcessIdByAppId(appId);
        if (null == processId) {
            logger.warn("data is null");
            return null;
        }
        return processId;
    }

    /**
     * logically delete
     *
     * @param id id
     * @return Process
     */
    public Process getProcessById(String username, boolean isAdmin, String id) {
        if (StringUtils.isBlank(id)) {
            logger.warn("id is null");
            return null;
        }

        Process processById = processMapper.getProcessById(username, isAdmin, id);
        if (null == processById) {
            logger.warn("data is null");
            return null;
        }
        return processById;
    }

    /**
     * getProcessNoGroupByAppId
     *
     * @param appId appId
     * @return Process
     */
    public List<Process> getProcessNoGroupByAppId(String appId) {
        if (StringUtils.isBlank(appId)) {
            logger.warn("appId is null");
            return null;
        }

        List<Process> processNoGroupList = processMapper.getProcessNoGroupByAppId(appId);
        if (null == processNoGroupList) {
            logger.warn("data is null");
            return null;
        }
        return processNoGroupList;
    }

    /**
     * getProcessNoGroupByAppId
     *
     * @return Process
     */
    public List<String> getRunningProcessAppId() {
        return processMapper.getRunningProcessAppId();
    }

    public ProcessState getProcessStateById(String id) {
        return processMapper.getProcessStateById(id);

    }
    
    public String[] getGlobalParamsIdsByProcessId(String processId) {
    	return processMapper.getGlobalParamsIdsByProcessId(processId);
    }
    
    public int linkGlobalParams(String processId, String[] globalParamsIds) {
    	return processMapper.linkGlobalParams(processId, globalParamsIds);
    }
    
    public int unlinkGlobalParams(String processId, String[] globalParamsIds) {
    	return processMapper.unlinkGlobalParams(processId, globalParamsIds);
    }

    public List<Process> getRunningProcessList(String flowId) {
        return processMapper.getRunningProcessList(flowId);
    }

    public Process getProcessByPageId(String username, boolean isAdmin, String processGroupId, String pageId) {
        return processMapper.getProcessByPageId(username, isAdmin, processGroupId, pageId);
    }

    public List<Process> getProcessListByParam(String username, boolean isAdmin, String param) {
        return processMapper.getProcessListByParam(username, isAdmin, param);
    }

    public List<Process> getProcessGroupListByParam(String username, boolean isAdmin, String param) {
        return processMapper.getProcessGroupListByParam(username, isAdmin, param);
    }

    public List<Process> getProcessList() {
        return processMapper.getProcessList();
    }

    public List<Process> getProcessListByAppIDs(String[] appIDs) {
        return processMapper.getProcessListByAppIDs(appIDs);
    }

    public ProcessStop getProcessStopByPageIdAndPageId(String processId, String pageId) {
        return processStopMapper.getProcessStopByPageIdAndPageId(processId, pageId);
    }

    public ProcessStop getProcessStopByNameAndPid(String processId, String name) {
        return processStopMapper.getProcessStopByNameAndPid(processId, name);
    }

    public String getProcessAppIdByStopId(String stopsId) {
        return processStopMapper.getProcessAppIdByStopId(stopsId);
    }

    public String getProcessStopNameByStopId(String stopsId) {
        return processStopMapper.getProcessStopNameByStopId(stopsId);
    }

    public List<Process> getProcessListByPublishingIdAndUserName(Long publishingId, String keywords, String username) {
        return processMapper.getProcessListByPublishingIdAndUserName(publishingId,keywords,username);
    }

    public Process getProcessWithFlowPublishingById(String processId) {
        return processMapper.getProcessWithFlowPublishingById(processId);
    }

    public List<Process> getProcessHistoryPageOfSelf(String keyword, String username) {
        return processMapper.getProcessHistoryPageOfSelf(keyword,username);
    }

    public Process getByFlowIdAndCrtUserWithoutState(String flowId,String username) {
        return processMapper.getByFlowIdAndCrtUserWithoutState(flowId,username);
    }
}
