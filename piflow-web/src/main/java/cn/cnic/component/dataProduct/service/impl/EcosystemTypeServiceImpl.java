package cn.cnic.component.dataProduct.service.impl;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.common.Eunm.ProductTypeAssociateType;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.dataProduct.domain.EcosystemTypeDomain;
import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.dataProduct.entity.EcosystemType;
import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import cn.cnic.component.dataProduct.service.IEcosystemTypeService;
import cn.cnic.component.dataProduct.vo.DataProductTypeVo;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class EcosystemTypeServiceImpl implements IEcosystemTypeService {

    private Logger logger = LoggerUtil.getLogger();

    private final EcosystemTypeDomain ecosystemTypeDomain;


    @Autowired
    public EcosystemTypeServiceImpl(EcosystemTypeDomain ecosystemTypeDomain) {
        this.ecosystemTypeDomain = ecosystemTypeDomain;
    }


//    @Override
//    public String save(MultipartFile file, DataProductTypeVo dataProductTypeVo) {
//        String username = SessionUserUtil.getCurrentUsername();
//        Date now = new Date();
//        if (null != dataProductTypeVo.getId()) {
//            //编辑更新
//            //删除并新增数据产品类型封面
//            if(ObjectUtils.isNotEmpty(file)){
//                fileServiceImpl.uploadFile(file, false, FileAssociateType.DATA_PRODUCT_TYPE_COVER.getValue(), dataProductTypeVo.getId().toString());
//            }
//
//            //更新数据产品类型
//            DataProductType dataProductType = dataProductTypeDomain.getById(dataProductTypeVo.getId());
//            //基本数据填充
//            dataProductType.setParentId(dataProductTypeVo.getParentId());
//            dataProductType.setName(dataProductTypeVo.getName());
//            dataProductType.setDescription(dataProductTypeVo.getDescription());
//            dataProductType.setLastUpdateUser(username);
//            dataProductType.setLastUpdateDttm(now);
//            int i = dataProductTypeDomain.update(dataProductType);
//            if (i > 0) {
//                return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
//            } else {
//                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
//            }
//        } else {
//            //新增
//            DataProductType dataProductType = new DataProductType();
//            BeanUtils.copyProperties(dataProductTypeVo, dataProductType);
//            if (null == dataProductType.getParentId() || 0L == dataProductType.getParentId()) {
//                dataProductType.setParentId(0L);
//            }
//            //基础数据填充
//            dataProductType.setCrtDttm(now);
//            dataProductType.setCrtUser(username);
//            dataProductType.setLastUpdateUser(username);
//            dataProductType.setLastUpdateDttm(now);
//            dataProductType.setEnableFlag(true);
//            dataProductType.setVersion(0L);
//
//            //同一ParentID下不允许有重名的类型
//            DataProductType isExist = dataProductTypeDomain.getByNameAndParentId(dataProductType);
//            if (ObjectUtils.isNotEmpty(isExist)) {
//                return ReturnMapUtils.setFailedMsgRtnJsonStr("same name is not allowed in one parent category!!");
//            }
//            if(null == dataProductType.getLevel() || 0 == dataProductType.getLevel()){
//                //递归获取分类的级别
//                Integer level = getLevelWithParentId(dataProductType.getParentId());
//                dataProductType.setLevel(level);
//            }
//            dataProductTypeDomain.save(dataProductType);
//            Long typeId = dataProductType.getId();
//            if (null != typeId && ObjectUtils.isNotEmpty(file)) {
//                try {
//                    //上传封面
//                    fileServiceImpl.uploadFile(file, false, FileAssociateType.DATA_PRODUCT_TYPE_COVER.getValue(), typeId.toString());
//                } catch (Exception e) {
//                    logger.error("upload cover error!! e:====={}", e.getMessage());
//                    return ReturnMapUtils.setFailedMsgRtnJsonStr("upload cover error!!");
//                }
//            }
//            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.ADD_SUCCEEDED_MSG());
//        }
//    }
//
//    private Integer getLevelWithParentId(Long parentId) {
//        if (parentId == 0L) {
//            return 1; // 最顶级为1级
//        } else {
//            //获取父级分类
//            DataProductType parentType = dataProductTypeDomain.getById(parentId);
//            if (parentType != null) {
//                int parentLevel = getLevelWithParentId(parentType.getParentId()); // 递归获取父级分类的层级
//                return parentLevel + 1; // 当前分类的层级为父级分类层级加1
//            } else {
//                return 0; // 错误情况，未找到父级分类
//            }
//        }
//    }
//
//    @Override
//    public String delete(Long id) {
//        int i = dataProductTypeDomain.delete(id);
//        //删除关联的封面
//        fileDomain.deleteByAssociateId(id.toString(),FileAssociateType.DATA_PRODUCT_TYPE_COVER.getValue());
//        if (i > 0) {
//            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.DELETE_SUCCEEDED_MSG());
//        } else {
//            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG());
//        }
//    }

    @Override
    public String get() {
        //获取分类
        List<EcosystemType> ecosystemTypeList = ecosystemTypeDomain.getAll();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data",ecosystemTypeList);
    }


}
