package cn.cnic.controller.visual;


import cn.cnic.component.visual.entity.GraphTemplate;
import cn.cnic.component.visual.service.ExcelSourceService;
import cn.cnic.component.visual.util.RequestData;
import cn.cnic.component.visual.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * author:hmh
 * date:2023-11-03
 */
@Api(tags = "表格数据管理")
@RestController
@RequestMapping("/visual")
public class ExcelSourceController {

    @Autowired
    private ExcelSourceService excelSourceService;
    //图表上传
    @PostMapping(value = "/uploadExcel")
    @ApiOperation("上传excel表格")
    public ResponseResult uploadExcel(MultipartFile file, GraphTemplate graphTemplate){
        return excelSourceService.uploadExcel(file,graphTemplate);
    }

    @PostMapping(value = "/uploadExcelFromPath")
    @ApiOperation("根据HDFS path上传excel表格")
    public ResponseResult uploadExcelFromPath(String path, GraphTemplate graphTemplate){
        return ResponseResult.success();
    }
    //图表删除
    @PostMapping("/delExcel")
    @ApiOperation("图表删除")
    @ApiImplicitParams({@ApiImplicitParam(name = "tableName",value = "表格名(不加文件后缀)")})
    public ResponseResult delExcel(@RequestBody RequestData requestData){
        return excelSourceService.delExcel(requestData);
    }
    //图表中数据查询
    @PostMapping("/selectExcelData")
    @ApiOperation("图表中数据查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "tableName",value = "表格名(不加文件后缀)")})
    public ResponseResult<List<Map>> selectExcelData(@RequestBody RequestData requestData){
        return excelSourceService.selectExcelData(requestData);
    }
    //图表数量查询
    @PostMapping("/selectExcel")
    @ApiOperation("图表数量查询")
    public ResponseResult<List<String>> selectExcel(){
        return excelSourceService.selectExcel();
    }
    //图表字段查询
    @PostMapping("/getExcelCol")
    @ApiOperation("图表字段查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "tableName",value = "表格名(不加文件后缀)")})
    public ResponseResult<List<String>> getExcelCol(@RequestBody RequestData requestData){
        return excelSourceService.getExcelCol(requestData);
    }


//    @PostMapping("/testTransactional")
//    public String testTransactional(){
//        return excelSourceService.testTransactional();
//    }
}
