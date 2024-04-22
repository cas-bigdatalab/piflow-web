package cn.cnic.component.visual.service;



import cn.cnic.component.visual.entity.GraphTemplate;
import cn.cnic.component.visual.util.RequestData;
import cn.cnic.component.visual.util.ResponseResult;

import java.util.List;

/**
 * TODO
 * author:hmh
 * date:2024-02-02
 */
public interface GraphTemplateService {
    ResponseResult<List<GraphTemplate>> getGraphTemplateList(RequestData requestData);
    ResponseResult<GraphTemplate> getGraphTemplateInfo(GraphTemplate graphTemplate);
    ResponseResult addGraphTemplate(GraphTemplate graphTemplate);
//    ResponseResult addGraphConfByOne(GraphTemplate graphTemplate);
    ResponseResult delGraphTemplate(GraphTemplate graphTemplate);
    ResponseResult updateGraphTemplate(GraphTemplate graphTemplate);
    GraphTemplate selectById(Integer id);
}
