package cn.cnic.component.flow.domain;

import cn.cnic.base.utils.FileUtils;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.common.Eunm.FileAssociateType;
import cn.cnic.component.dataProduct.mapper.DataProductTypeMapper;
import cn.cnic.component.flow.domain.StopsDomain;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowPublishing;
import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.mapper.FlowMapper;
import cn.cnic.component.flow.mapper.FlowPublishingMapper;
import cn.cnic.component.flow.mapper.PathsMapper;
import cn.cnic.component.flow.utils.FlowGlobalParamsUtils;
import cn.cnic.component.flow.vo.FlowPublishingVo;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.component.mxGraph.domain.MxGraphModelDomain;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.system.domain.FileDomain;
import cn.cnic.component.system.mapper.FileMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class FlowPublishDomain {

    @Autowired
    private FlowPublishingMapper flowPublishingMapper;
    @Autowired
    private DataProductTypeMapper dataProductTypeMapper;
    @Autowired
    private FileDomain fileDomain;

    /**
     * @param vo:
      * @return FlowPublishing
     * @author tianyao
     * @description 根据发布名称和flowID查询，同一flow的发布名称不能相同
     * @date 2024/2/20 15:54
     */
    public FlowPublishing getByNameAndFlowId(FlowPublishingVo vo) {
        return flowPublishingMapper.getByNameAndFlowId(vo.getName(),vo.getFlowId());
    }

    public int save(FlowPublishing flowPublishing) {
        return flowPublishingMapper.insert(flowPublishing);
    }

    public int update(FlowPublishing flowPublishing) {
        return flowPublishingMapper.update(flowPublishing);
    }

    public FlowPublishing getFullInfoById(String id) {
        return flowPublishingMapper.getFullInfoById(id);
    }

    public List<FlowPublishingVo> getListByPage(String keywords) {
        return flowPublishingMapper.getByKeywords(keywords);
    }

    /**
     * @param id:
      * @return int
     * @author tianyao
     * @description 逻辑删除
     * @date 2024/2/21 13:55
     */
    public int delete(Long id) {
        //逻辑删除
        int i = flowPublishingMapper.delete(id);
        //物理删除关联类型
        int typeDeleteNum = dataProductTypeMapper.deleteByAssociateId(id.toString());
        //物理删除文件  封面、说明书
        fileDomain.deleteByAssociateId(id.toString(), FileAssociateType.FLOW_PUBLISHING_COVER.getValue());
        fileDomain.deleteByAssociateId(id.toString(), FileAssociateType.FLOW_PUBLISHING_INSTRUCTION.getValue());

        return i;
    }

    public List<FlowPublishingVo> getListByProductTypeId(String keywords, Long productTypeId) {
        return flowPublishingMapper.getListByProductTypeId(keywords,productTypeId);
    }

    public List<FlowPublishingVo> getListByProductTypeIds(String keyword, List<Long> productTypeIds) {
        return flowPublishingMapper.getListByProductTypeIds(keyword,productTypeIds);
    }
}
