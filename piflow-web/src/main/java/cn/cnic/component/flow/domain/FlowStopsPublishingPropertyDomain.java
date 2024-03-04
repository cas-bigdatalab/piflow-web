package cn.cnic.component.flow.domain;

import cn.cnic.base.utils.FileUtils;
import cn.cnic.base.utils.HttpUtils;
import cn.cnic.common.constant.ApiConfig;
import cn.cnic.component.dataProduct.mapper.DataProductTypeMapper;
import cn.cnic.component.flow.entity.FlowPublishing;
import cn.cnic.component.flow.entity.FlowStopsPublishingProperty;
import cn.cnic.component.flow.mapper.FlowPublishingMapper;
import cn.cnic.component.flow.mapper.FlowStopsPublishingPropertyMapper;
import cn.cnic.component.flow.vo.FlowPublishingVo;
import cn.cnic.component.system.domain.FileDomain;
import cn.cnic.component.system.entity.File;
import cn.cnic.component.system.mapper.FileMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class FlowStopsPublishingPropertyDomain {

    @Autowired
    private FlowStopsPublishingPropertyMapper flowStopsPublishingPropertyMapper;
    @Autowired
    private FileMapper fileMapper;

    public int saveBatch(List<FlowStopsPublishingProperty> properties) {
        int i = flowStopsPublishingPropertyMapper.insertBatch(properties);
        return i;
    }

    public int updateBatch(List<FlowStopsPublishingProperty> properties) {
        return flowStopsPublishingPropertyMapper.updateBatch(properties);
    }

    public int deleteByPublishingId(long flowPublishingId) {
        int i = 0;
        List<FlowStopsPublishingProperty> stopsListByPublishingId = flowStopsPublishingPropertyMapper.getStopsListByPublishingId(flowPublishingId);
        if (CollectionUtils.isNotEmpty(stopsListByPublishingId)) {
            i = flowStopsPublishingPropertyMapper.deleteByPublishingId(flowPublishingId);//逻辑删除
            List<File> files = stopsListByPublishingId.stream()
                    .map(property -> {
                        File file = new File();
                        file.setId(property.getFileId());
                        file.setFilePath(property.getFilePath());
                        file.setFileName(property.getFileName());
                        return file;
                    })
                    .filter(file -> file.getId()!=null)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(files)) {
                String defaultFs = FileUtils.getDefaultFs();
                //物理删除
                fileMapper.deleteBatchById(files.stream().map(File::getId).collect(Collectors.toList()));
                files.forEach(file -> FileUtils.deleteHdfsFile(file.getFilePath(),defaultFs));
            }
        }
        return i;
    }
}
