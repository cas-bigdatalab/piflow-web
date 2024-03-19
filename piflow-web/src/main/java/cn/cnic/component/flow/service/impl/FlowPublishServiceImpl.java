package cn.cnic.component.flow.service.impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.Eunm.*;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.dataProduct.domain.DataProductDomain;
import cn.cnic.component.dataProduct.domain.DataProductTypeDomain;
import cn.cnic.component.dataProduct.entity.DataProduct;
import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import cn.cnic.component.flow.domain.FlowDomain;
import cn.cnic.component.flow.domain.FlowPublishDomain;
import cn.cnic.component.flow.domain.FlowStopsPublishingPropertyDomain;
import cn.cnic.component.flow.domain.StopsDomain;
import cn.cnic.component.flow.entity.*;
import cn.cnic.component.flow.mapper.PathsMapper;
import cn.cnic.component.flow.service.IFlowPublishService;
import cn.cnic.component.flow.vo.FlowPublishingVo;
import cn.cnic.component.flow.vo.StopPublishingPropertyVo;
import cn.cnic.component.flow.vo.StopPublishingVo;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.entity.ProcessStopProperty;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.system.domain.FileDomain;
import cn.cnic.component.system.entity.File;
import cn.cnic.third.service.IFlow;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class FlowPublishServiceImpl implements IFlowPublishService {

    private Logger logger = LoggerUtil.getLogger();

    private final SnowflakeGenerator snowflakeGenerator;
    private final FlowPublishDomain flowPublishDomain;
    private final FileDomain fileDomain;
    private final FlowDomain flowDomain;
    private final DataProductTypeDomain dataProductTypeDomain;
    private final FlowStopsPublishingPropertyDomain flowStopsPublishingPropertyDomain;
    private final ProcessDomain processDomain;
    private final IFlow flowImpl;
    private final DataProductDomain dataProductDomain;
    private final StopsDomain stopsDomain;
    private final PathsMapper pathsMapper;

    @Autowired
    public FlowPublishServiceImpl(SnowflakeGenerator snowflakeGenerator, FlowPublishDomain flowPublishDomain, FileDomain fileDomain, FlowDomain flowDomain, DataProductTypeDomain dataProductTypeDomain, FlowStopsPublishingPropertyDomain flowStopsPublishingPropertyDomain, ProcessDomain processDomain, IFlow flowImpl, DataProductDomain dataProductDomain, StopsDomain stopsDomain, PathsMapper pathsMapper) {
        this.snowflakeGenerator = snowflakeGenerator;
        this.flowPublishDomain = flowPublishDomain;
        this.fileDomain = fileDomain;
        this.flowDomain = flowDomain;
        this.dataProductTypeDomain = dataProductTypeDomain;
        this.flowStopsPublishingPropertyDomain = flowStopsPublishingPropertyDomain;
        this.processDomain = processDomain;
        this.flowImpl = flowImpl;
        this.dataProductDomain = dataProductDomain;
        this.stopsDomain = stopsDomain;
        this.pathsMapper = pathsMapper;
    }

    @Override
    public String publishingStops(FlowPublishingVo vo) {
        List<StopPublishingPropertyVo> returnData = new ArrayList<>();
        /*
         * 1.文件上传 2.记录发布信息 3.新增数据产品类型关联
         */
        //获取HDFS地址
//        String defaultFs = FileUtils.getDefaultFs();
        String username = SessionUserUtil.getCurrentUsername();
        String idStr = vo.getId();
        Long id = null;
        if (StringUtils.isBlank(idStr)) {
            //新增
            //同一flow下不能发布相同名字的发布流水线
            FlowPublishing sameName = flowPublishDomain.getByNameAndFlowId(vo);
            if (ObjectUtils.isNotEmpty(sameName)) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("Flow release name duplicate, please change the name or re-edit your flow release with the same name!!");
            }
            //新增发布信息
            Date now = new Date();
            FlowPublishing flowPublishing = new FlowPublishing();
            BeanUtils.copyProperties(vo, flowPublishing);
            id = snowflakeGenerator.next();
            flowPublishing.setId(id);
            flowPublishing.setCrtDttm(now);
            flowPublishing.setCrtUser(username);
            flowPublishing.setEnableFlag(true);
            flowPublishing.setLastUpdateDttm(now);
            flowPublishing.setLastUpdateUser(username);
            if (StringUtils.isBlank(flowPublishing.getDescription())) {
                flowPublishing.setDescription("");
            }

            List<FlowStopsPublishingProperty> properties = new ArrayList<>();
//            List<File> files = new ArrayList<>();
            for (StopPublishingVo stop : vo.getStops()) {
                String stopId = stop.getStopId();
                for (StopPublishingPropertyVo stopPublishingPropertyVo : stop.getStopPublishingPropertyVos()) {
                    FlowStopsPublishingProperty property = new FlowStopsPublishingProperty();
                    BeanUtils.copyProperties(stopPublishingPropertyVo, property);
                    Long propertyId = snowflakeGenerator.next();
                    property.setId(propertyId);
                    property.setPublishingId(id);
                    property.setStopId(stopId);
                    property.setStopName(stop.getStopName());
                    property.setStopBundle(stop.getStopBundle());
                    if (null == property.getType()) {
                        property.setType(FlowStopsPublishingPropertyType.COMMON.getValue());
                    }
                    //设置排序
                    property.setBak1(stop.getBak1());
//                    if (StringUtils.isBlank(property.getBak1())) {
//                        property.setBak1(stop.getBak1());
//                    }
                    //设置分组名
                    property.setBak2(stop.getBak2());
                    property.setBak3(stop.getBak3());
                    property.setCrtDttm(now);
                    property.setCrtUser(username);
                    property.setEnableFlag(true);
                    property.setLastUpdateDttm(now);
                    property.setLastUpdateUser(username);
                    property.setCrtDttmStr(DateUtils.dateTimesToStr(now));
                    property.setLastUpdateDttmStr(DateUtils.dateTimesToStr(now));
                    property.setEnableFlagNum(1);
                    properties.add(property);

                    StopPublishingPropertyVo rerturnVo = new StopPublishingPropertyVo();
                    BeanUtils.copyProperties(property, rerturnVo);
                    rerturnVo.setId(propertyId.toString());
                    rerturnVo.setPublishingId(id.toString());
                    returnData.add(rerturnVo);

                }
            }
            //新增数据产品类型关联
            ProductTypeAssociate associate = new ProductTypeAssociate();
            associate.setId(snowflakeGenerator.next());
            associate.setProductTypeId(vo.getProductTypeId());
            associate.setProductTypeName(vo.getProductTypeName());
            associate.setAssociateId(id.toString());
            associate.setAssociateType(ProductTypeAssociateType.FLOW.getValue());
            associate.setState(ProductTypeAssociateState.USABLE.getValue());

            flowPublishDomain.save(flowPublishing);
            if (CollectionUtils.isNotEmpty(properties)) {
                flowStopsPublishingPropertyDomain.saveBatch(properties);
            }
            dataProductTypeDomain.insertAssociate(associate);
        } else {
            id = Long.parseLong(idStr);
            //更新编辑后发布
            //更新发布信息
            Date now = new Date();
            FlowPublishing flowPublishing = new FlowPublishing();
            BeanUtils.copyProperties(vo, flowPublishing);
            flowPublishing.setId(Long.parseLong(vo.getId()));
            flowPublishing.setLastUpdateDttm(now);
            flowPublishing.setLastUpdateUser(username);
            flowPublishing.setVersion(vo.getVersion());

            List<FlowStopsPublishingProperty> insertProperties = new ArrayList<>();
            List<FlowStopsPublishingProperty> updateProperties = new ArrayList<>();
            for (StopPublishingVo stop : vo.getStops()) {
                String stopId = stop.getStopId();
                for (StopPublishingPropertyVo stopPublishingPropertyVo : stop.getStopPublishingPropertyVos()) {
                    FlowStopsPublishingProperty property = new FlowStopsPublishingProperty();
                    BeanUtils.copyProperties(stopPublishingPropertyVo, property);
                    if (StringUtils.isBlank(stopPublishingPropertyVo.getId())) {
                        //新增参数
                        Long propertyId = snowflakeGenerator.next();
                        property.setId(propertyId);
                        property.setPublishingId(id);
                        property.setStopId(stopId);
                        property.setStopName(stop.getStopName());
                        property.setStopBundle(stop.getStopBundle());
                        if (null == property.getType()) {
                            property.setType(FlowStopsPublishingPropertyType.COMMON.getValue());
                        }
                        //设置排序
                        property.setBak1(stop.getBak1());
//                        if (StringUtils.isBlank(property.getBak1())) {
//                            property.setBak1(stop.getBak1());
//                        }
                        //设置分组名
                        property.setBak2(stop.getBak2());
                        property.setBak3(stop.getBak3());
                        property.setCrtDttm(now);
                        property.setCrtUser(username);
                        property.setEnableFlag(true);
                        property.setLastUpdateDttm(now);
                        property.setLastUpdateUser(username);
                        property.setCrtDttmStr(DateUtils.dateTimesToStr(now));
                        property.setLastUpdateDttmStr(DateUtils.dateTimesToStr(now));
                        property.setEnableFlagNum(1);
                        insertProperties.add(property);
                        StopPublishingPropertyVo rerturnVo = new StopPublishingPropertyVo();
                        BeanUtils.copyProperties(property, rerturnVo);
                        rerturnVo.setId(propertyId.toString());
                        rerturnVo.setPublishingId(idStr);
                        returnData.add(rerturnVo);
                    } else {
                        //更新参数
                        property.setId(Long.parseLong(stopPublishingPropertyVo.getId()));
                        property.setPublishingId(Long.parseLong(stopPublishingPropertyVo.getPublishingId()));
                        if (null == property.getType()) {
                            property.setType(FlowStopsPublishingPropertyType.COMMON.getValue());
                        }
                        //设置排序
                        property.setBak1(stop.getBak1());
//                        if (StringUtils.isBlank(property.getBak1())) {
//                            property.setBak1(stop.getBak1());
//                        }
                        //设置分组名
                        property.setBak2(stop.getBak2());
                        property.setBak3(stop.getBak3());
                        property.setLastUpdateDttm(now);
                        property.setLastUpdateUser(username);
                        property.setVersion(property.getVersion() + 1);
                        updateProperties.add(property);
                        returnData.add(stopPublishingPropertyVo);
                    }

                }
            }
            //更新数据产品类型关联
            ProductTypeAssociate associate = dataProductTypeDomain.getAssociateByAssociateId(id.toString());
            associate.setProductTypeId(vo.getProductTypeId());
            associate.setProductTypeName(vo.getProductTypeName());

            flowPublishDomain.update(flowPublishing);
            if (CollectionUtils.isNotEmpty(insertProperties)) {
                flowStopsPublishingPropertyDomain.saveBatch(insertProperties);
            }
            if (CollectionUtils.isNotEmpty(updateProperties)) {
                flowStopsPublishingPropertyDomain.updateBatch(updateProperties);
            }

            dataProductTypeDomain.updateAssociate(associate);
        }


        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", returnData);
    }

    @Override
    public String getPublishingById(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        FlowPublishing flowPublishing = flowPublishDomain.getFullInfoById(id);
        //查询发布流水线的关联封面
        FlowPublishingVo result = new FlowPublishingVo();
        BeanUtils.copyProperties(flowPublishing, result);
        result.setId(flowPublishing.getId().toString());
        //获取封面
        File flowPublishingCover = fileDomain.getByAssociateId(flowPublishing.getId().toString(), FileAssociateType.FLOW_PUBLISHING_COVER.getValue());
        if (ObjectUtils.isNotEmpty(flowPublishingCover)) {
            result.setCoverFileId(flowPublishingCover.getId().toString());
            result.setCoverFileName(flowPublishingCover.getFileName());
        }
        //获取说明书
        File flowPublishingInstruction = fileDomain.getByAssociateId(flowPublishing.getId().toString(), FileAssociateType.FLOW_PUBLISHING_INSTRUCTION.getValue());
        if (ObjectUtils.isNotEmpty(flowPublishingInstruction)) {
            result.setInstructionFileId(flowPublishingInstruction.getId().toString());
            result.setInstructionFileName(flowPublishingInstruction.getFileName());
        }
        //根据username和flowId以及state为空，查看是否有暂存的数据，如果有暂存的数据，将暂存的customValue赋给property
        Process process = processDomain.getByFlowIdAndCrtUserWithoutState(id, username);

        List<StopPublishingVo> stops = flowPublishing.getProperties().stream()
                .collect(Collectors.groupingBy(FlowStopsPublishingProperty::getStopId))
                .entrySet()
                .stream()
                .map(entry -> {
                    StopPublishingVo vo = new StopPublishingVo();
                    vo.setStopId(entry.getKey());
                    vo.setStopName(entry.getValue().get(0).getStopName());
                    vo.setBak1(entry.getValue().get(0).getBak1());
                    vo.setBak2(entry.getValue().get(0).getBak2());
                    vo.setBak3(entry.getValue().get(0).getBak3());
                    Optional<ProcessStop> processStop = ObjectUtils.isEmpty(process) ? Optional.empty() : process.getProcessStopList().stream().filter(x -> vo.getStopId().equals(x.getFlowStopId())).findFirst();
                    final Map<String, ProcessStopProperty> propertyMap = processStop.map(stop -> stop.getProcessStopPropertyList().stream()
                                    .collect(Collectors.toMap(ProcessStopProperty::getName, propertyVo -> propertyVo)))
                            .orElseGet(HashMap::new);
                    vo.setStopPublishingPropertyVos(entry.getValue().stream()
                            .map(property -> {
                                StopPublishingPropertyVo propertyVo = new StopPublishingPropertyVo();
                                BeanUtils.copyProperties(property, propertyVo);
                                propertyVo.setId(property.getId().toString());
                                propertyVo.setPublishingId(property.getPublishingId().toString());
                                Long fileId = property.getFileId();
                                if (fileId != null) {
                                    propertyVo.setFileId(fileId.toString());
                                    propertyVo.setFileName(property.getFileName());
                                }
                                //赋值customValue
                                ProcessStopProperty processStopProperty = propertyMap.get(propertyVo.getPropertyName());
                                if (ObjectUtils.isNotEmpty(processStopProperty)) {
                                    propertyVo.setIsTempSave(true);
                                    propertyVo.setCustomValue(processStopProperty.getCustomValue());
                                }
                                return propertyVo;
                            })
                            .sorted(Comparator.comparing(StopPublishingPropertyVo::getPropertySort))
                            .collect(Collectors.toList()));
                    return vo;
                })
                .sorted(Comparator.comparing(vo -> Integer.parseInt(vo.getBak1())))
                .collect(Collectors.toList());
        result.setStops(stops);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", result);
    }

    @Override
    public String getListByPage(FlowPublishingVo flowPublishingVo) {
        Page<FlowPublishingVo> page = PageHelper.startPage(flowPublishingVo.getPage(), flowPublishingVo.getLimit(), "last_update_dttm desc");
        flowPublishDomain.getListByPage(flowPublishingVo.getKeyword());
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    @Override
    public String delete(String id) {
        //删除发布信息，数据类型关联信息，文件信息
        Long flowPublishingId = Long.parseLong(id);
        flowPublishDomain.delete(flowPublishingId);
        flowStopsPublishingPropertyDomain.deleteByPublishingId(flowPublishingId);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.DELETE_SUCCEEDED_MSG());
    }

    @Override
    public String getFlowPublishingListPageByProductTypeId(FlowPublishingVo flowPublishingVo) {
        List<DataProductType> dataProductTypeList = dataProductTypeDomain.getDataProductTypeAndAllChildById(flowPublishingVo.getProductTypeId());
        Page<FlowPublishingVo> page = PageHelper.startPage(flowPublishingVo.getPage(), flowPublishingVo.getLimit(), "product_type_id, last_update_dttm desc");
        flowPublishDomain.getListByProductTypeIds(flowPublishingVo.getKeyword(), dataProductTypeList.stream().map(DataProductType::getId).collect(Collectors.toList()));

        List<FlowPublishingVo> result = page.getResult();
        if (CollectionUtils.isNotEmpty(result)) {
            result.forEach(publishingVo -> {
                //获取说明书
                File flowPublishingInstruction = fileDomain.getByAssociateId(publishingVo.getId(), FileAssociateType.FLOW_PUBLISHING_INSTRUCTION.getValue());
                if (ObjectUtils.isNotEmpty(flowPublishingInstruction)) {
                    publishingVo.setInstructionFileId(flowPublishingInstruction.getId().toString());
                    publishingVo.setInstructionFileName(flowPublishingInstruction.getFileName());
                }
            });
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamWithChangeResultRtnStr(page, new ArrayList<Object>(result), rtnMap);
    }

    @Override
    public String tempSave(FlowPublishingVo flowPublishingVo) throws Exception {
        //将数据暂存到flow_process以及flow_process_stop_property表中,  不设置状态，状态为空，用来判断是否有暂存，如果有，先更新一版暂存的，运行的时候复制一份这个暂存的运行
        logger.info("=======temp save flow start===============");
        if (ObjectUtils.isEmpty(tempSaveProcess(flowPublishingVo)))
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        logger.info("=======temp save flow finish===============");
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    private Process tempSaveProcess(FlowPublishingVo flowPublishingVo) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        //根据username和flowId以及state为空，查看是否有暂存的数据，如果有，更新，如果没有，新增
        Process oldProcess = processDomain.getByFlowIdAndCrtUserWithoutState(flowPublishingVo.getId(), username);
        if (ObjectUtils.isEmpty(oldProcess)) {
            //新增process
            oldProcess = initProcess(flowPublishingVo, username);
            //不设置状态，状态为空，用来判断是否有暂存，如果有，先更新一版暂存的，运行的时候复制一份这个暂存的运行
            int updateProcess = processDomain.addProcess(oldProcess);
            if (updateProcess <= 0) {
                return null;
            }
        } else {
            //更新process
            //将所有输出类型参数的customValue进行改造，重命名
            for (StopPublishingVo stop : flowPublishingVo.getStops()) {
                for (StopPublishingPropertyVo propertyVo : stop.getStopPublishingPropertyVos()) {
                    if (FlowStopsPublishingPropertyType.OUTPUT.getValue().equals(propertyVo.getType())) {
                        String customValue = propertyVo.getCustomValue();
                        if (customValue.contains(".")) {
                            //file
                            String[] split = customValue.split("\\.");
                            customValue = split[0] + "_" + snowflakeGenerator.next() + "." + split[1];
                            propertyVo.setCustomValue(customValue);
                        } else {
                            //file dir /a/b/->/a/b/12345678902    /a/b->a/b12345678902
                            customValue = customValue + snowflakeGenerator.next();
                            propertyVo.setCustomValue(customValue);
                        }
                    }
                }
            }
            //将customValue值赋值到process的stop property中
            Map<String, StopPublishingVo> stopPublishingVoMap = flowPublishingVo.getStops().stream().collect(Collectors.toMap(StopPublishingVo::getStopId, vo -> vo));
            for (ProcessStop stop : oldProcess.getProcessStopList()) {
                if (stopPublishingVoMap.containsKey(stop.getFlowStopId())) {
                    Map<String, StopPublishingPropertyVo> publishingPropertyVoMap = stopPublishingVoMap.get(stop.getFlowStopId()).getStopPublishingPropertyVos().stream().collect(Collectors.toMap(StopPublishingPropertyVo::getPropertyName, vo -> vo));
                    for (ProcessStopProperty property : stop.getProcessStopPropertyList()) {
                        if (publishingPropertyVoMap.containsKey(property.getName())) {
                            property.setCustomValue(publishingPropertyVoMap.get(property.getName()).getCustomValue());
                        }
                    }
                }
            }
            oldProcess.setLastUpdateDttm(new Date());
            int updateProcess = processDomain.updateProcess(oldProcess);
            if (updateProcess <= 0) {
                return null;
            }
        }
        return oldProcess;
    }

    private Process initProcess(FlowPublishingVo flowPublishingVo, String username) {
        Process process;
        Flow flowById = flowDomain.getFlowById(flowPublishingVo.getFlowId());
        //根据flowPublishingVo更改flow的id值和包含参数的customValue值
        flowById.setId(flowPublishingVo.getId());
        flowById.setName(flowPublishingVo.getName());

        //校验文件类的参数是否有上传文件并将所有输出类型参数的customValue进行改造，重命名,,,校验文件类的参数是否有上传文件,如果没有上传，用样例文件，所以这里就不校验了
        for (StopPublishingVo stop : flowPublishingVo.getStops()) {
            for (StopPublishingPropertyVo propertyVo : stop.getStopPublishingPropertyVos()) {
                if (FlowStopsPublishingPropertyType.OUTPUT.getValue().equals(propertyVo.getType())) {
                    String customValue = propertyVo.getCustomValue();
                    if (customValue.contains(".")) {
                        //file
                        String[] split = customValue.split("\\.");
                        customValue = split[0] + "_" + snowflakeGenerator.next() + "." + split[1];
                        propertyVo.setCustomValue(customValue);
                    } else {
                        //file dir /a/b/->/a/b/12345678902    /a/b->a/b12345678902
                        customValue = customValue + snowflakeGenerator.next();
                        propertyVo.setCustomValue(customValue);
                    }
                }
            }
        }
        //将customValue值赋值到flow的stop property中
        Map<String, StopPublishingVo> stopPublishingVoMap = flowPublishingVo.getStops().stream().collect(Collectors.toMap(StopPublishingVo::getStopId, vo -> vo));
        for (Stops stops : flowById.getStopsList()) {
            if (stopPublishingVoMap.containsKey(stops.getId())) {
                Map<String, StopPublishingPropertyVo> publishingPropertyVoMap = stopPublishingVoMap.get(stops.getId()).getStopPublishingPropertyVos().stream().collect(Collectors.toMap(StopPublishingPropertyVo::getPropertyId, vo -> vo));
                for (Property property : stops.getProperties()) {
                    if (publishingPropertyVoMap.containsKey(property.getId())) {
                        property.setCustomValue(publishingPropertyVoMap.get(property.getId()).getCustomValue());
                    }
                }
            }
        }
        process = ProcessUtils.flowToProcess(flowById, username, false);
        RunModeType runModeType = RunModeType.RUN;
        process.setRunModeType(runModeType);
        process.setId(UUIDUtils.getUUID32());
        return process;
    }

    @Override
    public String run(FlowPublishingVo flowPublishingVo) throws Exception {
        logger.info("=======run flow start===============");
        String username = SessionUserUtil.getCurrentUsername();
        //先更新暂存，运行的时候复制一份这个暂存的运行,如果没有暂存，不新增暂存，如果有，更新
        Process oldProcess = processDomain.getByFlowIdAndCrtUserWithoutState(flowPublishingVo.getId(), username);
        if (ObjectUtils.isEmpty(oldProcess)) {
            //新增process
            oldProcess = initProcess(flowPublishingVo, username);
        }else {
            oldProcess = tempSaveProcess(flowPublishingVo);
        }
        if (ObjectUtils.isEmpty(oldProcess))
            return ReturnMapUtils.setFailedMsgRtnJsonStr("process create failed!!");

        final Process process = ProcessUtils.copyProcess(oldProcess, username, RunModeType.RUN, true);
        if (null == process) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.CONVERSION_FAILED_MSG());
        }
        int updateProcess = processDomain.addProcess(process);
        if (updateProcess <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.CONVERSION_FAILED_MSG());
        }
        String processId = process.getId();
        //改成异步获取appid
        CompletableFuture<Map<String, Object>> starFlowFuture = null; //定义future结构
        starFlowFuture = CompletableFuture.supplyAsync(() ->
                flowImpl.startFlow(process, "", RunModeType.RUN));
        // 当 CompletableFuture 完成时更新appID，生成数据产品记录
        starFlowFuture.whenComplete((result, throwable) -> {
            // 检查是否有异常抛出
            if (throwable != null) {
                logger.error("start flow failed: " + throwable.getMessage());
            } else {
                if (null == result || 200 != ((Integer) result.get("code"))) {
                    processDomain.updateProcessEnableFlag(username, true, processId);
                } else {
                    SysParamsCache.STARTED_PROCESS.put(processId, (String) result.get("appId"));
                    Process process1 = processDomain.getProcessById(username, true, processId);
                    process1.setLastUpdateDttm(new Date());
                    process1.setLastUpdateUser(username);
                    process1.setAppId((String) result.get("appId"));
                    process1.setProcessId((String) result.get("appId"));
                    process1.setState(ProcessState.STARTED);
                    process1.setLastUpdateUser(username);
                    process1.setLastUpdateDttm(new Date());
                    try {
                        processDomain.updateProcess(process1);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    //为所有发布的输出参数都创建一个数据产品记录
                    Date now = new Date();
                    List<DataProduct> dataProducts = new ArrayList<>();
                    flowPublishingVo.getStops().stream()
                            .flatMap(stop -> stop.getStopPublishingPropertyVos().stream())
                            .filter(property -> FlowStopsPublishingPropertyType.OUTPUT.getValue().equals(property.getType()))
                            .forEach(property -> {
                                DataProduct dataProduct = new DataProduct();
                                dataProduct.setId(snowflakeGenerator.next());
                                dataProduct.setProcessId(processId);
                                dataProduct.setPropertyId(Long.parseLong(property.getId()));
                                dataProduct.setPropertyName(property.getName());
                                dataProduct.setDatasetUrl(property.getCustomValue());
                                dataProduct.setPermission(DataProductPermission.OPEN.getValue());
                                dataProduct.setState(DataProductState.CREATING.getValue());
                                dataProduct.setCrtDttm(now);
                                dataProduct.setCrtDttmStr(DateUtils.dateTimesToStr(now));
                                dataProduct.setCrtUser(username);
                                dataProduct.setLastUpdateDttm(now);
                                dataProduct.setLastUpdateDttmStr(DateUtils.dateTimeToStr(now));
                                dataProduct.setLastUpdateUser(username);
                                dataProduct.setEnableFlag(true);
                                dataProduct.setEnableFlagNum(1);
                                dataProduct.setVersion(0L);
                                dataProducts.add(dataProduct);
                            });
                    dataProductDomain.addBatch(dataProducts);
                }
            }
        });
        logger.info("========run flow finish==================");
        Map<String, String> result = new HashMap<>();
        result.put("processId", processId);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", result);
    }

    /**
     * @param file:
     * @param stopPublishingPropertyVo:
     * @param now:
     * @param username:
     * @param files:
     * @param defaultFs:
     * @return void
     * @author tianyao
     * @description TODO
     * @date 2024/2/20 15:31
     */
    private void uploadPropertyFiles(MultipartFile[] file, Long propertyId, StopPublishingPropertyVo
            stopPublishingPropertyVo, Date now, String username, List<File> files, String defaultFs) {
        if (file != null && file.length > 0) {
            String originFileName = stopPublishingPropertyVo.getFileName();
            if (StringUtils.isNotBlank(originFileName)) {
                String fileName = originFileName;
                File isExist = fileDomain.getByName(originFileName);
                if (ObjectUtils.isNotEmpty(isExist)) {
                    String[] split = originFileName.split("\\.");
                    fileName = split[0] + snowflakeGenerator.next() + "." + split[1];
                }
                for (MultipartFile multipartFile : file) {
                    String originalFilename = multipartFile.getOriginalFilename();
                    originalFilename = FileUtils.getFileName(originalFilename);
                    if (originFileName.equals(originalFilename)) {
                        File insertFile = new File();
                        insertFile.setId(snowflakeGenerator.next());
                        insertFile.setFileName(fileName);
                        insertFile.setFileType(multipartFile.getContentType());
                        insertFile.setFilePath(SysParamsCache.FILE_STORAGE_PATH + fileName);
                        insertFile.setAssociateId(propertyId.toString());
                        insertFile.setAssociateType(FileAssociateType.FLOW_PUBLISHING_PROPERTY_TEMPLATE.getValue());
                        insertFile.setCrtDttm(now);
                        insertFile.setCrtUser(username);
                        insertFile.setEnableFlag(true);
                        insertFile.setLastUpdateDttm(now);
                        insertFile.setLastUpdateUser(username);
                        insertFile.setCrtDttmStr(DateUtils.dateTimesToStr(now));
                        insertFile.setLastUpdateDttmStr(DateUtils.dateTimesToStr(now));
                        insertFile.setEnableFlagNum(1);
                        files.add(insertFile);
                        //上传文件至hdfs
                        FileUtils.saveFileToHdfs(multipartFile, fileName, SysParamsCache.FILE_STORAGE_PATH, defaultFs);
                        break;
                    }
                }
            }
        }
    }
}
