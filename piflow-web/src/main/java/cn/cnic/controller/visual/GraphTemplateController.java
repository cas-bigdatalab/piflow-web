package cn.cnic.controller.visual;


import cn.cnic.component.visual.entity.GraphTemplate;
import cn.cnic.component.visual.service.GraphTemplateService;
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

/**
 * TODO
 * author:hmh
 * date:2024-02-02
 */
@Api(tags = "图表模板配置管理")
@RestController
@RequestMapping("/visual")
public class GraphTemplateController {
    @Autowired
    private GraphTemplateService graphTemplateService;
    //获取图表模板配置列表
    @PostMapping("/getGraphTemplateList")
    @ApiOperation("获取图表模板配置列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNum",value = "当前页码")
            ,@ApiImplicitParam(name = "pageSize",value = "每页数量")
            ,@ApiImplicitParam(name = "queryContent",value = "查询内容")})
    public ResponseResult<List<GraphTemplate>> getGraphTemplateList(@RequestBody RequestData requestData){
        ResponseResult<List<GraphTemplate>> templateList = graphTemplateService.getGraphTemplateList(requestData);
        return templateList;
    }
    //获取图表模板配置信息
    @PostMapping("/getGraphTemplateById")
    @ApiOperation("用id获取图表模板配置信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "id号码")})
    public ResponseResult<GraphTemplate> getGraphTemplateById(@RequestBody GraphTemplate graphTemplate){
        return graphTemplateService.getGraphTemplateInfo(graphTemplate);
    }
    //添加图表模板配置
    @PostMapping("/addGraphTemplate")
    @ApiOperation("新增图表模板配置")
    @ApiImplicitParams({@ApiImplicitParam(name = "name",value = "模板名称")
            ,@ApiImplicitParam(name = "type",value = "模板类型")
            ,@ApiImplicitParam(name = "description",value = "模板描述")
            ,@ApiImplicitParam(name = "dataBaseId",value = "数据库id" )
            ,@ApiImplicitParam(name = "tableName",value = "表名称" )})
    public ResponseResult addGraphTemplate(@RequestBody GraphTemplate graphTemplate){
        return graphTemplateService.addGraphTemplate(graphTemplate);
    }
    //一键生成-本地数据库中-添加图表模板配置-和图表配置--不用了
//    @PostMapping("/addGraphConfByOne")
//    @ApiOperation("一键生成-图表配置")
//    @ApiImplicitParams({@ApiImplicitParam(name = "name",value = "模板名称")
//            ,@ApiImplicitParam(name = "type",value = "模板类型")
//            ,@ApiImplicitParam(name = "description",value = "模板描述")
//            ,@ApiImplicitParam(name = "tableName",value = "表名称" )})
//    public ResponseResult addGraphConfByOne(@RequestBody GraphTemplate graphTemplate){
//        return graphTemplateService.addGraphConfByOne(graphTemplate);
//    }
    //删除图表模板配置
    @PostMapping("/delGraphTemplate")
    @ApiOperation("用id删除图表模板配置")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "id号码" )})
    public ResponseResult delGraphTemplate(@RequestBody GraphTemplate graphTemplate){
        return graphTemplateService.delGraphTemplate(graphTemplate);
    }
    //更新图表模板配置
    @PostMapping("/updateGraphTemplate")
    @ApiOperation("用id更新图表模板配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id号码" )
            ,@ApiImplicitParam(name = "name",value = "模板名称")
            ,@ApiImplicitParam(name = "description",value = "模板描述")
            ,@ApiImplicitParam(name = "dataBaseId",value = "数据库id")
            ,@ApiImplicitParam(name = "tableName",value = "表名称")})
    public ResponseResult updateGraphTemplate(@RequestBody GraphTemplate graphTemplate){
        return graphTemplateService.updateGraphTemplate(graphTemplate);
    }
}
