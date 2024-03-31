package cn.cnic.component.system.service.Impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.Eunm.FileAssociateType;
import cn.cnic.common.Eunm.ProductTypeAssociateState;
import cn.cnic.common.Eunm.ProductTypeAssociateType;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import cn.cnic.component.dataProduct.vo.DataProductTypeVo;
import cn.cnic.component.system.domain.FileDomain;
import cn.cnic.component.system.entity.File;
import cn.cnic.component.system.service.IFileService;
import cn.cnic.component.system.vo.FileVo;
import cn.cnic.component.template.entity.FlowTemplate;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerUtil.getLogger();

    private final FileDomain fileDomain;

    private final SnowflakeGenerator snowflakeGenerator;

    @Autowired
    public FileServiceImpl(FileDomain fileDomain, SnowflakeGenerator snowflakeGenerator) {
        this.fileDomain = fileDomain;
        this.snowflakeGenerator = snowflakeGenerator;
    }

    @Override
    public String uploadFile(MultipartFile file, Integer associateType, String associateId) {
        String username = SessionUserUtil.getCurrentUsername();
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NOT_LOGIN);
        }
        //先删除原有的绑定文件，同一个type和id只能绑定一个文件,除了流水线发布参数（也就是运行时上传文件）
        if (!Objects.equals(FileAssociateType.FLOW_PUBLISHING_PROPERTY.getValue(), associateType)) {
            fileDomain.deleteByAssociateId(associateId, associateType);
        }
        //上传文件,不管有没有同名文件，通通重命名
        String originalFilename = file.getOriginalFilename();
        originalFilename = FileUtils.getFileName(originalFilename);
//        String fileName = originalFilename;
//        File isExist = fileDomain.getByName(originalFilename);
//        if (ObjectUtils.isNotEmpty(isExist)) {
        String[] split = originalFilename.split("\\.");
        String fileName = split[0] + "_" + snowflakeGenerator.next() + "." + split[1];
//        }
        //上传文件到hdfs，数据产品分类封面和数据产品封面放在服务器上
        String path = "";
        if (associateType.equals(FileAssociateType.DATA_PRODUCT_TYPE_COVER.getValue()) || associateType.equals(FileAssociateType.DATA_PRODUCT_COVER.getValue())) {
            path = SysParamsCache.FILE_PATH;
            FileUtils.uploadRtnMap(file, path, fileName);
            path = SysParamsCache.SYS_CONTEXT_PATH + "/files/";
        } else {
            path = SysParamsCache.FILE_STORAGE_PATH;
            FileUtils.saveFileToHdfs(file, fileName, path, FileUtils.getDefaultFs());
        }
        //新增file记录
        File insertFile = new File();
        Date now = new Date();
        insertFile.setId(snowflakeGenerator.next());
        insertFile.setFileName(fileName);
        insertFile.setFileType(file.getContentType());
        insertFile.setFilePath(path + fileName);
        insertFile.setAssociateId(associateId);
        insertFile.setAssociateType(associateType);
        insertFile.setCrtDttm(now);
        insertFile.setCrtUser(username);
        insertFile.setEnableFlag(true);
        insertFile.setLastUpdateDttm(now);
        insertFile.setLastUpdateUser(username);
        insertFile.setCrtDttmStr(DateUtils.dateTimesToStr(now));
        insertFile.setLastUpdateDttmStr(DateUtils.dateTimesToStr(now));
        insertFile.setEnableFlagNum(1);
        int i = fileDomain.save(insertFile);
        if (i > 0) {
            FileVo vo = new FileVo();
            BeanUtils.copyProperties(insertFile, vo);
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", vo);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ADD_ERROR_MSG());
        }
    }

    @Override
    public void getFileById(HttpServletResponse response, String id) {
        File file = fileDomain.getById(id);
        FileUtils.downloadFileFromHdfs(response, file.getFilePath(), file.getFileName(), FileUtils.getDefaultFs());
    }

    @Override
    public void getFileByFilePath(HttpServletResponse response, String filePath) {
        FileUtils.downloadFileFromHdfs(response, filePath, null, FileUtils.getDefaultFs());
    }

    @Override
    public void getFileListByIds(HttpServletResponse response, String ids) {
        //获取多个文件并返回
        List<File> fileList = fileDomain.getListByIds(ids);
        if (CollectionUtils.isNotEmpty(fileList)) {
            DateUtils.dateTimesToStr(new Date());
            FileUtils.downloadFilesFromHdfs(response, fileList, "a.zip", FileUtils.getDefaultFs());
        } else {
            throw new RuntimeException("file not be found!!");
        }
    }

    private void addChildType(DataProductTypeVo vo, List<DataProductType> dataProductTypeList) {
        List<DataProductType> tempList = dataProductTypeList.stream().filter(type -> vo.getId().equals(type.getParentId())).collect(Collectors.toList());
        List<DataProductTypeVo> dataProductTypeVos = JSON.parseArray(JSON.toJSONString(tempList), DataProductTypeVo.class);
        vo.setChildren(dataProductTypeVos);
        dataProductTypeVos.forEach(typeVo -> addChildType(typeVo, dataProductTypeList));
    }


}
