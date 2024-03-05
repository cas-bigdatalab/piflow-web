package cn.cnic.component.dataProduct.domain;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.component.dataProduct.entity.DataProduct;
import cn.cnic.component.dataProduct.entity.ProductUser;
import cn.cnic.component.dataProduct.mapper.DataProductMapper;
import cn.cnic.component.dataProduct.mapper.ProductUserMapper;
import cn.cnic.component.dataProduct.vo.DataProductVo;
import cn.cnic.component.dataProduct.vo.ProductUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class DataProductDomain {

    @Autowired
    private DataProductMapper dataProductMapper;
    @Autowired
    private ProductUserMapper productUserMapper;

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

    public int delete(DataProduct dataProduct) {
        return dataProductMapper.delete(dataProduct);
    }

    public DataProduct getById(Long id) {
        return dataProductMapper.getById(id);
    }

    public List<DataProduct> getListByName(String name) {
        return dataProductMapper.getListByName(name);
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

    public List<ProductUserVo> getByPageForPermission(ProductUserVo productUserVo, String username) {
        return dataProductMapper.getByPageForPermission(productUserVo,username);
    }

    public DataProductVo getFullInfoById(Long id) {
        return dataProductMapper.getFullInfoById(id);
    }

    public ProductUser getApplyInfoByUserName(String username) {
        return productUserMapper.getOneByUserName(username);
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
}
