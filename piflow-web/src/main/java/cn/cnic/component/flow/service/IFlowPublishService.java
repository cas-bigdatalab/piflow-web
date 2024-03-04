package cn.cnic.component.flow.service;

import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.vo.FlowPublishingVo;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.controller.requestVo.FlowInfoVoRequestAdd;
import cn.cnic.controller.requestVo.FlowInfoVoRequestUpdate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFlowPublishService {

    String publishingStops(FlowPublishingVo vo);

    String getPublishingById(String id);

    String getListByPage(FlowPublishingVo flowPublishingVo);

    String delete(String id);

    String getFlowPublishingListPageByProductTypeId(FlowPublishingVo flowPublishingVo);

    String run(FlowPublishingVo flowPublishingVo) throws Exception;
}
