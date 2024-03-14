package cn.cnic.component.dataProduct.service.impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.Eunm.FileAssociateType;
import cn.cnic.common.Eunm.ProductTypeAssociateState;
import cn.cnic.common.Eunm.ProductTypeAssociateType;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.dataProduct.domain.DataProductTypeDomain;
import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import cn.cnic.component.dataProduct.service.IDataProductTypeService;
import cn.cnic.component.dataProduct.vo.DataProductTypeVo;
import cn.cnic.component.system.domain.FileDomain;
import cn.cnic.component.system.entity.File;
import cn.cnic.component.system.service.IFileService;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class DataProductTypeServiceImpl implements IDataProductTypeService {

    private Logger logger = LoggerUtil.getLogger();

    private final DataProductTypeDomain dataProductTypeDomain;
    private final FileDomain fileDomain;
    private final IFileService fileServiceImpl;

    private final SnowflakeGenerator snowflakeGenerator;

    @Autowired
    public DataProductTypeServiceImpl(DataProductTypeDomain dataProductTypeDomain, FileDomain fileDomain, IFileService fileServiceImpl, SnowflakeGenerator snowflakeGenerator) {
        this.dataProductTypeDomain = dataProductTypeDomain;
        this.fileDomain = fileDomain;
        this.fileServiceImpl = fileServiceImpl;
        this.snowflakeGenerator = snowflakeGenerator;
    }


    @Override
    public String save(MultipartFile file, DataProductTypeVo dataProductTypeVo) {
        String username = SessionUserUtil.getCurrentUsername();
        Date now = new Date();
        if (null != dataProductTypeVo.getId()) {
            //编辑更新
            //删除并新增数据产品类型封面
            if(ObjectUtils.isNotEmpty(file)){
                fileServiceImpl.uploadFile(file, FileAssociateType.DATA_PRODUCT_TYPE_COVER.getValue(), dataProductTypeVo.getId().toString());
            }

            //更新数据产品类型
            DataProductType dataProductType = dataProductTypeDomain.getById(dataProductTypeVo.getId());
            //基本数据填充
            dataProductType.setParentId(dataProductTypeVo.getParentId());
            dataProductType.setName(dataProductTypeVo.getName());
            dataProductType.setDescription(dataProductTypeVo.getDescription());
            dataProductType.setLastUpdateUser(username);
            dataProductType.setLastUpdateDttm(now);
            int i = dataProductTypeDomain.update(dataProductType);
            if (i > 0) {
                return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
            } else {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
            }
        } else {
            //新增
            DataProductType dataProductType = new DataProductType();
            BeanUtils.copyProperties(dataProductTypeVo, dataProductType);
            if (null == dataProductType.getParentId() || 0L == dataProductType.getParentId()) {
                dataProductType.setParentId(0L);
            }
            //基础数据填充
            dataProductType.setCrtDttm(now);
            dataProductType.setCrtUser(username);
            dataProductType.setLastUpdateUser(username);
            dataProductType.setLastUpdateDttm(now);
            dataProductType.setEnableFlag(true);
            dataProductType.setVersion(0L);

            //同一ParentID下不允许有重名的类型
            DataProductType isExist = dataProductTypeDomain.getByNameAndParentId(dataProductType);
            if (ObjectUtils.isNotEmpty(isExist)) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("same name is not allowed in one parent category!!");
            }
            if(null == dataProductType.getLevel() || 0 == dataProductType.getLevel()){
                //递归获取分类的级别
                Integer level = getLevelWithParentId(dataProductType.getParentId());
                dataProductType.setLevel(level);
            }
            dataProductTypeDomain.save(dataProductType);
            Long typeId = dataProductType.getId();
            if (null != typeId && ObjectUtils.isNotEmpty(file)) {
                try {
                    //上传封面
                    fileServiceImpl.uploadFile(file, FileAssociateType.DATA_PRODUCT_TYPE_COVER.getValue(), typeId.toString());
                } catch (Exception e) {
                    logger.error("upload cover error!! e:====={}", e.getMessage());
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("upload cover error!!");
                }
            }
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.ADD_SUCCEEDED_MSG());
        }
    }

    private Integer getLevelWithParentId(Long parentId) {
        if (parentId == 0L) {
            return 1; // 最顶级为1级
        } else {
            //获取父级分类
            DataProductType parentType = dataProductTypeDomain.getById(parentId);
            if (parentType != null) {
                int parentLevel = getLevelWithParentId(parentType.getParentId()); // 递归获取父级分类的层级
                return parentLevel + 1; // 当前分类的层级为父级分类层级加1
            } else {
                return 0; // 错误情况，未找到父级分类
            }
        }
    }

    @Override
    public String delete(Long id) {
        int i = dataProductTypeDomain.delete(id);
        //删除关联的封面
        fileDomain.deleteByAssociateId(id.toString(),FileAssociateType.DATA_PRODUCT_TYPE_COVER.getValue());
        if (i > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.DELETE_SUCCEEDED_MSG());
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG());
        }
    }

    @Override
    public String get() {
        //获取分类
        String userName = SessionUserUtil.getCurrentUsername();
        List<DataProductType> dataProductTypeList = dataProductTypeDomain.getAll(userName);

        //构建树形结构返回
        List<DataProductTypeVo> result = new ArrayList<>();
        List<DataProductType> parentList = dataProductTypeList.stream()
                .filter(type -> type.getParentId().equals(0L))
                .sorted(Comparator.comparing(DataProductType::getId))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(parentList)) {
            for (DataProductType dataProductType : parentList) {
                DataProductTypeVo vo = new DataProductTypeVo();
                BeanUtils.copyProperties(dataProductType, vo);
                result.add(vo);
                addChildType(vo, dataProductTypeList);
            }
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data",result);
    }

    @Override
    public String preference(Long id, Integer preference) {
        String username = SessionUserUtil.getCurrentUsername();
        //新增偏好设置或更改偏好设置
        ProductTypeAssociate associate = dataProductTypeDomain.getAssociateByTypeIdAndUserName(id, username);
        if (ObjectUtils.isNotEmpty(associate)) {
            //更新
            int i = dataProductTypeDomain.updatePreference(associate.getId(), preference);
            if (i > 0) {
                return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
            } else {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
            }
        } else {
            DataProductType dataProductType = dataProductTypeDomain.getById(id);
            //新增
            associate = new ProductTypeAssociate();
            associate.setId(snowflakeGenerator.next());
            associate.setProductTypeId(id);
            associate.setProductTypeName(dataProductType.getName());
            associate.setAssociateType(ProductTypeAssociateType.USER.getValue());
            associate.setAssociateId(username);
            associate.setState(preference);
            int i = dataProductTypeDomain.insertAssociate(associate);
            if (i > 0) {
                return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.ADD_SUCCEEDED_MSG());
            } else {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ADD_ERROR_MSG());
            }
        }
    }

    private void addChildType(DataProductTypeVo vo, List<DataProductType> dataProductTypeList) {
        List<DataProductType> tempList = dataProductTypeList.stream()
                .filter(type -> vo.getId().equals(type.getParentId()))
                .sorted(Comparator.comparing(DataProductType::getId))
                .collect(Collectors.toList());
        List<DataProductTypeVo> dataProductTypeVos = JSON.parseArray(JSON.toJSONString(tempList), DataProductTypeVo.class);
        vo.setChildren(dataProductTypeVos);
        dataProductTypeVos.forEach(typeVo -> addChildType(typeVo, dataProductTypeList));
    }


}