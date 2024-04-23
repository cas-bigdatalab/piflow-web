package cn.cnic.component.visual.service;



import cn.cnic.component.visual.entity.DataBaseInfo;
import cn.cnic.component.visual.entity.GraphConf;
import cn.cnic.component.visual.util.RequestData;
import cn.cnic.component.visual.util.ResponseResult;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * author:hmh
 * date:2023-11-13
 */
public interface DataBaseInfoService {
    ResponseResult<List<DataBaseInfo>> getDatabaseList(RequestData requestData);
    ResponseResult<DataBaseInfo> getDatabaseInfo(DataBaseInfo dataBaseInfo);
    ResponseResult addDatabase(DataBaseInfo dataBaseInfo);
    ResponseResult delDatabase(DataBaseInfo dataBaseInfo);
    ResponseResult updateDatabase(DataBaseInfo dataBaseInfo);

    ResponseResult<List<Map>> getTableData(GraphConf graphConf);
    ResponseResult<List<String>> showTables(RequestData requestData);
    ResponseResult<List<String>> getTableCol(RequestData requestData);
    ResponseResult<List<Map>> getVisualData(RequestData requestData);
}
