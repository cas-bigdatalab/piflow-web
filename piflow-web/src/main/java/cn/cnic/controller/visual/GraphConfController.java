package cn.cnic.controller.visual;


import cn.cnic.component.visual.entity.GraphConf;
import cn.cnic.component.visual.service.GraphConfService;
import cn.cnic.component.visual.util.RequestData;
import cn.cnic.component.visual.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 * author:hmh
 * date:2023-09-21
 */
@Api(tags = "图表配置管理")
@RestController
@RequestMapping("/visual")
public class GraphConfController {
    @Resource(name = "graphConfServiceImpl")
    private GraphConfService graphConfService;

    //图表配置列表
    @PostMapping("/getGraphConfList")
    @ApiOperation("查询图表配置列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNum",value = "当前页码")
            ,@ApiImplicitParam(name = "pageSize",value = "每页数量")
            ,@ApiImplicitParam(name = "queryContent",value = "查询内容")})
    public ResponseResult<List<GraphConf>> getGraphConfList(@RequestBody RequestData requestData){
        //todo : 过滤下用户,只有自己创建的或者admin能看到
        return graphConfService.getGraphConfList(requestData);
    }
    //查询图表配置
    @PostMapping("/getGraphConfById")
    @ApiOperation("用id查询图表配置")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "id")})
    public ResponseResult<GraphConf> getGraphConfById(@RequestBody GraphConf graphConf){
        return graphConfService.getGraphConfById(graphConf);
    }
    //新增图表配置
    @PostMapping("/addGraphConf")
    @ApiOperation("新增图表配置")
    @ApiImplicitParams({@ApiImplicitParam(name = "name",value = "图表配置名称")
                       ,@ApiImplicitParam(name = "description",value = "图表配置描述")
                       ,@ApiImplicitParam(name = "graphTemplateId",value = "图表模板id")
                       ,@ApiImplicitParam(name = "configInfo",value = "具体配置信息,自定义字符串即可")
    })
    public ResponseResult addGraphConf(@RequestBody GraphConf graphConf){
        return graphConfService.addGraphConf(graphConf);
    }
    //删除图表配置
    @PostMapping("/delGraphConf")
    @ApiOperation("用id删除图表配置")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "id")})
    public ResponseResult delGraphConf(@RequestBody GraphConf graphConf){
        return graphConfService.delGraphConf(graphConf);
    }
    //修改图表配置
    @PostMapping("/updateGraphConf")
    @ApiOperation("用id修改图表配置")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "id")
            ,@ApiImplicitParam(name = "name",value = "图表配置名称")
            ,@ApiImplicitParam(name = "description",value = "图表配置描述")
            ,@ApiImplicitParam(name = "graphTemplateId",value = "图表模板id")
            ,@ApiImplicitParam(name = "configInfo",value = "具体配置信息,自定义字符串即可")
            ,@ApiImplicitParam(name = "addFlag",value = "标记（add为新增，update为修改）")})
    public ResponseResult updateGraphConf(@RequestBody GraphConf graphConf){
        return graphConfService.updateGraphConf(graphConf);
    }
//    //查询表格对应的图表配置列表
//    @PostMapping("/getGraphConfByNameAndId")
//    @ApiOperation("查询表格对应的图表配置列表")
//    @ApiImplicitParams({
//             @ApiImplicitParam(name = "dataBaseId",value = "数据库信息表id")
//            ,@ApiImplicitParam(name = "name",value = "图表名")})
//    public ResponseResult<List<GraphConf>> getGraphConfByNameAndId(@RequestBody GraphConf graphConf){
//        return graphConfService.getGraphConfByNameAndId(graphConf);
//    }
}
