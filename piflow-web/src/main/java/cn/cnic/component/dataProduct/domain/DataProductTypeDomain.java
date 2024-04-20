package cn.cnic.component.dataProduct.domain;

import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import cn.cnic.component.dataProduct.mapper.DataProductTypeMapper;
import cn.cnic.component.dataProduct.vo.DataProductTypeVo;
import cn.cnic.component.flow.domain.StopsDomain;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.mapper.FlowMapper;
import cn.cnic.component.flow.mapper.PathsMapper;
import cn.cnic.component.flow.utils.FlowGlobalParamsUtils;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.component.mxGraph.domain.MxGraphModelDomain;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
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
public class DataProductTypeDomain {

    @Autowired
    private DataProductTypeMapper dataProductTypeMapper;

    public DataProductType getById(Long id) {
        return dataProductTypeMapper.getById(id);
    }

    public DataProductType getByNameAndParentId(DataProductType dataProductType) {
        return dataProductTypeMapper.getByNameAndParentId(dataProductType.getName(),dataProductType.getParentId());
    }

    public Long save(DataProductType dataProductType) {
        return dataProductTypeMapper.insert(dataProductType);
    }

    public int update(DataProductType dataProductType) {
        return dataProductTypeMapper.update(dataProductType);
    }

    public int delete(Long id) {
        return dataProductTypeMapper.deleteById(id);
    }

    public List<DataProductType> getAll(String userName) {
        if(StringUtils.isBlank(userName)){
            return dataProductTypeMapper.getAllWithNoLogin();
        }
        return dataProductTypeMapper.getAll(userName);
    }

    public ProductTypeAssociate getAssociateByTypeIdAndUserName(Long typeId, String username) {
        return dataProductTypeMapper.getAssociateByTypeIdAndUserName(typeId,username);
    }

    public int insertAssociate(ProductTypeAssociate associate) {
        return dataProductTypeMapper.insertAssociate(associate);
    }

    public int updatePreference(Long id, Integer preference) {
        return dataProductTypeMapper.updatePreference(id,preference);
    }

    public ProductTypeAssociate getAssociateByAssociateId(String associateId) {
        return dataProductTypeMapper.getAssociateByAssociateId(associateId);
    }

    public int updateAssociate(ProductTypeAssociate associate) {
        return dataProductTypeMapper.updateAssociate(associate);
    }

    /**
     * @param id:
     * @return List<DataProductType>
     * @author tianyao
     * @description 递归获取自己以及所有子类
     * @date 2024/2/28 17:43
     */
    public List<DataProductType> getDataProductTypeAndAllChildById(Long id) {
        List<DataProductType> result = new ArrayList<>();
        DataProductType parentType = dataProductTypeMapper.getById(id);
        if(ObjectUtils.isNotEmpty(parentType)){
            result.add(parentType);
            getAllChildType(id,result);
        }
        return result;
    }

    private void getAllChildType(Long parentId, List<DataProductType> result) {
        List<DataProductType> typeList = dataProductTypeMapper.getByParentId(parentId);
        if(CollectionUtils.isNotEmpty(typeList)){
            result.addAll(typeList);
            for (DataProductType dataProductType : typeList) {
                getAllChildType(dataProductType.getId(), result);
            }
        }
    }

    public List<DataProductType> getByFlowPublishingIds(List<String> flowPublishingIds) {
        return dataProductTypeMapper.getByFlowPublishingIds(flowPublishingIds);
    }
}
