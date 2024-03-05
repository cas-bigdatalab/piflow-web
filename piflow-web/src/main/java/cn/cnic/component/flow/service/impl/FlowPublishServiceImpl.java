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
                    if(null == property.getType()){
                        property.setType(FlowStopsPublishingPropertyType.COMMON.getValue());
                    }
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
                        if(null == property.getType()){
                            property.setType(FlowStopsPublishingPropertyType.COMMON.getValue());
                        }
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
                        if(null == property.getType()){
                            property.setType(FlowStopsPublishingPropertyType.COMMON.getValue());
                        }
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

        List<StopPublishingVo> stops = flowPublishing.getProperties().stream()
                .collect(Collectors.groupingBy(FlowStopsPublishingProperty::getStopId))
                .entrySet()
                .stream()
                .map(entry -> {
                    StopPublishingVo vo = new StopPublishingVo();
                    vo.setStopId(entry.getKey());
                    vo.setStopName(entry.getValue().get(0).getStopName());
                    vo.setBak1(entry.getValue().get(0).getBak1());
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
    public String run(FlowPublishingVo flowPublishingVo) throws Exception {
        logger.info("=======run flow start===============");
        String username = SessionUserUtil.getCurrentUsername();
        Flow flowById = flowDomain.getFlowById(flowPublishingVo.getFlowId());
        //根据flowPublishingVo更改flow的id值和包含参数的customValue值
        flowById.setId(flowPublishingVo.getId());

        //校验文件类的参数是否有上传文件并将所有输出类型参数的customValue进行改造，重命名
        for (StopPublishingVo stop : flowPublishingVo.getStops()) {
            for (StopPublishingPropertyVo propertyVo : stop.getStopPublishingPropertyVos()) {
                //校验文件类的参数是否有上传文件,如果没有上传，用样例文件，所以这里就不校验了
//                if (FlowStopsPublishingPropertyType.FILE.getValue().equals(propertyVo.getType())) {
//                    File byPath = fileDomain.getByPath(propertyVo.getCustomValue());
//                    if (ObjectUtils.isEmpty(byPath)) {
//                        return ReturnMapUtils.setFailedMsgRtnJsonStr("run faild !! your have not uploaded file for your property,property name is:【" + propertyVo.getName() + "】");
//                    }
//                }
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
        final Process process = ProcessUtils.flowToProcess(flowById, username, false);
        if (null == process) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.CONVERSION_FAILED_MSG());
        }
        RunModeType runModeType = RunModeType.RUN;
        process.setRunModeType(runModeType);
        process.setId(UUIDUtils.getUUID32());
        int updateProcess = processDomain.addProcess(process);
        if (updateProcess <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.CONVERSION_FAILED_MSG());
        }
        StringBuffer checkpoint = new StringBuffer();
        List<Stops> stopsList = flowById.getStopsList();
        for (Stops stops : stopsList) {
            stops.getIsCheckpoint();
            if (null == stops.getIsCheckpoint() || !stops.getIsCheckpoint()) {
                continue;
            }
            if (StringUtils.isNotBlank(checkpoint)) {
                checkpoint.append(",");
            }
            checkpoint.append(stops.getName());
        }
        String processId = process.getId();
        //改成异步获取appid
        CompletableFuture<Map<String, Object>> starFlowFuture = null; //定义future结构
        starFlowFuture = CompletableFuture.supplyAsync(() ->
                flowImpl.startFlow(process, checkpoint.toString(), runModeType));
        // 当 CompletableFuture 完成时更新appID，生成数据产品记录
        starFlowFuture.whenComplete((result, throwable) -> {
            // 检查是否有异常抛出
            if (throwable != null) {
                logger.error("start flow failed: " + throwable.getMessage());
            } else {
                if (null == result || 200 != ((Integer) result.get("code"))) {
                    processDomain.updateProcessEnableFlag(username, true, processId);
                } else {
                    SysParamsCache.STARTED_PROCESS.put(processId,(String) result.get("appId"));
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
//        Map<String, Object> stringObjectMap = flowImpl.startFlow(process, checkpoint.toString(), runModeType);
//        if (null == stringObjectMap || 200 != ((Integer) stringObjectMap.get("code"))) {
//            processDomain.updateProcessEnableFlag(username, true, processId);
//            return ReturnMapUtils.setFailedMsgRtnJsonStr((String) stringObjectMap.get("errorMsg"));
//        }
//        Process process2 = processDomain.getProcessById(username, true, processId);
//        process2.setLastUpdateDttm(new Date());
//        process2.setLastUpdateUser(username);
////        process.setAppId((String) stringObjectMap.get("appId"));
////        process.setProcessId((String) stringObjectMap.get("appId"));
//        process2.setState(ProcessState.INIT);
//        process2.setLastUpdateUser(username);
//        process2.setLastUpdateDttm(new Date());
//        processDomain.updateProcess(process);
//        //为所有发布的输出参数都创建一个数据产品记录
//        Date now = new Date();
//        List<DataProduct> dataProducts = new ArrayList<>();
//        flowPublishingVo.getStops().stream()
//                .flatMap(stop -> stop.getStopPublishingPropertyVos().stream())
//                .filter(property -> FlowStopsPublishingPropertyType.OUTPUT.getValue().equals(property.getType()))
//                .forEach(property -> {
//                    DataProduct dataProduct = new DataProduct();
//                    dataProduct.setId(snowflakeGenerator.next());
//                    dataProduct.setProcessId(processId);
//                    dataProduct.setPropertyId(Long.parseLong(property.getId()));
//                    dataProduct.setPropertyName(property.getName());
//                    dataProduct.setDatasetUrl(property.getCustomValue());
//                    dataProduct.setPermission(DataProductPermission.OPEN.getValue());
//                    dataProduct.setState(DataProductState.CREATING.getValue());
//                    dataProduct.setCrtDttm(now);
//                    dataProduct.setCrtDttmStr(DateUtils.dateTimesToStr(now));
//                    dataProduct.setCrtUser(username);
//                    dataProduct.setLastUpdateDttm(now);
//                    dataProduct.setLastUpdateDttmStr(DateUtils.dateTimeToStr(now));
//                    dataProduct.setLastUpdateUser(username);
//                    dataProduct.setEnableFlag(true);
//                    dataProduct.setEnableFlagNum(1);
//                    dataProduct.setVersion(0L);
//                    dataProducts.add(dataProduct);
//                });
//
//        dataProductDomain.addBatch(dataProducts);
        logger.info("========run flow finish==================");
        Map<String, String> result = new HashMap<>();
        result.put("processId", processId);
//        result.put("appId", process.getAppId());
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
