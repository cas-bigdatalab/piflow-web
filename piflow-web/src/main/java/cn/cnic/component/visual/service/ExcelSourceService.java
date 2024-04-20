package cn.cnic.component.visual.service;


import cn.cnic.component.visual.entity.GraphTemplate;
import cn.cnic.component.visual.util.RequestData;
import cn.cnic.component.visual.util.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * author:hmh
 * date:2023-11-03
 */
public interface ExcelSourceService {
    ResponseResult uploadExcel(MultipartFile file, GraphTemplate graphTemplate);
    ResponseResult delExcel(RequestData requestData);
    ResponseResult<List<Map>> selectExcelData(RequestData requestData);
    ResponseResult<List<String>> selectExcel();
    ResponseResult<List<String>> getExcelCol(RequestData requestData);
    String testTransactional();


}
