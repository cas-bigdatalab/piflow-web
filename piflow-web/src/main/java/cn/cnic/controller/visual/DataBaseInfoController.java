package cn.cnic.controller.visual;


import cn.cnic.component.visual.entity.DataBaseInfo;
import cn.cnic.component.visual.entity.GraphConf;
import cn.cnic.component.visual.service.DataBaseInfoService;
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

import java.util.List;
import java.util.Map;

/**
 * TODO
 * author:hmh
 * date:2023-11-10
 */
@RestController
@Api(tags = "数据库管理")
@RequestMapping("/visual")
public class DataBaseInfoController {
    @Autowired
    private DataBaseInfoService dataBaseInfoService;
    //获取数据库列表
    @PostMapping("/getDatabaseList")
    @ApiOperation("获取数据库列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNum",value = "当前页码")
            ,@ApiImplicitParam(name = "pageSize",value = "每页数量")
            ,@ApiImplicitParam(name = "queryContent",value = "查询内容")})
    public ResponseResult<List<DataBaseInfo>> getDatabaseList(@RequestBody RequestData requestData){
        ResponseResult<List<DataBaseInfo>> databaseList = dataBaseInfoService.getDatabaseList(requestData);
        return databaseList;
    }
    //获取数据库信息
    @PostMapping("/getDatabaseInfoById")
    @ApiOperation("用id获取数据库连接信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "id号码")})
    public ResponseResult<DataBaseInfo> getDatabaseInfo(@RequestBody DataBaseInfo dataBaseInfo){
        return dataBaseInfoService.getDatabaseInfo(dataBaseInfo);
    }
    //添加数据库
    @PostMapping("/addDatabase")
    @ApiOperation("新增数据库连接")
    @ApiImplicitParams({@ApiImplicitParam(name = "dbName",value = "数据库名称")
            ,@ApiImplicitParam(name = "description",value = "数据描述")
            ,@ApiImplicitParam(name = "driverClass",value = "数据库驱动")
            ,@ApiImplicitParam(name = "url",value = "数据库地址")
            ,@ApiImplicitParam(name = "userName",value = "用户名")
            ,@ApiImplicitParam(name = "password",value = "密码")})
    public ResponseResult addDatabase(@RequestBody DataBaseInfo dataBaseInfo){
        return dataBaseInfoService.addDatabase(dataBaseInfo);
    }
    //删除数据库
    @PostMapping("/delDatabase")
    @ApiOperation("用id删除数据库连接")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "id号码")})
    public ResponseResult delDatabase(@RequestBody DataBaseInfo dataBaseInfo){
        return dataBaseInfoService.delDatabase(dataBaseInfo);
    }
    //更新数据库
    @PostMapping("/updateDatabase")
    @ApiOperation("用id更新数据库连接")
    @ApiImplicitParams({
             @ApiImplicitParam(name = "id",value = "id号码")
            ,@ApiImplicitParam(name = "dbName",value = "数据库名称")
            ,@ApiImplicitParam(name = "description",value = "数据描述")
            ,@ApiImplicitParam(name = "driverClass",value = "数据库驱动")
            ,@ApiImplicitParam(name = "url",value = "数据库地址")
            ,@ApiImplicitParam(name = "userName",value = "用户名")
            ,@ApiImplicitParam(name = "password",value = "密码")})
    public ResponseResult updateDatabase(@RequestBody DataBaseInfo dataBaseInfo){
        return dataBaseInfoService.updateDatabase(dataBaseInfo);
    }




    //连接数据库，获取数据库中的表名列表
    @PostMapping("/getDBTables")
    @ApiOperation("连接数据库，获取数据库中的表名列表")
    @ApiImplicitParams({
             @ApiImplicitParam(name = "driverClass",value = "数据库驱动" )
            ,@ApiImplicitParam(name = "url",value = "数据库地址" )
            ,@ApiImplicitParam(name = "userName",value = "用户名" )
            ,@ApiImplicitParam(name = "password",value = "密码" )})
    public ResponseResult<List<String>> getDBTables(@RequestBody RequestData requestData){
        ResponseResult<List<String>> listResponseResult = dataBaseInfoService.showTables(requestData);
        return listResponseResult;
    }
    //连接数据库，获取数据库中的表字段列表
    @PostMapping("/getTableCol")
    @ApiOperation("连接数据库，获取数据库中的表字段列表")
    @ApiImplicitParams({
             @ApiImplicitParam(name = "driverClass",value = "数据库驱动" )
            ,@ApiImplicitParam(name = "url",value = "数据库地址" )
            ,@ApiImplicitParam(name = "userName",value = "用户名" )
            ,@ApiImplicitParam(name = "password",value = "密码" )
            ,@ApiImplicitParam(name = "tableName",value = "表名" )})
    public ResponseResult<List<String>> getTableCol(@RequestBody RequestData requestData){
        ResponseResult<List<String>> tableCol = dataBaseInfoService.getTableCol(requestData);
        return tableCol;
    }
    //连接数据库，获取数据库中的表数据
    @PostMapping("/getTableData")
    @ApiOperation("通过图表模板id，获取数据库中的表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "graphTemplateId",value = "图表模板id" )})
    public ResponseResult<List<Map>> getTableData(@RequestBody GraphConf graphConf){
        ResponseResult<List<Map>> maps = dataBaseInfoService.getTableData(graphConf);
        return maps;
    }
    //流水线跳转图表可视化展示接口
    @PostMapping("/getVisualData")
    public ResponseResult getVisualData(@RequestBody RequestData requestData){
        return dataBaseInfoService.getVisualData(requestData);
    }
}
