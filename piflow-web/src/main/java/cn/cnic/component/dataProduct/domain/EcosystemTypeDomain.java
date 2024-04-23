package cn.cnic.component.dataProduct.domain;

import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.dataProduct.entity.EcosystemType;
import cn.cnic.component.dataProduct.entity.EcosystemTypeAssociate;
import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import cn.cnic.component.dataProduct.mapper.DataProductTypeMapper;
import cn.cnic.component.dataProduct.mapper.EcosystemTypeMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class EcosystemTypeDomain {

    @Autowired
    private EcosystemTypeMapper ecosystemTypeMapper;

    public EcosystemType getById(Long id) {
        return ecosystemTypeMapper.getById(id);
    }

    /**
     * @param ecosystemTypeIds:
     * @return List<EcosystemType>
     * @author tianyao
     * @description 根据id获取类型，多个用逗号隔开
     * @date 2024/4/20 10:26
     */
    public List<EcosystemType> getByIds(String ecosystemTypeIds) {
        return ecosystemTypeMapper.getByIds(ecosystemTypeIds);
    }

    public Long save(EcosystemType ecosystemType) {
        return ecosystemTypeMapper.insert(ecosystemType);
    }
//    public int update(EcosystemType dataProductType) {
//        return ecosystemTypeMapper.update(dataProductType);
//    }
//
//    public int delete(Long id) {
//        return ecosystemTypeMapper.deleteById(id);
//    }

    public List<EcosystemType> getAll() {
        return ecosystemTypeMapper.getAll();
    }

    public List<EcosystemTypeAssociate> getAssociateByAssociateId(String associateId) {
        return ecosystemTypeMapper.getAssociateByAssociateId(associateId);
    }

    public List<String> getAssociateByEcosystemTypeIdsAndAssociateType(List<Long> ecosystemTypeIds, Integer associateType) {
        return ecosystemTypeMapper.getAssociateByEcosystemTypeIdsAndAssociateType(ecosystemTypeIds, associateType);
    }

    public int updateAssociate(EcosystemTypeAssociate associate) {
        return ecosystemTypeMapper.updateAssociate(associate);
    }

    public int deleteByAssociateId(String associateId) {
        return ecosystemTypeMapper.deleteByAssociateId(associateId);
    }

    public int insertAssociateBatch(List<EcosystemTypeAssociate> associates) {
        return ecosystemTypeMapper.insertAssociateBatch(associates);
    }
}
