package cn.cnic.component.visual.service;



import cn.cnic.component.visual.entity.GraphConf;
import cn.cnic.component.visual.util.RequestData;
import cn.cnic.component.visual.util.ResponseResult;

import java.util.List;

/**
 * TODO
 * author:hmh
 * date:2023-09-21
 */
public interface GraphConfService {

    ResponseResult<List<GraphConf>> getGraphConfList(RequestData requestData);
    ResponseResult<GraphConf> getGraphConfById(GraphConf graphConf);
    ResponseResult addGraphConf(GraphConf graphConf);
    ResponseResult delGraphConf(GraphConf graphConf);
    ResponseResult updateGraphConf(GraphConf graphConf);
//    ResponseResult<List<GraphConf>> getGraphConfByNameAndId(GraphConf graphConf);

}
