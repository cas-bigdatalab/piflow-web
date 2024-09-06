package cn.cnic.component.dataProduct.domain;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.JsonUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.common.Eunm.DataProductMetaDataStatus;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.dataProduct.entity.DataProduct;
import cn.cnic.component.dataProduct.entity.ProductUser;
import cn.cnic.component.dataProduct.mapper.DataProductMapper;
import cn.cnic.component.dataProduct.mapper.DataProductMetaDataMapper;
import cn.cnic.component.dataProduct.mapper.ProductUserMapper;
import cn.cnic.component.dataProduct.vo.SharePlatformMetadata;
import cn.cnic.component.dataProduct.vo.DataProductMetaDataView;
import cn.cnic.component.dataProduct.vo.DataProductVo;
import cn.cnic.component.dataProduct.vo.ProductUserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static cn.cnic.component.dataProduct.util.DataProductUtil.getFileNameFromPath;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class DataProductDomain {

    private static Logger logger = LoggerUtil.getLogger();

    @Autowired
    private DataProductMapper dataProductMapper;
    @Autowired
    private ProductUserMapper productUserMapper;
    @Autowired
    private DataProductMetaDataMapper dataProductMetaDataMapper;

    public int insert(DataProduct dataProduct) {
        return dataProductMapper.insert(dataProduct);
    }

    public int addBatch(List<DataProduct> dataProducts) {
        return dataProductMapper.addBatch(dataProducts);
    }

    public int updateStateByProcessIdWithNoChangeUser(String processId, Integer state) {
        return dataProductMapper.updateStateByProcessIdWithNoChangeUser(processId, state, DateUtils.dateTimesToStr(new Date()));
    }

    public List<DataProductVo> getListByProcessId(String processId) {
        return dataProductMapper.getListByProcessId(processId);
    }

    public int update(DataProduct dataProduct) {
        return dataProductMapper.update(dataProduct);
    }

    public int updatePermission(DataProduct dataProduct) {
        return dataProductMapper.updatePermission(dataProduct);
    }

    /**
     * @param dataProduct:
     * @return int
     * @author tianyao
     * @description 对已发布、下架的数据产品删除，是转换给待发布，，如果是待发布、或者生成失败，则删除
     * @date 2024/4/21 15:27
     */
    public int delete(DataProduct dataProduct) {
        return dataProductMapper.delete(dataProduct);
    }

    public DataProduct getById(Long id) {
        return dataProductMapper.getById(id);
    }

    public List<DataProduct> getListByName(String name) {
        return dataProductMapper.getListByName(name);
    }

    public List<DataProduct> getListByNameAndId(String id, String name) {
        return dataProductMapper.getListByNameAndId(id,name);
    }

    public DataProduct getBasicInfoById(Long id) {
        return dataProductMapper.getBasicInfoById(id);
    }

    public int down(DataProduct dataProduct) {
        return dataProductMapper.down(dataProduct);
    }

    public List<DataProductVo> getByPageForHomePage(DataProductVo dataProductVo, String username) {
        return dataProductMapper.getByPageForHomePage(dataProductVo.getProductTypeId(), dataProductVo.getKeyword(), username);
    }

    public List<DataProductVo> getByPageForPublishing(DataProductVo dataProductVo) {
        return dataProductMapper.getByPageForPublishing(dataProductVo);
    }

    public List<DataProductVo> getByPageForPublishingWithAdmin(DataProductVo dataProductVo) {
        return dataProductMapper.getByPageForPublishingWithAdmin(dataProductVo);
    }

    public List<DataProductVo> getByPageForPublishingWithSdPublisher(DataProductVo dataProductVo) {
        return dataProductMapper.getByPageForPublishingWithSdPublisher(dataProductVo);
    }

    public List<DataProductVo> getByPageForPublishingWithORSAdmin(DataProductVo dataProductVo) {
        return dataProductMapper.getByPageForPublishingWithORSAdmin(dataProductVo);
    }

    public List<ProductUserVo> getByPageForPermission(ProductUserVo productUserVo, String username) {
        return dataProductMapper.getByPageForPermission(productUserVo, username);
    }

    public DataProductVo getFullInfoById(Long id) {
        return dataProductMapper.getFullInfoById(id);
    }

    public ProductUser getApplyInfoByUserName(Long dataProductId,String username) {
        return productUserMapper.getOneByUserName(dataProductId,username);
    }

    public int insertApplyInfo(ProductUser insertApply) {
        return productUserMapper.insert(insertApply);
    }

    public int permissionForUse(ProductUser productUser) {
        return productUserMapper.permissionForUse(productUser);
    }

    public ProductUser getPermissionById(Long id) {
        return productUserMapper.getById(id);
    }

    public List<DataProduct> getByIds(String[] ids) {
        return dataProductMapper.getByIds(ids);
    }

    public int updateEnableFlagToFalse(String[] ids) {
        return dataProductMapper.updateEnableFlagToFalse(ids);
    }

    public boolean insertOrUpdateDataProductMetaDataVo(DataProductMetaDataView metaDataView, String filePath) {
        SharePlatformMetadata dto = new SharePlatformMetadata();
        dto.setId(metaDataView.getIdentifier());
        dto.setReviewStatus(DataProductMetaDataStatus.REVIEWED.getValue());
        dto.setIconPath(transferToReal(metaDataView.getIconAddress()));
        dto.setDocumentationPath(transferToReal(metaDataView.getDocumentationAddress()));
        dto.setMetadataFilePath(filePath);
        dto.setCrtDttm(new Date());
        dto.setLastUpdatedDttm(new Date());
        dto.setMetadata(JsonUtils.toJsonNoException(metaDataView));
        SharePlatformMetadata metadata = dataProductMetaDataMapper.selectById(metaDataView.getIdentifier());
        // mybatis-plus没有提供on duplicate key update的功能，只能先查询后插入
        if (metadata == null) { //如果不存在则插入
            return dataProductMetaDataMapper.insert(dto) == 1; //返回插入条数,为1则为插入成功
        } else {
            return dataProductMetaDataMapper.updateById(dto) == 1;
        }
    }


    public void updateDataProductStatus(String identifier, Integer status, String message) {
        try {
            SharePlatformMetadata dto = new SharePlatformMetadata();
            dto.setId(identifier);
            dto.setReviewStatus(status);
            dto.setReviewMessage(message);
            dataProductMetaDataMapper.updateById(dto);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateDataProductStatus error, identifier:{}, status:{}, message:{}", identifier, status, message);
        }
    }

    /**
     * 上传的是相对地址,需要转为真实地址
     * @param iconPath
     * @return
     */
    public static  String transferToReal(String iconPath) {
        String path= SysParamsCache.FILE_PATH;
        String fileName = getFileNameFromPath(iconPath);

        return path + fileName;
    }

    public boolean updateDataProductMetaDataStatus(String dataProductId, String releasedDatasetUrl, int status, String message) {

        SharePlatformMetadata dto = new SharePlatformMetadata();
        dto.setId(dataProductId);
        dto.setReviewStatus(status);
        dto.setLastUpdatedDttm(new Date());
        if (status == DataProductMetaDataStatus.POSTED.getValue() || status == DataProductMetaDataStatus.NEEDCHANGED.getValue()) {
            dto.setProductUrl(releasedDatasetUrl);
            dto.setReviewMessage(message);
        }
        return dataProductMetaDataMapper.updateById(dto) == 1;
    }



    public SharePlatformMetadata getDataProductMetaDataById(String id) {
        QueryWrapper<SharePlatformMetadata> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        SharePlatformMetadata res = dataProductMetaDataMapper.selectOne(wrapper);
        return res;
    }
}
